package com.example.myapplication.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate

@Dao
interface MyDataDao {
    @Insert
    fun insert(myData : MyData)
    @Delete
    suspend fun delete(myData : MyData)
    @Update
    suspend fun update(myData : MyData)

    @Query("SELECT * FROM MyData ")
    fun getAllData(): List<MyData>

    @Query("SELECT* FROM MyData WHERE date BETWEEN :today-7 AND :today ORDER BY date DESC ")
    fun weeklyData(today : LocalDate): List<MyData>


}