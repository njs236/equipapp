//
//  QuestionViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import Firebase

class QuestionViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let db = Firestore.firestore()
        // testing questions.
       db.collection("questions").getDocuments() { (querySnapshot, err) in
        
        }

        // Do any additional setup after loading the view.
    }
    
    
    @IBOutlet var authorField: UITextField!
    
    @IBOutlet var speakerField: UITextField!
    @IBOutlet var questionView: UITextView!
    @IBOutlet var sendButton: UIButton!
    

    @IBAction func sendButtonClick(_ sender: UIButton) {
        let author = self.authorField.text
        let speaker = self.speakerField.text
        let question = self.questionView.text
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
        let date = Date()
        let cr_date = dateFormatter.string(from: date)
        
        //TODO: parsing info from fields.
        
        
        let db = Firestore.firestore()
        var ref : DocumentReference? = nil
        ref = db.collection("questions").addDocument(data: [
            "author": author,
            "speaker" : speaker,
            "question" : question,
            "cr_date": date
            
        ]){ err in
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
