package com.klab.upright.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_analysis.*


class AnalysisFragment : Fragment() {



    var tabTitle = arrayListOf<String>("현재 상태","생활 패턴 분석")
    lateinit var adapter: ViewPagerAdapter



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_analysis, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {

        //init viewpager
        val pagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager,2)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        //tablayout
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("현재 상태")))
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("생활 패턴 분석")))
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.setCurrentItem(tab!!.position)
            }
        })

    }

    private fun createTabView(tabName: String): View? {
        val tabView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val txt_name = tabView.findViewById<View>(R.id.tab_text) as TextView
        txt_name.text = tabName
        return tabView
    }
}