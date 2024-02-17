package com.example.myapplication.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity
data class MyData(
    @PrimaryKey val date : LocalDate,
    val steps : Int

){

}
