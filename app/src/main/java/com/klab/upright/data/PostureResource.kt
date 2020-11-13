package com.klab.upright.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.klab.upright.R

class PostureResource(val context: Context){

    var instructList = arrayListOf(
        "instruct1",
        "instruct2",
        "instruct3",
        "instruct4",
        "instruct5",
        "instruct6",
        "instruct7",
        "instruct8")

    fun getPostureData(index:Int, isUpDown:Boolean):Data{
        if(isUpDown){
            when(index){
                0->{
                    return Data(R.color.sit_color1, ContextCompat.getDrawable(context,R.drawable.sit1),instructList[0])
                }
                1->{
                    return Data(R.color.sit_color2, ContextCompat.getDrawable(context,R.drawable.sit2),instructList[1])
                }
                2->{
                    return Data(R.color.sit_color3, ContextCompat.getDrawable(context,R.drawable.sit3),instructList[2])
                }
                3->{
                    return Data(R.color.sit_color2, ContextCompat.getDrawable(context,R.drawable.sit4),instructList[3])
                }
                else->{
                    return Data(R.color.sit_color1, ContextCompat.getDrawable(context,R.drawable.sit5),instructList[4])
                }
            }
        }else{
            when(index){
                0->{
                    return Data(R.color.sit_color1, ContextCompat.getDrawable(context,R.drawable.left),instructList[5])
                }
                1->{
                    return Data(R.color.sit_color3, ContextCompat.getDrawable(context,R.drawable.good),instructList[6])
                }
                else->{
                    return Data(R.color.sit_color1, ContextCompat.getDrawable(context,R.drawable.right),instructList[7])
                }
            }
        }
    }
}

data class Data(
    var color:Int,
    var drawable: Drawable?,
    var instruction:String
){

}