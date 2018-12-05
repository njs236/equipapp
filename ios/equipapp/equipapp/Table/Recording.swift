//
//  Recording.swift
//  equipapp
//
//  Created by Nathan Sinclair on 5/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit

class Recording: NSObject {
    
    var recordingName :String
    var date: Date
    var dateStringForTable: String = ""
    var downloadURL: URL
    init(newRecordingName: String, newDate: Date, url: String) {
      
        self.recordingName = newRecordingName
        self.date = newDate
        let urlObject = URL(string: url)
        self.downloadURL = urlObject!
        
    }
    
    func createDate() {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd/mm/YYYY"
        self.dateStringForTable = formatter.string(from: date)
    }

}
