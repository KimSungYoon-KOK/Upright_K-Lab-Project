package com.klab.upright.ui.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klab.upright.R
import java.util.*
import kotlin.collections.ArrayList

class MemoAdapter(val context: Context, val memoList:ArrayList<MemoData>) : RecyclerView.Adapter<MemoAdapter.ViewHolder>(){

    val months=arrayOf("January", "February", "March","April","May","June","July","August","September","October","November","December")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_write,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    override fun onBindViewHolder(holder: MemoAdapter.ViewHolder, position: Int) {
        val writeData = memoList[position]
        val date = writeData.date

        holder.date.text = months[(date.get(Calendar.MONTH)+1)] +" "+date.get(Calendar.DATE).toString()+", "+date.get(Calendar.YEAR).toString()
        holder.time.text = writeData.time
        holder.type.text = writeData.type
        holder.pain.text = writeData.pain
        holder.content.text = writeData.content

        holder.titleView.setOnClickListener {
            if(holder.detailView.visibility == GONE)
                holder.detailView.visibility = VISIBLE
            else{
                holder.detailView.visibility = GONE
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var date:TextView
        var time:TextView
        var type:TextView
        var pain:TextView
        var content:TextView
        var titleView:LinearLayout
        var detailView:LinearLayout

        init{
            date = itemView.findViewById(R.id.dateText)
            time = itemView.findViewById(R.id.timeText)
            type = itemView.findViewById(R.id.typeText)
            pain = itemView.findViewById(R.id.painText)
            content = itemView.findViewById(R.id.contentText)
            titleView =itemView.findViewById(R.id.titleView)
            detailView =itemView.findViewById(R.id.detailView)

        }

    }


}