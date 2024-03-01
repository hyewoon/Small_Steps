package com.example.new_small_steps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.new_small_steps.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(layoutInflater)

        val target = MyApplication.prefs.getString("target", 0).toString().toInt()
        binding.apply {
            seekbarSun.max = target
            seekbarMon.max = target
            seekbarTue.max = target
            seekbarWed.max = target
            seekbarThu.max = target
            seekbarFri.max = target
            seekbarSat.max = target
        }

        return binding.root
    }


}