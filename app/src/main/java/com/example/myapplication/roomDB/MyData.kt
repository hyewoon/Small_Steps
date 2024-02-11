package com.example.myapplication.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyData(
    @PrimaryKey val date : String,
    val steps : Int,
    val distance : Double,
    val time : Long
){

}
