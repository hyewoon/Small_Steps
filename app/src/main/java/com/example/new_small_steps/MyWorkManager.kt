package com.example.new_small_steps

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.new_small_steps.roomDB.AppDataBase
import com.example.new_small_steps.roomDB.MyData
import java.time.LocalDateTime

class MyWorkManager(context : Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        //db연결
        val db = AppDataBase.getInstance(applicationContext)
        val date = LocalDateTime.now()

     //데이터 받가
        val steps : Int = inputData.getInt("steps", -1)
        val totalSteps : Int = inputData.getInt("totalSteps",-1)
        val myData : MyData = MyData(date!!, steps, totalSteps)

        //데이터 저장
        db.myDataDao.insert(myData)


        return Result.success()
    }
}