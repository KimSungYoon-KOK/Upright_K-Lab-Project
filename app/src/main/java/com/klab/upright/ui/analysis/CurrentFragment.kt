package com.klab.upright.ui.analysis

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener
import com.klab.upright.MyApplication
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_current.*
import kotlinx.android.synthetic.main.item_pressure.view.*

class CurrentFragment : Fragment() {

    lateinit var shape:GradientDrawable
    lateinit var centerShape:GradientDrawable
    var bt : BluetoothSPP? =null
    var pressureLine = 6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bt = (activity?.application as MyApplication).bt
        init()
        initPressure()
    }

    private fun initPressure() {
        //test code
        for(i in 0..3){
            changeState(2+i*6, 150)
            changeState(3+i*6, 150)

        }
        //
        bt?.setOnDataReceivedListener(object : OnDataReceivedListener {
            //데이터 수신
            override fun onDataReceived(data: ByteArray, message: String) {
                //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                for(i in 0..3){
                    changeState(2+i*6, message.toInt())
                }
                println(message)
            }
        })
    }

    private fun init() {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f)
        params.setMargins(20,0,20,40)
        val params2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f)
        params2.setMargins(20,0,20,0)

        shape = GradientDrawable()
        shape.setColor(ContextCompat.getColor(requireContext(),R.color.white))
        shape.setStroke(3,ContextCompat.getColor(requireContext(),R.color.white))
        shape.cornerRadius = 30f

        centerShape = GradientDrawable()
        centerShape.setColor(ContextCompat.getColor(requireContext(),R.color.white))
        centerShape.setStroke(10,ContextCompat.getColor(requireContext(),R.color.black))
        centerShape.cornerRadius = 25f

        for(i in 0..pressureLine){
            val v = LayoutInflater.from(context).inflate(R.layout.item_pressure,linearLayout_left1,false)
            if(i == pressureLine){
                v.item.layoutParams = params2
            }else{
                v.item.layoutParams = params
            }
            v.item.background = makeShape(R.color.level1_massage)
            //v.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
            linearLayout_left1.addView(v)
        }

        for(i in 0..pressureLine){
            val v = LayoutInflater.from(context).inflate(R.layout.item_pressure,linearLayout_left2,false)
            if(i == pressureLine){
                v.item.layoutParams = params2
            }else{
                v.item.layoutParams = params
            }
            v.item.background = makeShape(R.color.level1_massage)
            //v.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
            linearLayout_left2.addView(v)
        }

        for(i in 0..pressureLine){
            val v = LayoutInflater.from(context).inflate(R.layout.item_pressure,linearLayout_right1,false)
            if(i == pressureLine){
                v.item.layoutParams = params2
            }else{
                v.item.layoutParams = params
            }
            v.item.background = makeShape(R.color.level1_massage)
            //v.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
            linearLayout_right1.addView(v)
        }

        for(i in 0..pressureLine){
            val v = LayoutInflater.from(context).inflate(R.layout.item_pressure,linearLayout_right2,false)
            if(i == pressureLine){
                v.item.layoutParams = params2
            }else{
                v.item.layoutParams = params
            }
            v.item.background = makeShape(R.color.level1_massage)
            //v.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
            linearLayout_right2.addView(v)
        }

        makeCenter()
    }

    private fun makeCenter() {
        //centerLayout.weightSum = (pressureLine * 10 + ((pressureLine*2)*(pressureLine*2+1))/2).toFloat()
        for(i in 1..pressureLine*2){
            val v = View(requireContext())
            val centerParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f)
            centerParams.bottomMargin = 3
            centerParams.width = (60+i*5)
            v.layoutParams = centerParams
            v.background = centerShape
            centerLayout.addView(v)
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
            1,2,3,4,5,6->linearLayout_left1.getChildAt(index-1)
            7,8,9,10,11,12->linearLayout_left2.getChildAt(index-7)
            13,14,15,16,17,18->linearLayout_right1.getChildAt(index-13)
            19,20,21,22,23,24->linearLayout_right2.getChildAt(index-19)
            else->null
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

        changeView?.item?.background = makeShape(changeColor)
    }

}