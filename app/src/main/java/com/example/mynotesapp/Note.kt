package com.example.mynotesapp

class Note {

    var nodeID:Int?=null
    var title:String?=null
    var content:String?=null

    constructor(nodeID:Int,title:String,content:String){
        this.nodeID=nodeID
        this.title=title
        this.content=content
    }

}