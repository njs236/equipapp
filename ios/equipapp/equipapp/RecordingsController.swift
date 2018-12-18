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
    var db : Firestore? = nil
    var selectedIndex: Int? = -1
    func didTapCell(cell: CustomTableViewCell, index: Int) {
       // get cell.isSelected to determine if play or not.
        
        if (selectedIndex != -1) {
            let indexPath = IndexPath.init(row: self.selectedIndex!, section: 0)
            let prevcell = self.tableView.cellForRow(at: indexPath) as! CustomTableViewCell
            prevcell.playButton.isSelected = false
        }
        //store the index of the played talk.
        self.selectedIndex = index
        //select recording that corresponds to the pressed button.
        let recording = recordings[index]
        // select the url of the recording.
        let downloadUrl = recording.downloadURL
        // initialize player
        self.avPlayer = AVPlayer(url: downloadUrl)
        
        //play talk
        if (cell.playButton.isSelected) {
        self.avPlayer?.play()
        } else {
            self.avPlayer?.pause()
            self.selectedIndex = -1
        }
        //Log the url of the talk.
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
        
        db = Firestore.firestore()
    
    
    
        
        //Use firebase storage to get recording and play.

        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        
        recordings.removeAll()
        db!.collection("recordings").order(by:"date", descending: true).order(by: "speaker").order(by:"url", descending: true).getDocuments() { (querySnapshot, err) in
            
            if let err = err {
                print("Error in downloading: \(err)")
            } else {
                //succesful download
                for document in querySnapshot!.documents {
                    let url = document.get("url") as! String
                    let timestamp = document.get("date") as! Timestamp
                    let date = timestamp.dateValue()
                    var name = ""
                    if (document.get("name") != nil) {
                        name = document.get("name") as! String
                    }
                    var speaker = ""
                    if (document.get("speaker") != nil) {
                        speaker = document.get("speaker") as! String
                    }
                    let recording = Recording(newRecordingName: name, newDate: date, url: url, newSpeaker: speaker)
                    recording.createDate()
                    self.recordings += [recording]
                    
                    print("url: \(url)")
                    print("name: \(name)")
                    print("date: \(date)")
                }
            }
            self.tableView.reloadData()
            
        }
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RecordingCell") as! CustomTableViewCell
        

        let data = recordings[indexPath.row]
        //TODO. get data of recordings
        cell.nameOfRecording.text = data.recordingName
        cell.dateOfRecording.text = data.dateStringForTable
        cell.speakerOfRecording.text = data.speaker
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
