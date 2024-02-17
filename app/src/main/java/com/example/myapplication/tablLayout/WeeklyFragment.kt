package com.example.myapplication.tablLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentWeeklyBinding


class WeeklyFragment : Fragment() {
 private lateinit var binding : FragmentWeeklyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       binding = FragmentWeeklyBinding.inflate(inflater)
        return binding.root

    }


}