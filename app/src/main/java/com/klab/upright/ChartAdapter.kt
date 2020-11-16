package com.klab.upright

import android.content.Context
import android.graphics.DashPathEffect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.klab.upright.ui.analysis.Time
import com.klab.upright.ui.memo.MemoData
import kotlinx.android.synthetic.main.layout_chart.view.*
import kotlinx.android.synthetic.main.layout_chart2.view.*
import kotlinx.android.synthetic.main.layout_chart3.view.*

class ChartAdapter(val context:Context, val itemList: ArrayList<Time>, val memoList:ArrayList<MemoData>) : PagerAdapter()
{
    val TAG = "ChartAdapter"

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view: View
        when(position){
            0->{
                view = inflater.inflate(R.layout.layout_chart2,null)
                val chart = view.pieChart
            }
            1->{ // 날짜별 착용 시간
                val dateList = arrayListOf<String>()
                val totalList = arrayListOf<Int>()
                for(time in itemList){
                    totalList.add(time.total);
                    val str = time.date.toString().substring(4,6)+"/"+time.date.toString().substring(6,8) // 11/22
                    dateList.add(str);
                }

                view = inflater.inflate(R.layout.layout_chart,null)
                val lineChart = view.lineChart
                lineChart.apply {
                    setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                    axisRight.isEnabled=false
                    axisLeft.isEnabled=false
                    axisLeft.setDrawGridLines(false)
                    axisRight.setDrawGridLines(false)
                    description.isEnabled = false
                    setTouchEnabled(false)
                    isDragEnabled = false
                    legend.isEnabled = false
                    animateX(1000)

                }


                val xAxis = lineChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.isGranularityEnabled = true
                xAxis.textSize = 13f

                val yAxis = lineChart.axisLeft
                yAxis.setDrawGridLines(false)

                val values = arrayListOf<Entry>()

                for(i in totalList.indices){
                    values.add(Entry(i.toFloat(),totalList[i].toFloat(),ContextCompat.getDrawable(context, R.drawable.button_shadow_purple)))
                }

                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        if(dateList.size > value.toInt()){
                            return dateList[value.toInt()]
                        }
                        return ""
                    }
                }

                val set1:LineDataSet
                if (lineChart.data != null &&
                    lineChart.data.dataSetCount > 0
                ) {
                    set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
                    set1.values = values
                    set1.notifyDataSetChanged()
                    lineChart.data.notifyDataChanged()
                    lineChart.notifyDataSetChanged()
                }else {
                    set1 = LineDataSet(values,"Dataset 1")

                    set1.apply {
                        setDrawIcons(false)

                        // black line and point
                        color = ContextCompat.getColor(context,R.color.colorPrimary)
                        setCircleColor(ContextCompat.getColor(context,R.color.colorPrimary))


                        lineWidth=2f
                        circleRadius = 6f
                        circleHoleRadius = 4f

                        // customize legend entry
                        formLineWidth = 1f
                        formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
                        formSize = 13f

                        setDrawCircleHole(true)

                        valueTextSize = 13f

                        cubicIntensity = 0.2f
                        setMode(LineDataSet.Mode.CUBIC_BEZIER)

                        setDrawFilled(true)
                        fillColor = ContextCompat.getColor(context,R.color.purple_light_background)
                        fillFormatter =
                            IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }
                    }



                    val dataSets = arrayListOf<ILineDataSet>()
                    dataSets.add(set1)

                    val data = LineData(dataSets)

                    lineChart.data = data

                }
            }
            else->{
                view = inflater.inflate(R.layout.layout_chart3,null)
                val barChart = view.barChart

                val dateList = arrayListOf<String>()
                for(i in memoList.indices){
                    val str = memoList[i].date.toString().substring(4,6)+"/"+memoList[i].date.toString().substring(6,8) // 11/22
                    dateList.add(str);
                }

                barChart.apply {
                    setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                    axisRight.isEnabled=false
                    axisLeft.isEnabled=false
                    axisLeft.setDrawGridLines(false)
                    axisRight.setDrawGridLines(false)
                    description.isEnabled = false
                    setTouchEnabled(false)
                    isDragEnabled = false
                    legend.isEnabled = false
                    animateX(1000)
                }

                val xAxis = barChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.isGranularityEnabled = true
                xAxis.textSize = 13f

                val yAxis = barChart.axisLeft
                yAxis.setDrawGridLines(false)

                val values = arrayListOf<BarEntry>()

                for(i in memoList.indices){
                    values.add(BarEntry(i.toFloat(),memoList[i].pain.toFloat(),ContextCompat.getDrawable(context, R.drawable.button_shadow_purple)))
                }

                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        if(dateList.size > value.toInt()){
                            return dateList[value.toInt()]
                        }
                        return ""
                    }
                }

                val set1:BarDataSet
                if (barChart.data != null &&
                    barChart.data.dataSetCount > 0
                ) {
                    set1 = barChart.data.getDataSetByIndex(0) as BarDataSet
                    set1.values = values
                    set1.notifyDataSetChanged()
                    barChart.data.notifyDataChanged()
                    barChart.notifyDataSetChanged()
                }else {
                    set1 = BarDataSet(values,"Dataset 1")

                    val colorList = arrayListOf<Int>(
                        ContextCompat.getColor(context,R.color.level1_purple),
                        ContextCompat.getColor(context,R.color.level2_purple),
                        ContextCompat.getColor(context,R.color.level3_purple),
                        ContextCompat.getColor(context,R.color.level4_purple),
                        ContextCompat.getColor(context,R.color.level5_purple)
                    )

                    set1.apply {
                        setDrawIcons(false)

                        // black line and point
                        //color = ContextCompat.getColor(context,R.color.colorPrimary)

                        // customize legend entry
                        formLineWidth = 1f
                        formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
                        formSize = 13f

                        valueTextSize = 13f

                        colors = colorList
                        setValueTextColors(colorList)
                    }


                    val dataSets = arrayListOf<IBarDataSet>()
                    dataSets.add(set1)

                    val data = BarData(dataSets)

                    barChart.data = data

                }

            }
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }

    private fun setChartData() {

    }

}