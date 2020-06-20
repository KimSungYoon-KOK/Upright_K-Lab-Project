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
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        startDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        val month = (Calendar.getInstance().get(Calendar.MONTH)+1).toString()
        val day = Calendar.getInstance().get(Calendar.DATE).toString()

        startText.text = "$year.$month.$day"
        endText.text = "$year.$month.$day"

        //달력 시작 버튼 클릭
        startDate_pattern.setOnClickListener {
            val datePickerListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    startDate.set(year,month,dayOfMonth)
                    startText.text = "${year}.${month+1}.${dayOfMonth}"
                    updateData()
                }

            var builder = DatePickerDialog(requireContext(),datePickerListener,startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE))
            builder.show()
        }

        //달력 마지막 버튼 클릭
        endDate_pattern.setOnClickListener {
            val datePickerListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    endDate.set(year,month,dayOfMonth)
                    endText.text = "${year}.${month+1}.${dayOfMonth}"
                    updateData()
                }

            val builder = DatePickerDialog(requireContext(),datePickerListener,endDate.get(Calendar.YEAR),endDate.get(Calendar.MONTH),endDate.get(Calendar.DATE))
            builder.show()
        }
    }

    fun updateData(){
        //update data
    }

}