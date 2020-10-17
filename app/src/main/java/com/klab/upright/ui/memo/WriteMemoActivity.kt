package com.klab.upright.ui.memo

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.github.mikephil.charting.utils.Utils.init
import com.klab.upright.R
import com.klab.upright.sharedPreference.PreferenceManager
import kotlinx.android.synthetic.main.activity_write_memo.*
import java.util.*

class WriteMemoActivity : AppCompatActivity() {

    val months=arrayOf("January", "February", "March","April","May","June","July","August","September","October","November","December")
    lateinit var writeDate:Calendar
    lateinit var pref: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_memo)
        pref = PreferenceManager(this)
        init()
    }

    fun init(){

        writeDate = Calendar.getInstance()
        dateText.text = months[(writeDate.get(Calendar.MONTH)+1)] +" "+writeDate.get(Calendar.DATE).toString()+", "+writeDate.get(Calendar.YEAR).toString()

        dateLayout.setOnClickListener {
            val datePickerListener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    writeDate.set(year,month,dayOfMonth)
                    val temp_str = months[(month+1)] +" "+ dayOfMonth.toString()+", "+year.toString()
                    dateText.text = temp_str
                        //year.toString()+"년 "+(month+1).toString()+"월 "+dayOfMonth.toString()+"일"
                }

            }

            val builder = DatePickerDialog(this,datePickerListener,writeDate.get(
                Calendar.YEAR),writeDate.get(Calendar.MONTH),writeDate.get(Calendar.DATE))
            builder.show()
        }

        closeBtn.setOnClickListener {
            finish()
        }

        addBtn.setOnClickListener {
            val time = timeText.text.toString()
            val type = typeText.text.toString()
            val pain = painText.text.toString()
            val edit = contentText.text.toString()
            val data = MemoData(writeDate,time,type,pain,edit)
            pref.addMemo(data)
            val intent = getIntent()
            setResult(RESULT_OK, intent)
            finish()
        }

        timeText.setOnClickListener {
            val dialog = SnapTimePickerDialog.Builder().apply {
                setTitle("Please select the exercise time")
            }.build().apply {
                setListener{ hour,minute ->
                    this.requireActivity().timeText.text = hour.toString()+" : "+minute.toString()
                }
            }
            dialog.show(supportFragmentManager,SnapTimePickerDialog.TAG)
        }
    }
}