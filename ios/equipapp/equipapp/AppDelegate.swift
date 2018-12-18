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


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    var window: UIWindow?
    var db: Firestore!
    var notis = [EventNotification]()
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
        
        return true
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
    }
    
    func startLongRunningTask() {
        timer?.invalidate()
        let timeInterval: TimeInterval = 1
        timer = Timer.scheduledTimer(withTimeInterval: timeInterval, repeats: true) { (timer) in
            self.retrieveNotificationData()
        }
        
    }
    
    func application(_ application: UIApplication, performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // check db
      
        
        
        
        
        startLongRunningTask()
        
    }
    
    func retrieveNotificationData() {
        notis.removeAll()
        
        let collection = db.collection("notis")
        
        collection.order(by: "date", descending:true).getDocuments() {
            (querySnapshot, err) in
            
            if false {
                
            } else {
                for document in querySnapshot!.documents {
                    let timestamp = document.get("date") as! Timestamp
                    let date = timestamp.dateValue()
                    let message = document.get("description") as! String
                    let delay = document.get("delay") as! Double
                    self.notis += [EventNotification(newDate: date, newMessage: message, newDelay: delay)]
                }
                //check the notifications against the current time.
                if false {
                    self.checkNotificationAgainstCurrentTime()
                }
                self.sendNotification(message: self.notis[0].message)
            }
        }
    }
    
    func checkNotificationAgainstCurrentTime() {
        //current time
        let date = Date()
        
        for notification in notis {
            let subtractDelay = -notification.delay
            let eventTriggerTime = Date(timeInterval: subtractDelay, since: notification.date)
            if (date == eventTriggerTime) {
                //send Notification
                sendNotification(message: notification.message)
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

