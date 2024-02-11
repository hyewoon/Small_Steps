package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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