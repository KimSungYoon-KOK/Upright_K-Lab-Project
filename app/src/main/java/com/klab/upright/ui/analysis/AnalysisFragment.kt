package com.klab.upright.ui.analysis

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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

        //initBarChart()
        initChart()
    }

    private fun initChart() {

        lineChart.apply{
            setViewPortOffsets(0f,0f,0f,0f)
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

            description.isEnabled = false
            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setBorderColor(ContextCompat.getColor(requireContext(), R.color.white))
            setDrawGridBackground(false)
        }

        val x = lineChart.xAxis
        x.isEnabled = false

        val y = lineChart.axisLeft
        y.apply {
            textColor =ContextCompat.getColor(requireContext(), R.color.black)
            textSize = 16f
            setLabelCount(5)
            setDrawLabels(true)
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            axisLineColor = ContextCompat.getColor(requireContext(), R.color.black)
            setDrawGridLines(true)
        }

        lineChart.axisRight.isEnabled = false

        setChartData()



    }

    private fun setChartData() {
        //set data
        var values = arrayListOf<Entry>()

        for (i in 0 until 10) {
            val v =Random().nextInt(10).toFloat()+3f
            values.add(Entry(i.toFloat(), v))
        }

        val set1: LineDataSet

        // create a dataset and give it a type
        set1 = LineDataSet(values, "Sitting Time")
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        set1.setDrawFilled(true)
        set1.setDrawCircles(false)
        set1.lineWidth = 1.8f
        set1.circleRadius = 4f
        set1.setCircleColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        set1.highLightColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        set1.color =ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        set1.fillColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        set1.fillAlpha = 100
        set1.setDrawHorizontalHighlightIndicator(false)
        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> lineChart.getAxisLeft().getAxisMinimum() }


        //set data
        var values2 = arrayListOf<Entry>()

        for (i in 0 until 10) {
            val v =Random().nextInt(4).toFloat()
            values2.add(Entry(i.toFloat(), v))
        }

        val set2: LineDataSet

        // create a dataset and give it a type
        set2 = LineDataSet(values2, "Standing Time")
        set2.mode = LineDataSet.Mode.CUBIC_BEZIER
        set2.cubicIntensity = 0.2f
        set2.setDrawFilled(true)
        set2.setDrawCircles(false)
        set2.lineWidth = 1.8f
        set2.circleRadius = 4f
        set2.setCircleColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        set2.highLightColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        set2.color =ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        set2.fillColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        set2.fillAlpha = 100
        set2.setDrawHorizontalHighlightIndicator(false)
        set2.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> lineChart.getAxisLeft().getAxisMinimum() }

        // create a data object with the data sets

        val dataList = arrayListOf<ILineDataSet>()
        dataList.add(set1)
        dataList.add(set2)

        val data = LineData(dataList)

        //data.setValueTypeface(tfLight)
        data.setValueTextSize(12f)
        data.setDrawValues(true)
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))

        // set data
        lineChart.setData(data)
    }

    fun updateData(){
        //update data
    }

//    fun initBarChart(){
//        val entries = arrayListOf<BarEntry>()
//        entries.add(BarEntry(5.5f,0))
//        entries.add(BarEntry(3f,1))
//
//        val depenses = BarDataSet(entries,"시간")
//        depenses.axisDependency = YAxis.AxisDependency.LEFT
//
//        var labels = arrayListOf<String>()
//        labels.add("앉아있는 시간")
//        labels.add("서있는 시간")
//
//        var data = BarData(labels,depenses)
//        data.setValueTextSize(16f)
//
//
//        depenses.setColors(ColorTemplate.PASTEL_COLORS)
//
//        val legend = barChart.legend
//        legend.apply {
//            textSize = 15f
//            formSize = 0f
//            //formSize = 30f
//            mTextHeightMax = 10f
//
//
//        }
//
//        val xAxis = barChart.xAxis
//        xAxis.apply {
//            setDrawGridLines(false)
//            textSize = 16f
//
//        }
//
//        val rightAxis = barChart.axisRight
//        rightAxis.isEnabled = false
//
//        barChart.data = data
//        barChart.setDescription(null)
//        barChart.animateXY(1500,1500)
//        barChart.invalidate()
//
//    }

}