package com.example.new_small_steps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.new_small_steps.databinding.FragmentHomeBinding
import com.example.new_small_steps.tabLayout.ViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager2Adapter : ViewPager2Adapter
    private lateinit var viewPager2 : ViewPager2
    private lateinit var tabLayout : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    //onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = binding.viewPager2
        tabLayout = binding.tabLayout
        viewPager2Adapter = ViewPager2Adapter(this)
        viewPager2.adapter = viewPager2Adapter

        // TabLayout attach  to link the TabLayout to the ViewPager2
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when(position) {
                0 -> tab.text = "일"
               /* 1 -> tab.text = "주"
                2 -> tab.text = "월"*/
            }
        }.attach()
    }

}