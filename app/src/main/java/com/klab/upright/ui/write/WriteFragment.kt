package com.klab.upright.ui.write

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_pattern.*
import kotlinx.android.synthetic.main.fragment_write.*
import java.util.*

class WriteFragment : Fragment() {

    var writeList = arrayListOf<WriteData>()
    lateinit var adapter: MyWriteAdapter
    lateinit var writeDate:Calendar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_write, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        setExample()
        val layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        recyclerView_write.layoutManager = layoutManager
        adapter = MyWriteAdapter(requireContext(), writeList)
        recyclerView_write.adapter = adapter

        writeDate = Calendar.getInstance()
        dateText.text = writeDate.get(Calendar.YEAR).toString()+"년 "+
                (writeDate.get(Calendar.MONTH)+1).toString()+"월 "+writeDate.get(Calendar.DATE).toString()+"일"

        dateLayout.setOnClickListener {
            val datePickerListener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    writeDate.set(year,month,dayOfMonth)
                    dateText.text = year.toString()+"년 "+(month+1).toString()+"월 "+dayOfMonth.toString()+"일"
                }

            }

            var builder = DatePickerDialog(requireContext(),datePickerListener,writeDate.get(Calendar.YEAR),writeDate.get(Calendar.MONTH),writeDate.get(Calendar.DATE))
            builder.show()
        }
    }

    fun setExample(){
        writeList.add(WriteData(Calendar.getInstance(),"1시간 30분","달리기","아픔","오늘은 운동했다"))
        writeList.add(WriteData(Calendar.getInstance(),"2시간 30분","걷기","안아픔","오늘은 운동했다"))
    }

}