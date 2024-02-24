package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedVIewModel : ViewModel(){
    private val target = MutableLiveData<Int>()
    private val steps = MutableLiveData<Int>()

    fun setTarget(myTarget : Int){
        target.value = myTarget
    }

    fun getTarget() : LiveData<Int> = target

    fun setSteps(mySteps : Int){
        steps.value = mySteps
    }

    fun getSteps() : LiveData<Int> = steps

}
