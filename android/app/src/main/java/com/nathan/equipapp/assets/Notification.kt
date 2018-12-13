package com.nathan.equipapp.assets

import java.util.*

class Notification {
    var date: Date
    var delay: Long
    var description: String

    constructor(newDate: Date, newDelay: Long, newDescription: String) {
        date = newDate
        delay = newDelay
        description = newDescription
    }
}