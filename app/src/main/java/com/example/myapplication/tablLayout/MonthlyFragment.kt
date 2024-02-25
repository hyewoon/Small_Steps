package com.example.myapplication.tablLayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.MyApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMonthlyBinding


class MonthlyFragment : Fragment() {
    private lateinit var binding : FragmentMonthlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding  = FragmentMonthlyBinding.inflate(inflater)
        binding.text.text = MyApplication.prefs.getString("currentSteps", 0).toString()
        binding.progress1.progress = MyApplication.prefs.getString("currentSteps", 0).toString().toInt()
        return binding.root
    }


}