package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.MyApplication.Companion.prefs

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}



class PreferenceUtil(applicationContext: Context) {
    private val pref : SharedPreferences = applicationContext.getSharedPreferences("nowSteps", Context.MODE_PRIVATE)

    //가져오기 read
    fun getString(key: String, defValue: Int){
      pref.getInt(key,defValue)
    }
    //저장하기 write
    fun setString(key: String, str: Int) {
       with(pref.edit()) {
          putInt(key, str)
           apply()
       }

    }

}


