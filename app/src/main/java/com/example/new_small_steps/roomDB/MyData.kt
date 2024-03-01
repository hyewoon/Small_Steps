package com.example.new_small_steps.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class MyData(
    @PrimaryKey val date : LocalDateTime,
    val total_steps : Int,
    val steps : Int
){

}
