package com.nathan.equipapp.assets

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.*

class Question {

    var author: String
    var description: String
    var speaker: String
    var date: Date
    var formattedDate: String? = ""
    var documentID: String? = ""

            constructor (newSpeaker: String, newAuthor: String, newDescription: String, newDate: Date, id: String) {
                this.speaker = newSpeaker
                this.author = newAuthor
                this.description = newDescription
                this.date = newDate
                documentID = id
                createDate()
            }



    fun createDate() {

        var formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("eee, d MMMM YYYY")
        val aucklandTime = LocalDateTime.ofInstant(DateTimeUtils.toInstant(date), ZoneId.of("Pacific/Auckland"))
        formattedDate = aucklandTime.format(formatter)

    }
}