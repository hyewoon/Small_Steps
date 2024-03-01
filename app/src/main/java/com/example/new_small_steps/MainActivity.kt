package com.example.new_small_steps

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.new_small_steps.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val localDatTime = LocalDateTime.now()

        setBottomNavigationView()

        // 앱 초기 실행시 홈화면으로 실행
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.home
        }



    }
    private fun setBottomNavigationView(){

        binding.apply {
            bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit()
                        true
                    }
                    R.id.results ->{
                        supportFragmentManager.beginTransaction().replace(R.id.main_container, ResultFragment()).commit()
                        true
                    }
                    R.id.target ->{
                        supportFragmentManager.beginTransaction().replace(R.id.main_container, TargetFragment()).commit()
                        true

                    }

                    else -> false
                }
            }
        }

    }
}