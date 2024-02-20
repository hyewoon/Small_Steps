package com.example.myapplication

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.myapplication.roomDB.AppDataBase
import com.example.myapplication.roomDB.MyData
import java.time.LocalDate
import java.util.Date
import java.util.UUID

class MyWorkManager(context : Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        //db연결
        val db :AppDataBase = AppDataBase.getInstance(this.applicationContext)

        val date = LocalDate.now()

        //데이터 받가
        val steps : Int = inputData.getInt("steps", -1)
        val totalSteps : Int = inputData.getInt("totalSteps",-1)
        val myData : MyData = MyData(date!!, steps, totalSteps)

        //데이터 저장
        db.myDataDao.insert(myData)

      //DailyFragment 데이터 보내기
        val results : Data = workDataOf(
            "results" to 0
        )

        return Result.success(results)


    }

}