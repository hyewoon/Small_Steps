package com.example.myapplication.tablLayout

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.renderscript.RenderScript
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.myapplication.MyApplication
import com.example.myapplication.MyWorkManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDailyBinding
import com.example.myapplication.roomDB.AppDataBase
import com.example.myapplication.roomDB.MyData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationResult.create
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class DailyFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentDailyBinding
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private val ACTIVITY_RECOGNITION_REQ_CODE = 100 // Request code for activity recognition


    //걸음 수 측정
    var steps: Int = 0
    var totalSteps : Int = 0
    private var initialStepCount: Int? = null // Holds the initial step count
    private var currentSessionSteps: Int = 0 // Steps taken in the current session



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermission()
        loadData()


        // Initialize the SensorManager and Step Counter Sensor
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater)




 /*  /    val workManagerA = OneTimeWorkRequestBuilder<MyWorkManager>().build()
       WorkManager.getInstance(this.requireActivity()).getWorkInfoByIdLiveData(MyWorkManager.id)
           .observe(this.requireActivity()){ info ->
               if (info != null && info.state.isFinished) {

                   val result = info.outputData.getInt("results", 1000)

               }

           }
*/
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //loadData()


    }

    override fun onResume() {
        super.onResume()
        // Register the sensor listener
        stepCounterSensor?.also { stepCounter ->
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)
            loadData()


        }


    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener to prevent battery drain
        sensorManager.unregisterListener(this)
        saveData()
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }


    //실제 데이터 받는 부분
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if(initialStepCount == null){
                    initialStepCount = it.values[0].toInt()

                }
                loadData()
                // Calculate steps taken since initialStepCount was recorded.
                currentSessionSteps = it.values[0].toInt() - (initialStepCount ?: 0) // 이 값은 저장되지 않는다.
                saveData()
                Log.d("DailyFragment", "saveI " + initialStepCount.toString())
                Log.d("DailyFragment", "saveC" + currentSessionSteps.toString())
                loadData()
                Log.d("DailyFragment", "loadI " + initialStepCount.toString())
                Log.d("DailyFragment", "loadC " + currentSessionSteps.toString())
                binding.steps.text = currentSessionSteps.toString()


                val sendData : Data = workDataOf(
                    "steps" to currentSessionSteps,
                    "totalSteps" to initialStepCount
                )
                workManager(sendData)

                }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun saveData(){
        MyApplication.prefs.setString("InitialStepCount", (initialStepCount ?: 0))
        MyApplication.prefs.setString("CurrentSessionSteps", currentSessionSteps)
    }
    fun loadData() {
        val loadInitial =
            MyApplication.prefs.getString("InitialStepCount", 0).toString().toIntOrNull()
        val loadCurrent =
            MyApplication.prefs.getString("CurrentSessionSteps", 0).toString().toIntOrNull()
        initialStepCount = loadInitial
          loadCurrent?.let {
              currentSessionSteps = loadCurrent
          }



        // A method to reset the session steps to 0
        fun resetSessionSteps() {
            // Reset initialStepCount to null so it will be set again with the next sensor event
            initialStepCount = null
            currentSessionSteps = 0
            // Update your UI here to reflect the reset
        }
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

    private fun workManager(data : Data){

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


}