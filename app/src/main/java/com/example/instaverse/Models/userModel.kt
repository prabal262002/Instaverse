package com.example.instaverse.Models

class userModel {
    var image:String?=null
    var name:String?=null
    var email:String?=null
    var password:String?=null
    var followers: MutableList<String> = mutableListOf()
    var following: MutableList<String> = mutableListOf()
    var savedArticles: MutableList<String> = mutableListOf()
    constructor()
    constructor(image: String?, name: String?, email: String?, password: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }

    constructor(name: String?, email: String?, password: String?, followers: MutableList<String>, following: MutableList<String>) {
        this.name = name
        this.email = email
        this.password = password
        this.followers = followers
        this.following = following
    }


}