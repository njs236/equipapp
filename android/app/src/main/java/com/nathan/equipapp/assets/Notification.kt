package com.nathan.equipapp.assets

import java.util.*

class Notification {
    var date: Date
    var delay: Long
    var description: String
    var id: String? = null
    var repeat: Boolean? = null
    var end: Date? = null

    constructor(newDate: Date, newDelay: Long, newDescription: String) {
        date = newDate
        delay = newDelay
        description = newDescription
    }

    constructor(newDate: Date, newDelay: Long, newDescription: String, notificationID: String) {
        date = newDate
        delay = newDelay
        description = newDescription
        id = notificationID

    }

    constructor(newDate: Date, newDelay: Long, newDescription: String, newRepeat: Boolean, newEnd: Date) {
        date = newDate
        delay = newDelay
        description = newDescription
        repeat = newRepeat
        end = newEnd
    }
}