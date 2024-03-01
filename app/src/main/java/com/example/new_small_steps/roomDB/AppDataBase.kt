package com.example.new_small_steps.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [MyData::class], version = 1, exportSchema = false)
abstract class AppDataBase  : RoomDatabase(){
    // DAO 인터페이스를 지정
    abstract val myDataDao : MyDataDao

    companion object{
        @Volatile
        private var INSTANCE : AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "mydata"
                    ).build()
                    INSTANCE = instance
                }
                return instance

            }
        }

    }



}