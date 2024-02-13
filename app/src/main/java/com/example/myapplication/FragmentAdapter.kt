package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.tablLayout.DailyFragment
import com.example.myapplication.tablLayout.MonthlyFragment
import com.example.myapplication.tablLayout.WeeklyFragment

/*
adapter
1.  getItemCount() : 총 몇개의 화면을 지정할지
2.
* */
class FragmentAdapter(fragment : Fragment)  : FragmentStateAdapter(fragment){


  //  var fragments : ArrayList<Fragment> = arrayListOf()
    override fun getItemCount(): Int = 3


    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> DailyFragment()
            1 -> WeeklyFragment()
            else -> MonthlyFragment()
        }

    }


   /* fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size-1)
    }

    fun removeFragment() {
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }*/

}