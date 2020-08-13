package com.example.tweeter_v1

data class Bird( var name:String, var image: Int, var wiki: String)

var birds : MutableList<VerifyClassification.DBWrite> =  mutableListOf(VerifyClassification.DBWrite("","","","",""))

var CurLoc: String = ""