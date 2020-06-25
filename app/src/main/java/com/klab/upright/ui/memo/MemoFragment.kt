package com.klab.upright.ui.memo

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_memo.*
import java.util.*

class MemoFragment : Fragment() {

    var writeList = arrayListOf<MemoData>()
    lateinit var adapter: MemoAdapter
    val REQUEST_TEST = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_memo, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        setExample()
        val layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        recyclerView_write.layoutManager = layoutManager
        adapter = MemoAdapter(requireContext(), writeList)
        recyclerView_write.adapter = adapter
        writeBtn.setOnClickListener {
            val intent = Intent(requireContext(), WriteMemoActivity::class.java)
            startActivityForResult(intent,REQUEST_TEST)
        }

    }

    fun setExample(){
        writeList.add(MemoData(Calendar.getInstance(),"1시간 30분","달리기","아픔","오늘은 운동했다"))
        writeList.add(MemoData(Calendar.getInstance(),"2시간 30분","걷기","안아픔","오늘은 운동했다"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TEST) {
            if (resultCode == RESULT_OK) {
                val d = data?.getSerializableExtra("data") as MemoData
                writeList.add(d)
            }
        }

    }

}