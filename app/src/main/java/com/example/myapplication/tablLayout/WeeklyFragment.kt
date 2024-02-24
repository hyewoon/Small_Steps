package com.example.myapplication.tablLayout

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.SharedVIewModel
import com.example.myapplication.databinding.FragmentWeeklyBinding
import com.example.myapplication.roomDB.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


class WeeklyFragment : Fragment() {
 private lateinit var binding : FragmentWeeklyBinding
 private val viewModel : SharedVIewModel by activityViewModels()
 private lateinit var db : AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWeeklyBinding.inflate(inflater)
        viewModel.getSteps().observe(viewLifecycleOwner, Observer {
            binding.totalSteps.text = it.toString()
        })

        //AppDataBase.getInstance(requireActivity()).myDataDao.weeklyData()
        db = AppDataBase.getInstance(this.requireContext())
        val date = LocalDate.now()

        //데이터 불러오기
        CoroutineScope(Dispatchers.IO).launch {
            val result = db.myDataDao.weeklyData(date)
            binding.apply {

                totalSteps.text = result[0].totalSteps.toString()

                proText1.text = result[0].steps.toString()
                progressBar1.progress = result[0].steps.toInt()
                 proText2.text = result[1].steps.toString()
                 progressBar2.progress = result[1].steps.toInt()
                 /*oText3.text = result.get(2).steps.toString()
                 progressBar3.progress = result.get(2).steps.toInt()
                 proText4.text = result.get(2).steps.toString()
                 progressBar4.progress = result.get(2).steps.toInt()
                 proText5.text = result.get(3).steps.toString()
                 progressBar5.progress = result.get(3).steps.toInt()
                 proText6.text = result.get(4).steps.toString()
                 progressBar6.progress = result.get(4).steps.toInt()
                 proText7.text = result.get(5).steps.toString()
                 progressBar7.progress = result.get(5).steps.toInt()*/
            }
        }
        return binding.root
    }
}