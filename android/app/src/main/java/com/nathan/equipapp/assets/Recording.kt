package com.nathan.equipapp.assets

import android.util.Log
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.*

class Recording {
    var speaker: String
    var name: String
    var date: Date
    var url: String
    var formattedDate: String? = null
    val TAG = this.javaClass.simpleName

    constructor(newSpeaker: String, newName: String, newDate: Date, newUrl: String) {
        this.speaker = newSpeaker
        this.name = newName
        this.date = newDate
        this.url = newUrl
        createDate()

    }

    fun createDate() {

        var formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("eee, d MMMM YYYY")
        val aucklandTime = LocalDateTime.ofInstant(DateTimeUtils.toInstant(date), ZoneId.of("Pacific/Auckland"))
        formattedDate = aucklandTime.format(formatter)

    }
}