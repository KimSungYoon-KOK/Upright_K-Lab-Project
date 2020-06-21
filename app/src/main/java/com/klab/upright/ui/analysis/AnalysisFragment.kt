package com.klab.upright.ui.analysis

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_analysis.*
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

            val builder = DatePickerDialog(requireContext(),datePickerListener,startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE))
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

        initBarChart()
    }

    fun updateData(){
        //update data
    }

    fun initBarChart(){
        val entries = arrayListOf<BarEntry>()
        entries.add(BarEntry(5.5f,0))
        entries.add(BarEntry(3f,1))

        val depenses = BarDataSet(entries,"시간")
        depenses.axisDependency = YAxis.AxisDependency.LEFT

        var labels = arrayListOf<String>()
        labels.add("앉아있는 시간")
        labels.add("서있는 시간")

        var data = BarData(labels,depenses)
        data.setValueTextSize(16f)


        depenses.setColors(ColorTemplate.PASTEL_COLORS)

        val legend = barChart.legend
        legend.apply {
            textSize = 15f
            formSize = 0f
            //formSize = 30f
            mTextHeightMax = 10f


        }

        val xAxis = barChart.xAxis
        xAxis.apply {
            setDrawGridLines(false)
            textSize = 16f

        }

        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        barChart.data = data
        barChart.setDescription(null)
        barChart.animateXY(1500,1500)
        barChart.invalidate()

    }

}