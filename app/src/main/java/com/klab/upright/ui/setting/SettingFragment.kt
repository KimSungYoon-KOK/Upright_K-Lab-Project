package com.klab.upright.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.klab.upright.MyApplication
import com.klab.upright.R
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    var bt:BluetoothSPP?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bt = (activity?.application as MyApplication).bt
        init()
        initBluetooth()

    }

    private fun initBluetooth() {


        //블루투스 연결 버튼 클릭 시
        bluetoothBtn.setOnClickListener {
            (activity?.application as MyApplication).initBluetooth(requireContext())
            if(bt != null){
                if (bt!!.serviceState == BluetoothState.STATE_CONNECTED) {
                    bt!!.disconnect()
                } else {
                    val intent = Intent(requireContext(), DeviceList::class.java)
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
                }
            }
            Toast.makeText(requireContext(),"initbluetooth",Toast.LENGTH_SHORT).show()

        }
    }

    private fun init() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt?.connect(data)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt?.setupService()
                bt?.startService(BluetoothState.DEVICE_OTHER)
            } else {
                Toast.makeText(
                    requireContext()
                    , "Bluetooth was not enabled."
                    , Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}