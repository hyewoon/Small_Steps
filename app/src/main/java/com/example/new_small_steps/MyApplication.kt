package com.example.new_small_steps

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

//전역변수로 shqredpreference 이용하기 : currentSteps
class MyApplication  : Application(){

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
    //저장하기 write
    fun setString(key: String, str: Int) {
        with(pref.edit()) {
            putString(key, str.toString())
            apply()
        }
    }

    //가져오기 read
    fun getString(key: String, defValue: Int): String {
        return pref.getString(key, defValue.toString()).toString()
    }

    fun setDate(key: String, str: String) {
        with(pref.edit()) {
            putString(key, str)
            apply()
        }
    }

    fun getDate(key: String, defValue: String): String {
        return pref.getString(key, defValue).toString()
    }




}