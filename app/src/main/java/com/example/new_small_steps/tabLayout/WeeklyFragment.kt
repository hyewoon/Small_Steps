package com.example.new_small_steps.tabLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.new_small_steps.R
import com.example.new_small_steps.databinding.FragmentWeeklyBinding


class WeeklyFragment : Fragment() {
    private lateinit var binding:FragmentWeeklyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentWeeklyBinding.inflate(layoutInflater)
        return binding.root
    }


}