package com.klab.upright.ui.analysis

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter (
    fm: FragmentManager, val tabCount:Int
): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0->{
                val tab1 = CurrentFragment()
                return tab1
            }
            else->{
                val tab2 = PatternFragment()
                return tab2
            }
        }
    }

    override fun getCount(): Int {
        return tabCount
    }


}