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
        ref = db.collection("notis").addDocument(data: [
            "date": datePickerEvent.date,
            "description": eventNameTextField.text
            ]) { err in
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
