package com.example.myapplication.tablLayout

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.util.LocaleData
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.myapplication.MyApplication
import com.example.myapplication.MyWorkManager
import com.example.myapplication.SharedVIewModel
import com.example.myapplication.databinding.FragmentDailyBinding
import java.time.LocalDate
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DailyFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentDailyBinding
    private val viewModel: SharedVIewModel by activityViewModels()
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private val ACTIVITY_RECOGNITION_REQ_CODE = 100 // Request code for activity recognition


    //걸음 수 측정
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
        saveData()
        Log.d("DailyFragment", "onCreate " + initialStepCount.toString())
        Log.d("DailyFragment", "onCreate " + initialStepCount.toString())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater)
        val date = LocalDate.now().toString()

        binding.date.text = date
        //viewModeldml 값 가져오기
        binding.targetSteps.text = MyApplication.prefs.getString("target", 0)


     /*  viewModel.getTarget().observe(viewLifecycleOwner, Observer {
           binding.targetSteps.text = it.toString()
        })*/




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


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        loadData()
        saveData()
        // Register the sensor listener
        stepCounterSensor?.also { stepCounter ->
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)

            Log.d("DailyFragment", "onResume " + initialStepCount.toString())
            Log.d("DailyFragment", "onResume " + initialStepCount.toString())

        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener to prevent battery drain
        loadData()
        saveData()
        sensorManager.unregisterListener(this)

        Log.d("DailyFragment", "onPause " + initialStepCount.toString())
        Log.d("DailyFragment", "onPause " + initialStepCount.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop() {
        super.onStop()
        loadData()
        saveData()
        Log.d("DailyFragment", "onStop " + initialStepCount.toString())
        Log.d("DailyFragment", "onStop " + initialStepCount.toString())
    }


    //실제 데이터 받는 부분
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                if(initialStepCount == null){
                    initialStepCount = it.values[0].toInt()
                }
                //load
                Log.d("DailyFragment", "firstLoad " + currentSessionSteps)
                currentSessionSteps = it.values[0].toInt() - (initialStepCount ?: 0 )
                 currentSessionSteps = currentSessionSteps - 2374
                binding.steps.text = currentSessionSteps.toString()
                binding.progressBar.progress = currentSessionSteps
                Log.d("DailyFragment", "second " + currentSessionSteps)
                Log.d("DailyFragment", ("second " + initialStepCount ?: 0) as String)
                //save
                MyApplication.prefs.setString("currentSteps",currentSessionSteps )



                //데이터 가져오기
               /* viewModel.getSteps().observe(viewLifecycleOwner, Observer {
                    binding.steps.text = it.toString()
                })*/
               // binding.steps.text = MyApplication.prefs.getString("currentSteps", 0)
                saveData()
                Log.d("DailyFragment", "saveData " + initialStepCount.toString())
                Log.d("DailyFragment", "saveData " + currentSessionSteps)
                loadData()
                Log.d("DailyFragment", "loadData " + initialStepCount.toString())
                Log.d("DailyFragment", "loadData " + currentSessionSteps)


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveData(){

        val currentDate = LocalDate.now().toString()
        MyApplication.prefs.apply {
            setString("InitialStepCountI", initialStepCount ?: 0)
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


    // A method to reset the session steps to 0
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetSessionSteps() {
        // Reset initialStepCount to null so it will be set again with the next sensor event
        initialStepCount = null
        currentSessionSteps = 0
        //save
        MyApplication.prefs.setString("currentSteps",currentSessionSteps)
        binding.steps.text = MyApplication.prefs.getString("currentSteps", 0)


        saveData()

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