package com.klab.upright.ui.analysis

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_current.*
import kotlinx.android.synthetic.main.item_pressure.view.*

class CurrentFragment : Fragment() {

    lateinit var shape:GradientDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initPressure()
    }

    private fun initPressure() {

    }

    private fun init() {
        var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f)
        params.setMargins(0,0,0,40)

        shape = GradientDrawable()
        shape.setColor(ContextCompat.getColor(requireContext(),R.color.white))
        shape.setStroke(3,ContextCompat.getColor(requireContext(),R.color.white))
        shape.cornerRadius = 30f

        for(i in 0..2){
            val v = LayoutInflater.from(context).inflate(R.layout.item_pressure,linearLayout_left,false)
            v.item.layoutParams = params
            v.item.background = makeShape(R.color.level1_massage)
            //v.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
            linearLayout_left.addView(v)
        }

        for(i in 0..2){
            val v = LayoutInflater.from(context).inflate(R.layout.item_pressure,linearLayout_right,false)
            v.item.layoutParams = params
            v.item.background = makeShape(R.color.level2_massage)
            //v.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
            linearLayout_right.addView(v)
        }
    }

    fun makeShape(id:Int):GradientDrawable{
        var shape = GradientDrawable()
        shape.setColor(ContextCompat.getColor(requireContext(),id))
        shape.setStroke(3,ContextCompat.getColor(requireContext(),id))
        shape.cornerRadius = 30f
        return shape
    }

    fun changeState(index:Int,pressure:Int){

        //변경할 뷰(1~6)
        val changeView = when(index){
            1->linearLayout_left.getChildAt(0)
            2->linearLayout_left.getChildAt(1)
            3->linearLayout_left.getChildAt(2)
            4->linearLayout_right.getChildAt(0)
            5->linearLayout_right.getChildAt(1)
            else->linearLayout_right.getChildAt(2)
        }

        //변경할 색
        var changeColor = 0
        if(pressure < 40)
            changeColor = R.color.level1_massage
        else if(pressure < 80)
            changeColor = R.color.level2_massage
        else if(pressure < 120)
            changeColor = R.color.level3_massage
        else if(pressure < 160)
            changeColor = R.color.level4_massage
        else
            changeColor = R.color.level5_massage

        changeView.item.background = makeShape(changeColor)
    }

}