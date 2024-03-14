package com.example.new_small_steps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.new_small_steps.databinding.FragmentResultBinding
import com.example.new_small_steps.roomDB.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private lateinit var db : AppDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(layoutInflater)
        db = AppDataBase.getInstance(this.requireActivity())

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

        CoroutineScope(Dispatchers.IO).launch {
            binding.weeklySteps.text = db.myDataDao.getAllData()[8].total_steps.toString()
            binding.seekbarFri.progress =db.myDataDao.getAllData()[8].total_steps.toString().toInt()

        }


        return binding.root
    }


}