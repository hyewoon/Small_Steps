package com.example.new_small_steps

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.new_small_steps.databinding.FragmentTargetBinding

class TargetFragment : Fragment() , View.OnClickListener{
    private lateinit var binding: FragmentTargetBinding
    private var myTarget = 100

    override fun onResume() {
        super.onResume()
        myTarget = loadData()
        binding.target.text = myTarget.toString()
        saveData()
    }

    override fun onStop() {
        super.onStop()
        myTarget = loadData()
        binding.target.text = myTarget.toString()
        saveData()
    }

    override fun onPause() {
        super.onPause()
        myTarget = loadData()
        binding.target.text = myTarget.toString()
        saveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTargetBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        val btnSequence = binding.container.children
        btnSequence.forEach { btn ->
            btn.setOnClickListener(this)

        }
    }


    override fun onClick(v: View?) {
        myTarget = loadData()
        binding.target.text = myTarget.toString()
        if (v != null) {
            when(v.id){
                R.id.plus -> plus()

                R.id.minus -> minus()
            }
            Log.d("TargetFragment", "clicked :" + myTarget)
            binding.target.text = myTarget.toString()
            //이 값이 DailyFragment에서 연결되는
           saveData()

        }
    }

    private fun plus(){
        if (myTarget < 100) {
            myTarget = 100
            binding.target.text = myTarget.toString()
        } else if (myTarget < 1000) {
            this.myTarget += 100
            binding.target.text = myTarget.toString()

        } else if (myTarget < 60000) {
            this.myTarget += 500
            binding.target.text = myTarget.toString()
        }else if(myTarget <= 6000)
            myTarget = 6000
        binding.target.text = myTarget.toString()

    }

    private fun minus(){
        if(myTarget <= 100){
            myTarget = 100
            binding.target.text = myTarget.toString()
        }else if(myTarget < 1000){
            this.myTarget -= 100
            binding.target.text = myTarget.toString()
        }else if(myTarget <= 60000) {
            this.myTarget -= 500
            binding.target.text = myTarget.toString()
        }
    }

    private fun saveData(){
        MyApplication.prefs.setString("target",myTarget )
    }

    private fun loadData() : Int{
       return  MyApplication.prefs.getString("target", 0).toInt()
    }



}