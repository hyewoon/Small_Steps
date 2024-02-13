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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDailyBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationResult.create
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentDailyBinding
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private val ACTIVITY_RECOGNITION_REQ_CODE = 100 // Request code for activity recognition
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    //걸음 수 측정
    var steps: Int? = 0

    //이전 거리
    var lastLocation: Location? = null

    //누적 이동 거리
    var total_distance: Float? = 0.0f

    //누적 이동 시간
    var total_time: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermission()
        requestLocationPermission()

        // Initialize the SensorManager and Step Counter Sensor
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater)
        return binding.root


    }

    override fun onResume() {
        super.onResume()
        // Register the sensor listener
        stepCounterSensor?.also { stepCounter ->
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)
        }
        requestLocationUpdates()


    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener to prevent battery drain
        sensorManager.unregisterListener(this)
        requestLocationUpdates()
    }

    //실제 데이터 받는 부분
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                // Update your UI here with the new step count
                val steps = it.values[0].toInt()

                // Use 'steps' variable to update your UI
                binding.steps.text = steps.toString()
                binding.progressBar.progress = steps



            }
        }


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

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


    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            //requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 10000 // Update interval in milliseconds
            fastestInterval = 5000 // Fastest update interval in milliseconds
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper() // Looper can be provided for custom threading
        )
    }

    //위치정보 update받는 부분
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            //newLocoation = 현재 위치
            val newLocation = locationResult.lastLocation

            //현재시간
            val timestamp = newLocation.time

            if (lastLocation != null) {
                //두 지점 간의 거리 측정 단위 : meter
                val distance = lastLocation?.distanceTo(newLocation)
                val lastTime = lastLocation!!.time
                var time = (timestamp - lastTime!!) / 1000

                if(lastLocation == newLocation){ //거리 변화가 없으면
                    time = 0 //시간은 변화없게 하고
                }
                total_time = time + time

                if (distance != null) {
                    total_distance = total_distance?.plus(distance)
                }


                Log.d("changed", distance.toString() + lastTime.toString())

                /* binding.distance.text= "이동거리"+ String.format("%.2f", distance)+ "m"
                binding.lastTime.text = "이전 시간 : ${beforeTime}"
                binding.currentTime.text = "현재 시간 : ${nowTime}"
                binding.duration.text = "이동 시간 : ${time} 초 "
*/


                // 변환
                var sec = total_time % 60
                var min = total_time / 60 % 60
                var hour = total_time / 3600

                //미터를  킬로미터로 변환
                var distanceKm = total_distance?.div(1000)

                binding.distance.text = "전체이동거리" + String.format("%.3f", distanceKm) + "km"
                binding.time.text = "전체이동시간 : ${hour}시 + ${min}분 + ${sec}초"
            }
            lastLocation = newLocation
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACTIVITY_RECOGNITION_REQ_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, proceed with the activity recognition
                } else {
                    // Permission denied, you can disable the functionality that depends on this permission or inform the user
                }
                return
            }
            // Handle other permission requests by checking other request codes
        }
    }



}