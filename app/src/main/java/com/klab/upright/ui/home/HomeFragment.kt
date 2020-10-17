package com.klab.upright.ui.home

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.klab.upright.MyApplication
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_pressure.view.*

class HomeFragment : Fragment() {

    lateinit var shape: GradientDrawable
    lateinit var centerShape: GradientDrawable
    var bt: BluetoothSPP? = null
    var pressureLine = 6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bt = (activity?.application as MyApplication).bt
        init()
    }


    private fun init() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        )
        params.setMargins(20, 0, 20, 40)
        val params2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        )
        params2.setMargins(20, 0, 20, 0)

        shape = GradientDrawable()
        shape.setColor(ContextCompat.getColor(requireContext(), R.color.white))
        shape.setStroke(3, ContextCompat.getColor(requireContext(), R.color.white))
        shape.cornerRadius = 30f

        centerShape = GradientDrawable()
        centerShape.setColor(ContextCompat.getColor(requireContext(), R.color.white))
        centerShape.setStroke(10, ContextCompat.getColor(requireContext(), R.color.middle_grey))
        centerShape.cornerRadius = 25f



        makeCenter()
    }

    private fun makeCenter() {
        //centerLayout.weightSum = (pressureLine * 10 + ((pressureLine*2)*(pressureLine*2+1))/2).toFloat()
        for (i in 1..pressureLine * 2) {
            val v = View(requireContext())
            val centerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
            centerParams.bottomMargin = 3
            centerParams.width = (60 + i * 5)
            v.layoutParams = centerParams
            v.background = centerShape
        }
    }


}

