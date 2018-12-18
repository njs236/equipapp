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
    
    init(newDate: Date, newMessage: String, newDelay: Double) {
        self.date = newDate
        self.message = newMessage
        self.delay = newDelay
    }
    
    

}
