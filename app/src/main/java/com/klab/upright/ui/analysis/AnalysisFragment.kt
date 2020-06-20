package com.klab.upright.ui.analysis

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_analysis.endDate_pattern
import kotlinx.android.synthetic.main.fragment_analysis.endText
import kotlinx.android.synthetic.main.fragment_analysis.startDate_pattern
import kotlinx.android.synthetic.main.fragment_analysis.startText
import java.util.*


class AnalysisFragment : Fragment() {

    lateinit var startDate: Calendar
    lateinit var endDate: Calendar

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
        startDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
        var year = Calendar.getInstance().get(Calendar.YEAR).toString()
        var month = (Calendar.getInstance().get(Calendar.MONTH)+1).toString()
        var day = Calendar.getInstance().get(Calendar.DATE).toString()

        startText.text = year+"."+month+"."+day
        endText.text = year+"."+month+"."+day

        //달력 시작 버튼 클릭
        startDate_pattern.setOnClickListener {
            val datePickerListener = object : DatePickerDialog.OnDateSetListener{
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
            val datePickerListener = object : DatePickerDialog.OnDateSetListener{
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