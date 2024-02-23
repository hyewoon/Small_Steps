package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.myapplication.databinding.FragmentTargetBinding

class TargetFragment : Fragment() , View.OnClickListener{
    private lateinit var binding: FragmentTargetBinding
    var result : Int = 0
    override fun onStop() {
        super.onStop()

        val onStop= MyApplication.prefs.getString("target",0)
        binding.target.text = onStop
        MyApplication.prefs.setString("target", mytarget)
        Log.d("TargetFragment", "onStop :" + mytarget)
    }

    override fun onPause() {
        super.onPause()
        val opPause = MyApplication.prefs.getString("target", -1)
        binding.target.text = opPause

        Log.d("TargetFragment", "onPauseget :" + mytarget)
        binding.target.text = MyApplication.prefs.setString("target", mytarget ).toString()

    }

    override fun onResume() {
        super.onResume()
        MyApplication.prefs.setString("target", -1)
        binding.target.text = MyApplication.prefs.getString("target", mytarget)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTargetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val oncreate = MyApplication.prefs.getString("target",-1)
        result = binding.target.text.toString().toInt()

       MyApplication.prefs.setString("target", oncreate )
            Log.d("TargetFragment", "onViewCreated"+ mytarget)

        setOnClickListener()

    }
    private  fun setOnClickListener(){
        val btnSequence = binding.container.children
        btnSequence.forEach { btn ->
            btn.setOnClickListener(this)


        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.plus -> plus()

                R.id.minus -> minus()
            }

            Log.d("TargetFragment", "clicked :" + result)
            binding.target.text = result.toString()
            MyApplication.prefs.setString("target",result )
            //binding.target.text = MyApplication.prefs.getString("target", -1)

        }
    }
    fun plus()  {
        if (result < 100) {
            binding.target.text = result.toString()
        } else if (result < 1000) {
            this.result += 100
            binding.target.text = result.toString()

        } else if (result < 60000) {
            this.result += 500
            binding.target.text = result.toString()
        }
    }

    fun minus() {
         if(result <= 100){
             result = 100
            binding.target.text = result.toString()
         }else if(result < 1000){
             this.result -= 100
             binding.target.text = result.toString()
        }else if(result <= 60000) {
             this.result -= 500
             binding.target.text = result.toString()
         }
    }
    companion object {
        private const val TAG = "TargetFragment"
        fun instance() = TargetFragment()
    }


  }

