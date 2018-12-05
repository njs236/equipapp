//
//  RecordingsController.swift
//  equipapp
//
//  Created by Nathan Sinclair on 22/11/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit
import Firebase

import AVFoundation
import AVKit

class RecordingsController: UIViewController, UITableViewDelegate, UITableViewDataSource, RecordingCellProtocol {
    var avPlayer: AVPlayer? = nil
    func didTapCell(cell: UITableViewCell, index: Int) {
        // play sound
        let recording = recordings[index]
        let downloadUrl = recording.downloadURL
        self.avPlayer = AVPlayer(url: downloadUrl)
        self.avPlayer?.play()
        print("downloadUrl obtained and set: \(downloadUrl)")
    
    }
    
    @IBOutlet var searchBarField: UITextField!
    var recordings = [Recording]()
    var storageRef: StorageReference? = nil
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return recordings.count
    }
        
    @IBOutlet var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        let db = Firestore.firestore()
        recordings.removeAll()
        db.collection("recordings").getDocuments() { (querySnapshot, err) in
            
            if let err = err {
                print("Error in downloading: \(err)")
            } else {
                //succesful download
                for document in querySnapshot!.documents {
                    let url = document.get("downloadURL") as! String
                    let timestamp = document.get("date") as! Timestamp
                    let date = timestamp.dateValue()
                    let name = document.get("name") as! String
                    let recording = Recording(newRecordingName: name, newDate: date, url: url)
                    recording.createDate()
                    self.recordings += [recording]
                    
                    print("url: \(url)")
                    print("name: \(name)")
                    print("date: \(date)")
                }
            }
            self.tableView.reloadData()
            
        }
    
    
    
        
        //Use firebase storage to get recording and play.

        // Do any additional setup after loading the view.
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RecordingCell") as! CustomTableViewCell
        

        let data = recordings[indexPath.row]
        //TODO. get data of recordings
        cell.nameOfRecording.text = data.recordingName
        cell.dateOfRecording.text = data.dateStringForTable
        cell.index = indexPath.row
        cell.cellDelegate = self
        
        
        return cell
        
        
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
