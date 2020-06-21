package com.klab.upright.ui.analysis

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
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

        initBarChart()
    }

    private fun initChart() {
//        val xAxis = lineChart.xAxis
//
//        xAxis.apply {
//            position = XAxis.XAxisPosition.BOTTOM
//            textSize = 10f
//            setDrawAxisLine(false)
//            granularity = 1f
//            axisMinimum = 2f
//            isGranularityEnabled = true
//        }
//
//        lineChart.apply {
//            axisRight.isEnabled = false
//            axisLeft.axisMaximum = 50f
//            legend.apply {
//                textSize = 15f
//                verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                horizontalAlignment =Legend.LegendHorizontalAlignment.CENTER
//                orientation = Legend.LegendOrientation.HORIZONTAL
//                setDrawInside(false)
//            }
//        }
//
//        val values = arrayListOf<Entry>()
//        //앉아있는시간
//
//        //서있는시간
//
////        val lineDataSet = LineDataSet
//
//        val lineData = LineData()
//        lineChart.data = lineData
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
        barChart.animateXY(2000,2000)
        barChart.invalidate()

    }

    fun updateData(){
        //update data
    }


}