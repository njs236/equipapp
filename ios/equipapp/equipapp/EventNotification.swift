//
//  EventNotification.swift
//  equipapp
//
//  Created by Nathan Sinclair on 12/12/18.
//  Copyright © 2018 Nathan Sinclair. All rights reserved.
//

import UIKit

class EventNotification: NSObject {
    
    var date: Date
    var message: String
    var delay: Double
    var id: String? = nil
    var repeats: Bool? = nil
    var end: Date? = nil
    
    init(newDate: Date, newMessage: String, newDelay: Double) {
        self.date = newDate
        self.message = newMessage
        self.delay = newDelay
    }
    
    init(newDate: Date, newDelay: Double, newMessage: String, notificationID: String) {
        self.date = newDate
        self.delay = newDelay
        self.message = newMessage
        self.id = notificationID
    }
    
    init(newDate: Date, newDelay: Double, newMessage:String, newRepeat: Bool, newEnd: Date) {
        self.date = newDate
        self.delay = newDelay
        self.message = newMessage
        self.repeats = newRepeat
        self.end = newEnd
    }
    
    
    

}
