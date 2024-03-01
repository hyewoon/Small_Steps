package com.example.new_small_steps.roomDB

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

@Dao
interface MyDataDao {
    @Insert
    fun insert(myData:MyData)
    @Update
    fun update(myData : MyData)
    @Delete
    fun delete(myData: MyData)
    @Query("SELECT * FROM mydata")
    fun getAllData(): List<MyData>
    @Query("SELECT * FROM MyData WHERE date >= Date('now' - 7) AND Date('now' + 1)")
    fun getWeeklyData() : List<MyData>

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfWeek(date: LocalDateTime): String {
        return date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
    }

}