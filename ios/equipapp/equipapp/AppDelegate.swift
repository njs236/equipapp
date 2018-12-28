//
//  AppDelegate.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import GoogleMaps
import Firebase
import AVFoundation
import UserNotifications
import AppCenter
import AppCenterAnalytics
import AppCenterCrashes


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    var window: UIWindow?
    var db: Firestore!
    var notis = [EventNotification]()
    var instantNotis = [EventNotification]()
    var bgTask: UIBackgroundTaskIdentifier?
    var timer: Timer?


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        let audioSession = AVAudioSession.sharedInstance()
 
        do {
            try audioSession.setCategory(AVAudioSession.Category.playback, mode: AVAudioSession.Mode.default, options: AVAudioSession.CategoryOptions.allowAirPlay)
        } catch {
            print("Setting category to AVAudioSessionCategoryPlayback failed.")
        }
        GMSServices.provideAPIKey("AIzaSyBE9sNAykr_FCyFWjdl-kKc2CeufTSq1pI")
        FirebaseApp.configure()
       
        
        Auth.auth().signInAnonymously() { (authResult, error) in
            
            if let err = error {
                print ("sign in received error: \(err)")
                
            } else {
                let user = authResult?.user
                let isAnonymous = user?.isAnonymous
                let uid = user?.uid
                
                print("successfully signed in \(uid)")
            }
        }
        db = Firestore.firestore()
        
        let settings = db.settings
        settings.areTimestampsInSnapshotsEnabled = true
        db.settings = settings
        
        let center = UNUserNotificationCenter.current()
        center.delegate = self
        
        //Request permission to display alerts and play sounds
        center.requestAuthorization(options: [.alert, .sound]) {
            (granted, error) in
           
        }
        
        startLongRunningTask()
        
        MSAppCenter.start("46a333a6-7a65-4f04-959b-f0610be914ed", withServices: [MSAnalytics.self, MSCrashes.self])
        
        return true
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.alert, .badge, .sound])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        completionHandler()
    }
    
    func startLongRunningTask() {
        timer?.invalidate()
        let timeInterval: TimeInterval = 10
        timer = Timer.scheduledTimer(withTimeInterval: timeInterval, repeats: true) { (timer) in
            self.retrieveNotificationData()
            self.deleteLateNotifications()
        }
        
    }
    
    func sendInstantNotification() {
        let user = Auth.auth().currentUser
        print("appDelegate: sendInstantNotification")
        for notification in instantNotis {
            self.db.collection(user!.uid).document(notification.id!).getDocument { (documentSnapshot, error) in
                print("appDelegate: snapshot: \(documentSnapshot)")
                print("appDelegate: data: \(documentSnapshot!.data())")
                if (documentSnapshot?.data() != nil) {
                    
                } else {
                     self.db.collection(user!.uid)
                        .document(notification.id!)
                        .setData(["timeToDeath": notification.delay,
                                "date": notification.date,
                                "message": notification.message]){ err in
                                    if let err = err {
                                        print ("Error adding document: \(err)")
                                    } else {
                                        print ("Document added")
                                        //condition: if a user fetches a notification after the TimeToDeath, don't send. only send notification within the time frame it lasts for
                                        let delayInMillis = notification.delay * 1000
                                        let time = Date()
                                        let triggerTimeToDeath = Date(timeInterval: delayInMillis, since: notification.date)
                                        
                                        if (time < triggerTimeToDeath) {
                                            self.sendNotification(message: notification.message)
                                        } else {
                                            print("this notification was sent after it is allowed to be retrived by logged in user")
                                        }
                                    }
                    }
                    
                }
            }
        }
    }
    
    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // check db
      
        
        
        
        
        startLongRunningTask()
        
    }
    
    func deleteLateNotifications() {
        db.collection("notis").whereField("now", isEqualTo: true).getDocuments(completion: { (querySnapshot, error) in
            for document in querySnapshot!.documents {
                let timestamp = document.get("date") as! Timestamp
                let date = timestamp.dateValue()
                let delay = document.get("delay") as! Double
                let delayInMillis = delay * 1000
                
                let time = Date()
                let eventTriggerTime = Date(timeInterval: delayInMillis, since: date)
                if (time > eventTriggerTime) {
                    self.db.collection("notis").document(document.documentID).delete(completion: { (error) in
                        
                    })
                }
                
            }
        })
    }
    
    func retrieveNotificationData() {
        notis.removeAll()
        instantNotis.removeAll()
        
        let collection = db.collection("notis")
        
        collection.order(by: "date", descending:true).getDocuments() {
            (querySnapshot, err) in
            
                for document in querySnapshot!.documents {
                    let timestamp = document.get("date") as! Timestamp
                    let date = timestamp.dateValue()
                    let message = document.get("description") as! String
                    let delay = document.get("delay") as! Double
                    var repeats: Bool = false
                    var endTimestamp: Timestamp? = nil
                    var end: Date? = nil
                    
                    if (document.get("now") != nil) {
                        // For debuf. retrieve if the notification is to be sent immediately or wait for a certain time.
                        let now = document.get("now") as! Bool
                        
                        self.instantNotis += [EventNotification(newDate: date, newDelay: delay, newMessage: message, notificationID: document.documentID)]
                    } else {
                        //if the notification is repeating
                        if (document.get("repeats") != nil) {
                            repeats = document.get("repeats") as! Bool
                            endTimestamp = document.get("end") as! Timestamp
                            end = endTimestamp?.dateValue()
                            self.notis += [EventNotification(newDate: date, newDelay: delay, newMessage: message, newRepeat: repeats, newEnd: end!)]
                        } else {
                            self.notis += [EventNotification(newDate: date, newMessage: message, newDelay: delay)]
                            
                        }
                    }
                    
                }
                if (self.instantNotis.count > 0) {
                    self.sendInstantNotification()
                }
                if (self.notis.count > 0) {
                    self.checkNotificationAgainstCurrentTime()
                
                }
                //check the notifications against the current time.
            
            
            
            
            
            }
    }
    
    func checkNotificationAgainstCurrentTime() {
        //current time
        let time = Date()
        
        for notification in notis {
            let subtractDelay = -notification.delay
            let eventTriggerTime = Date(timeInterval: subtractDelay, since: notification.date)
            
            if (notification.repeats != nil) {
                let endTime = Date(timeInterval: subtractDelay, since: notification.date)
                //determine that the time is the same but the day is different and falls between start and end date
                if (time >= eventTriggerTime && time <= endTime) {
                    let userCalendar = Calendar.current
                    let timeComponents = userCalendar.dateComponents([.hour, .minute, .second], from: eventTriggerTime)
                    let currentTimeComponents = userCalendar.dateComponents([.hour, .minute, .second], from: time)
                    var notificationTimeOfDay = (timeComponents.hour! * 60 * 60)
                    notificationTimeOfDay += timeComponents.minute! * 60
                    notificationTimeOfDay += timeComponents.second!
                    var timeOfDay = (currentTimeComponents.hour! * 60 * 60)
                    timeOfDay += currentTimeComponents.minute! * 60
                    timeOfDay += currentTimeComponents.second!
                    
                    if ((timeOfDay - 5) <= notificationTimeOfDay && notificationTimeOfDay < (timeOfDay + 5)) {
                        self.sendNotification(message: notification.message)
                    }
                }
            } else {
                let userCalendar = Calendar.current
                let notiComponents = userCalendar.dateComponents([.hour, .minute, .second], from: eventTriggerTime)
                let currentTimeComponents = userCalendar.dateComponents([.hour, .minute, .second], from: time)
                var notificationTimeOfDay = (notiComponents.hour! * 60 * 60)
                notificationTimeOfDay += notiComponents.minute! * 60
                notificationTimeOfDay += notiComponents.second!
                var timeOfDay = (currentTimeComponents.hour! * 60 * 60)
                timeOfDay += currentTimeComponents.minute! * 60
                timeOfDay += currentTimeComponents.second!
                
               
                //send Notification
                if ((notificationTimeOfDay - 5) <= timeOfDay && timeOfDay < (notificationTimeOfDay + 5)) {
                    self.sendNotification(message: notification.message)
                }
                
            }
        }
    }
    
    func sendNotification(message: String) {
        let content = UNMutableNotificationContent()
        content.title="Equip Conference"
        content.body = message
        content.sound = UNNotificationSound.default
       
        let notificationRequest = UNNotificationRequest(identifier: "com.nathan.equipapp.notification", content: content, trigger: nil)
        
        let center = UNUserNotificationCenter.current()
        center.add(notificationRequest) { (err) in
            if (err != nil) {
                
            }
        }
    }
    

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        
        bgTask = application.beginBackgroundTask(withName: "sendNotifications") {
            application.endBackgroundTask(self.bgTask!)
            self.bgTask = UIBackgroundTaskIdentifier.invalid
            
            
        }
        startLongRunningTask()
        
        
        
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


}

