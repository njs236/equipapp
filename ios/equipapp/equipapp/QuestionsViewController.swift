//
//  QuestionsViewController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 4/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import Firebase

class QuestionsViewController: UIViewController,UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return UITableViewCell()
    }
    
    var handle: AuthStateDidChangeListenerHandle!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let db = Firestore.firestore()
        
        let settings = db.settings
        settings.areTimestampsInSnapshotsEnabled = true
        db.settings = settings
        
        db.collection("questions").getDocuments() { (querySnapshot, err) in
            if let err=err {
                print ("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    let author = document.get("author")
                    let cr_date = document.get("cr_date") as! Timestamp
                    let date : Date = cr_date.dateValue()
                    let speaker = document.get("speaker")
                    let description = document.get("description")
                    
                    print ("author: \(author)")
                    print ("cr_date: \(date)")
                    print ("speaker: \(speaker)")
                    print ("description: \(description)")
                }
            }
            
        }

        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        handle = Auth.auth().addStateDidChangeListener { (auth, user) in
            
            if let user=user {
                let uid = user.uid
                let email = user.email
                let photURL = user.photoURL
            }
            
        }
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        Auth.auth().removeStateDidChangeListener(handle!)
        
        
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
