//
//  NotificationViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 4/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import Firebase

class NotificationViewController: UIViewController {
 
    @IBOutlet var sendButton: UIButton!
    @IBOutlet var datePickerEvent: UIDatePicker!
    @IBOutlet var eventNameTextField: UITextField!
    
    var db: Firestore!
    
    @IBOutlet var switchNotification: UISwitch!
 
    @IBAction func switchNoti_changed(_ sender: Any) {
        
        print("switchNoti: \(switchNotification.isOn)")
        if (switchNotification.isOn) {
            datePickerEvent.isHidden = true
        } else {
            datePickerEvent.isHidden = false
            
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
    db = Firestore.firestore()
        db.collection("notis").getDocuments() { (querySnapshot, err) in
            
            if let err = err {
                print ("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    
                }
            }
        }
        
        

        //Do any additional setup after loading the view.
    }
    
    @IBAction func sendButtonClick(_ sender: UIButton) {
        // get data from field and send to firebase database
        
        var ref : DocumentReference? = nil
        var date :Date? = nil
        var docData: [String: Any]
        
        if (switchNotification.isOn) {
            date = Date()
            docData = ["date": date,
                       "description": eventNameTextField.text,
                       "now": true,
                       "delay": 300]
            
        } else {
            date = datePickerEvent.date
            docData = ["date": date,
                       "description": eventNameTextField.text,
                       "delay": 300]
            
        }
        
        ref = db.collection("notis").addDocument(data: docData) { err in
            if let err = err {
                print ("Error adding document: \(err)")
            } else {
                print ("Document added with ID: \(ref!.documentID)")
            }
        }
        
        
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
