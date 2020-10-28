package com.klab.upright.ui.massage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_massage.*

class MassageFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_massage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    fun init(){
        seekBar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_background))
//        seekBar.color
    }

}