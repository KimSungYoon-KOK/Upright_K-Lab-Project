package com.klab.upright

import android.content.Context
import android.graphics.DashPathEffect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.klab.upright.ui.analysis.Time
import kotlinx.android.synthetic.main.fragment_analysis.*
import kotlinx.android.synthetic.main.layout_chart.view.*
import kotlinx.android.synthetic.main.layout_chart2.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChartAdapter(val context:Context, val itemList: ArrayList<Time>) : PagerAdapter()
{
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
            1->{
                val dateList = arrayListOf<String>()
                val totalList = arrayListOf<Int>()
                for(time in itemList){
                    totalList.add(time.total);
                    dateList.add(time.date.toString());
                }

                view = inflater.inflate(R.layout.layout_chart,null)
                val lineChart = view.lineChart
                lineChart.apply {
                    setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                    axisRight.isEnabled=false
                    axisLeft.isEnabled=false
                    axisLeft.setDrawGridLines(false);
                    axisRight.setDrawGridLines(false);
                }

                val xAxis = lineChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return dateList[value.toInt()]
                    }
                }
                val yAxis = lineChart.axisLeft
                yAxis.setDrawGridLines(false)

                val values = arrayListOf<Entry>()

                for(i in totalList.indices){
                    values.add(Entry(i.toFloat(),totalList[i].toFloat(),ContextCompat.getDrawable(context, R.drawable.button_shadow_purple)))
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
                        formSize = 15f

                        setDrawCircleHole(true)

                        valueTextSize = 10f

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
                view = inflater.inflate(R.layout.layout_chart,null)
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