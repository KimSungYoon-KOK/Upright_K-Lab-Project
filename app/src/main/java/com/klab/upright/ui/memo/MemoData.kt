package com.klab.upright.ui.memo

import java.io.Serializable
import java.util.*

data class MemoData(val date: Calendar, val time:String, val type:String, val pain:String, val content:String) : Serializable{

}