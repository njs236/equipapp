//
//  Question.swift
//  equipapp
//
//  Created by Nathan Sinclair on 5/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit

class Question: NSObject {
    
    var speaker: String
    var author: String
    var question: String
    var cr_date: Date
    
    init(newSpeaker: String, newAuthor: String, newQuestion: String, newDate: Date) {
        self.speaker = newSpeaker
        self.author = newAuthor
        self.question = newQuestion
        self.cr_date = newDate
    }

}
