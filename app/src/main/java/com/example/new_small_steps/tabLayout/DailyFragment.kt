package com.example.new_small_steps.tabLayout

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.new_small_steps.MyApplication
import com.example.new_small_steps.MyWorkManager
import com.example.new_small_steps.R
import com.example.new_small_steps.databinding.FragmentDailyBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit


class DailyFragment : Fragment(),SensorEventListener {
    private lateinit var binding: FragmentDailyBinding
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null

    private val ACTIVITY_RECOGNITION_REQ_CODE = 100 // Request code for activity recognition

    private var initialStepCount: Int? = null // Holds the initial step count
    private var currentSessionSteps: Int = 0 // Steps taken in the current session
    private var steps : Int = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermission()
        loadData()
        saveData()

        //fragmentdptj sensor 사용하려면 앞에 requireActivity() 붙여야
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Check if the sensor is available
        if (stepCounterSensor == null) {
            Toast.makeText(
                requireContext(),
                "Step counter sensor not available!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDailyBinding.inflate(layoutInflater)
        val date = LocalDate.now().toString()

        binding.date.text = date
        binding.targetSteps.text = MyApplication.prefs.getString("target", 0)
        binding.progressBar.max =  MyApplication.prefs.getString("target", 0).toString().toInt()
        return binding.root
    }

    private fun checkAndRequestPermission() {
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                requestPermissions(
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    ACTIVITY_RECOGNITION_REQ_CODE
                )
            } else {
                // Permission already granted, you can proceed with your feature that requires this permission
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop() {
        super.onStop()
        //loadData
       currentSessionSteps = MyApplication.prefs.getString("currentSteps", 0).toString().toInt()
        //변경사항 또 저장
        MyApplication.prefs.setString("currentSteps",currentSessionSteps )

        binding.steps.text = currentSessionSteps.toString()
        Log.d("DailyFragment", "onStop current$currentSessionSteps")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        loadData()
        saveData()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        stepCounterSensor?.also { step ->
            sensorManager.registerListener(this, step, SensorManager.SENSOR_DELAY_UI)
            loadData()
            saveData()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if(initialStepCount == null){
                    initialStepCount = it.values[0].toInt()
                }

                // Calculate steps taken since initialStepCount was recorded.
                currentSessionSteps = it.values[0].toInt() - (initialStepCount ?: 0)
                Log.d("DailyFragment", "initialStepCount: $initialStepCount")
                Log.d("DailyFragment", "currentSessionSteps: $currentSessionSteps")

                binding.steps.text = currentSessionSteps.toString()
                binding.progressBar.progress = currentSessionSteps
                //saveData
                MyApplication.prefs.setString("currentSteps",currentSessionSteps )
                Log.d("DailyFragment", "onSensorChanged$currentSessionSteps")
                saveData()
                loadData()

                var totalSteps = 0
                if(steps == 0){
                    totalSteps = currentSessionSteps + steps
                    steps = currentSessionSteps
                }else{
                    totalSteps = currentSessionSteps + steps
                }


                //데이터 보내기
                val sendData : Data = workDataOf(
                    "steps" to currentSessionSteps,
                    "totalSteps" to totalSteps
                )
                workManager(sendData)



            }
    }

}

override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

}


@RequiresApi(Build.VERSION_CODES.O)
    private fun saveData(){
        val currentDate = LocalDate.now().toString()
        MyApplication.prefs.apply {
            setString("InitialStepCount", initialStepCount ?: 0)
            setDate("LastDate", currentDate)
            //  Log.d("DailyFragment", "currentDate :" + currentDate )

        }

    /*val sharedPreferences = requireContext().getSharedPreferences("StepCounterPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("InitialStepCount", initialStepCount ?: 0)
        editor.apply()*/

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    private fun loadData() {
        val getInt = MyApplication.prefs.getString("InitialStepCount", 0).toString().toInt()
        initialStepCount =getInt
        if (initialStepCount == 0) initialStepCount = null

        val lastDate = MyApplication.prefs.getDate("LastDate", LocalDate.now().toString())
        val currentDate = LocalDate.now().toString()
        //  Log.d("DailyFragment", "currentDate: "+ currentDate + "lastDate :" + lastDate )

        if (lastDate != currentDate) {
            // It's a new day
            resetSessionSteps()
        }

    /*
             val sharedPreferences = requireContext().getSharedPreferences("StepCounterPrefs", Context.MODE_PRIVATE)
             initialStepCount = sharedPreferences.getInt("InitialStepCount", 0)
             if (initialStepCount == 0) initialStepCount = null // Adjust based on your logic*/

    }


   //  A method to reset the session steps to 0
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetSessionSteps() {
        // Reset initialStepCount to null so it will be set again with the next sensor event
        initialStepCount = null
        currentSessionSteps = 0
        //save
        MyApplication.prefs.setString("currentSteps",currentSessionSteps)
      //  binding.steps.text = MyApplication.prefs.getString("currentSteps", 0)

        saveData()

    }


    private fun workManager(data: Data) {

        val workManager = PeriodicWorkRequestBuilder<MyWorkManager>(1, TimeUnit.DAYS) //하루에 한번 실행
            .setInputData(data) //데이터 보내기
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()


        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "mywork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workManager
        )
    }

    }

    private fun calculateInitialDelay(): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 0)
        val scheduledTime = calendar.timeInMillis

        // 이미 23시 59분을 지났다면 다음 날로 설정합니다.
        if (scheduledTime < now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis - now
    }

