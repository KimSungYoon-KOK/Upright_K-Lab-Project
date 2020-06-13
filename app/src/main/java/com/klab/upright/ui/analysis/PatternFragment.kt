package com.klab.upright.ui.analysis

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_pattern.*
import java.util.*

class PatternFragment : Fragment() {

    lateinit var startDate: Calendar
    lateinit var endDate: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pattern, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }

    private fun init() {
        startDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
        var year = Calendar.getInstance().get(Calendar.YEAR).toString()
        var month = (Calendar.getInstance().get(Calendar.MONTH)+1).toString()
        var day = Calendar.getInstance().get(Calendar.DATE).toString()

        startText.text = year+"."+month+"."+day
        endText.text = year+"."+month+"."+day

        //달력 시작 버튼 클릭
        startDate_pattern.setOnClickListener {
            val datePickerListener = object :DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    startDate.set(year,month,dayOfMonth)
                    startText.text = year.toString()+"."+(month+1).toString()+"."+dayOfMonth.toString()
                    updateData()
                }

            }

            var builder = DatePickerDialog(requireContext(),datePickerListener,startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE))
            builder.show()
        }

        //달력 마지막 버튼 클릭
        endDate_pattern.setOnClickListener {
            val datePickerListener = object :DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    endDate.set(year,month,dayOfMonth)
                    endText.text = year.toString()+"."+(month+1).toString()+"."+dayOfMonth.toString()
                    updateData()
                }

            }

            var builder = DatePickerDialog(requireContext(),datePickerListener,endDate.get(Calendar.YEAR),endDate.get(Calendar.MONTH),endDate.get(Calendar.DATE))
            builder.show()
        }
    }

    fun updateData(){
        //update data
    }


}