package com.example.myapplication.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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

}