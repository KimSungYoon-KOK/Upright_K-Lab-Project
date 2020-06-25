package com.klab.upright.ui.memo

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.github.mikephil.charting.utils.Utils.init
import com.klab.upright.R
import kotlinx.android.synthetic.main.activity_write_memo.*
import java.util.*

class WriteMemoActivity : AppCompatActivity() {

    lateinit var writeDate:Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_memo)
        init()
    }

    fun init(){

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

            var builder = DatePickerDialog(this,datePickerListener,writeDate.get(
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
            val data = MemoData(Calendar.getInstance(),time,type,pain,edit)
            val intent = getIntent()
            intent.putExtra("data",data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}