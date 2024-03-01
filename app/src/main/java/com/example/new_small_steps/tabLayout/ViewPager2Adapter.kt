package com.example.new_small_steps.tabLayout

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(fragment: Fragment) :FragmentStateAdapter(fragment){
    override fun getItemCount(): Int  = 1

    override fun createFragment(position: Int): Fragment {
        /*  return when(position){
           0 -> DailyFragment()
           1 -> WeeklyFragment()
           else ->MonthlyFragment()
       }*/

        return DailyFragment()
    }
}