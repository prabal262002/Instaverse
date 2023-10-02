package com.example.instaverse.Models

import java.util.Date

class Message {
    var message: String? = null
    var senderMail:String? = null
    var timestamp: Date? = null
    constructor()
    constructor(message: String?,senderMail:String?, timestamp: Date){
        this.message = message
        this.senderMail = senderMail
        this.timestamp = timestamp
    }
}