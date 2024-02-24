package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentTargetBinding

class TargetFragment : Fragment() , View.OnClickListener{
    private lateinit var binding: FragmentTargetBinding
    private val viewModel: SharedVIewModel by activityViewModels()


    private var mytarget : Int = 100
    override fun onStop() {
        super.onStop()

        val onStop= MyApplication.prefs.getString("target",0)
        binding.target.text = onStop
        //save
        MyApplication.prefs.setString("target", onStop.toInt())
        Log.d("TargetFragment", "onStop :" + onStop)
    }

    override fun onPause() {
        super.onPause()
        val opPause = MyApplication.prefs.getString("target", 0)
        binding.target.text = opPause

        Log.d("TargetFragment", "onPauseget :" + opPause)
        //svae
       MyApplication.prefs.setString("target", opPause.toInt() )

    }

    override fun onResume() {
        super.onResume()
        val onResume= MyApplication.prefs.getString("target", 0)
        binding.target.text = onResume
        //save
        MyApplication.prefs.setString("target", onResume.toInt())

        Log.d("TargetFragment", "onResume :" + onResume)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       // binding.target.text = mytarget.toString()
        binding = FragmentTargetBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      val onView = MyApplication.prefs.getString("target", 0)
        binding.target.text = onView
        viewModel.setTarget(mytarget)
        Log.d("TargetFragment", "onViewCreate:" + onView.toString())
        //save
        MyApplication.prefs.setString("target", onView.toInt())


        setOnClickListener()

    }
    private  fun setOnClickListener(){
        val btnSequence = binding.container.children
        btnSequence.forEach { btn ->
            btn.setOnClickListener(this)

        }
    }

    override fun onClick(v: View?) {
       mytarget = MyApplication.prefs.getString("target", 0).toInt()
        binding.target.text = mytarget.toString()
        if (v != null) {
            when(v.id){
                R.id.plus -> plus()

                R.id.minus -> minus()
            }
            Log.d("TargetFragment", "clicked :" + mytarget)
            binding.target.text = mytarget.toString()
            //viewModel에 값 저장

            MyApplication.prefs.setString("target",mytarget )





        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun plus()  {
        if (mytarget < 100) {
            mytarget = 100
            binding.target.text = mytarget.toString()
        } else if (mytarget < 1000) {
            this.mytarget += 100
            binding.target.text = mytarget.toString()

        } else if (mytarget < 60000) {
            this.mytarget += 500
            binding.target.text = mytarget.toString()
        }else if(mytarget <= 6000)
            mytarget = 6000
            binding.target.text = mytarget.toString()
    }

    fun minus() {
         if(mytarget <= 100){
             mytarget = 100
            binding.target.text = mytarget.toString()
         }else if(mytarget < 1000){
             this.mytarget -= 100
             binding.target.text = mytarget.toString()
        }else if(mytarget <= 60000) {
             this.mytarget -= 500
             binding.target.text = mytarget.toString()
         }
    }

    companion object {
        private const val TAG = "TargetFragment"
        fun instance() = TargetFragment()
    }


  }



