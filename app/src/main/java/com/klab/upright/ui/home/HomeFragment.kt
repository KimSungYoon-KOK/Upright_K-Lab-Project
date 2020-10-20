package com.klab.upright.ui.home

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.SimpleExpandableListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.klab.upright.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_pressure.view.*
import java.util.*

class HomeFragment : Fragment() {

    val LIST_NAME = "NAME"
    val LIST_UUID = "UUID"
    val unknown_characteristic = "Unknown characteristic"
    val unknown_service = "Unknown service"

    private var mGattCharacteristics =
        ArrayList<ArrayList<BluetoothGattCharacteristic>>()

    private lateinit var deviceName: String
    private lateinit var deviceAddress: String

    private var mNotifyCharacteristic: BluetoothGattCharacteristic? = null
    private var mWriteCharacteristic: BluetoothGattCharacteristic? = null

    // Code to manage Service lifecycle.
    private var bluetoothLeService: BluetoothLeService?=null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bluetoothLeService = (service as BluetoothLeService.LocalBinder).service
            bluetoothLeService?.connect(deviceAddress)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bluetoothLeService = null
        }
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read or notification operations.
    var mConnected: Boolean = false
    private val gattUpdateReceiver = object: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            Log.d("Log_BroadCast_Intent: ", intent.action)
            when(intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    mConnected = true
                    Toast.makeText(requireContext(), "BLE: Connected to device", Toast.LENGTH_SHORT).show()
                    updateConnectionState(R.string.connected)
//                    invalidateOptionsMenu()

                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    mConnected = false
                    Toast.makeText(requireContext(), "BLE: Disconnected to device", Toast.LENGTH_SHORT).show()
                    updateConnectionState(R.string.disconnected)
//                    invalidateOptionsMenu()
                    clearUI()
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    displayGattServices(bluetoothLeService?.getSupportedGattServices())
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA)!!)
                    updateData()
//                    bluetoothLeService.let {
//                        val data = intent.getStringExtra(EXTRA_DATA)
//                        Log.d("Log_Data", data)
//                        if (data != null) {
//                            Log.d("Log_Data", data)
//                            displayData(data)
//                        }
//                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }


    private fun init() {




    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Toast.makeText(
            requireContext()
            , "attach"
            , Toast.LENGTH_SHORT
        ).show()
        //BLEConnectActivity 로 부터 intent 받기
        deviceName = (activity as MainActivity).getData_Name()
        deviceAddress = (activity as MainActivity).getData_Adress()

        if(deviceName.isNotEmpty()){
            //GATT Intent
            val gattServiceIntent = Intent(requireContext(), BluetoothLeService::class.java)
            activity?.bindService(gattServiceIntent, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)

            //onresume
            activity?.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
            if (bluetoothLeService != null) {
                val result: Boolean = bluetoothLeService!!.connect(deviceAddress)
                Log.d("Log_connection_request", "Connect request result=$result")
            }else{
                Log.d("Log_connection_request", "Connect request result=null")
            }
        }
//        device_address.text = deviceAddress
    }

    private fun clearUI() {
//        data_value.text = getText(R.string.no_data)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        if(deviceName.isNotEmpty()){
            activity?.unregisterReceiver(gattUpdateReceiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(deviceName.isNotEmpty()){
            activity?.unbindService(serviceConnection)
            bluetoothLeService = null
        }

    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.gatt_services, menu)
//        if (mConnected) {
//            menu.findItem(R.id.menu_connect).isVisible = false
//            menu.findItem(R.id.menu_disconnect).isVisible = true
//        } else {
//            menu.findItem(R.id.menu_connect).isVisible = true
//            menu.findItem(R.id.menu_disconnect).isVisible = false
//        }
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_connect -> {
//                bluetoothLeService?.connect(deviceAddress)
//                return true
//            }
//            R.id.menu_disconnect -> {
//                bluetoothLeService?.disconnect()
//                return true
//            }
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun updateConnectionState(resourceId: Int) {
        activity?.runOnUiThread { connection_state.text = getText(resourceId) }
    }

    private fun displayData(data: String) {
        activity?.runOnUiThread { data_value.text = data }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView on the UI.
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {

        if (gattServices == null) return

        var uuid = ""
        val gattServiceData = ArrayList<HashMap<String, String>>()
        val gattCharacteristicData = ArrayList<ArrayList<HashMap<String, String>>>()
        mGattCharacteristics = ArrayList()

        // Loops through available GATT Services.

        // Loops through available GATT Services.
        for (gattService in gattServices) {
            val currentServiceData =
                HashMap<String, String>()
            uuid = gattService.uuid.toString()
            val temp = SampleGattAttributes().lookup(uuid, unknown_service)
            currentServiceData[LIST_NAME] = temp.toString()
            currentServiceData[LIST_UUID] = uuid
            gattServiceData.add(currentServiceData)
            val gattCharacteristicGroupData =
                ArrayList<HashMap<String, String>>()
            val gattCharacteristics =
                gattService.characteristics
            val charas =
                ArrayList<BluetoothGattCharacteristic>()

            // Loops through available Characteristics.
            for (gattCharacteristic in gattCharacteristics) {
                charas.add(gattCharacteristic)
                val currentCharaData =
                    HashMap<String, String>()
                uuid = gattCharacteristic.uuid.toString()
                val temp2 = SampleGattAttributes().lookup(uuid, unknown_characteristic)
                currentCharaData[LIST_NAME] = temp2.toString()
                currentCharaData[LIST_UUID] = uuid
                gattCharacteristicGroupData.add(currentCharaData)
            }
            mGattCharacteristics.add(charas)
            gattCharacteristicData.add(gattCharacteristicGroupData)
        }

//        val gattServiceAdapter = DataListAdapter(this,listener,gattServiceData,gattCharacteristicData)
//        data_recyclerview.adapter = gattServiceAdapter
//        Log.d("inae","set adapter")
        val gattServiceAdapter = SimpleExpandableListAdapter(
            requireContext(),
            gattServiceData,
            android.R.layout.simple_expandable_list_item_2,
            arrayOf(LIST_NAME,LIST_UUID),
            intArrayOf(android.R.id.text1, android.R.id.text2),
            gattCharacteristicData,
            android.R.layout.simple_expandable_list_item_2,
            arrayOf(LIST_NAME,LIST_UUID),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        data_recyclerview.setAdapter(gattServiceAdapter)
        updateData()
        data_recyclerview.setOnChildClickListener(object: ExpandableListView.OnChildClickListener{
            override fun onChildClick(
                parent: ExpandableListView?,
                view: View?,
                groupPosition: Int,
                childPosition: Int,
                id: Long
            ): Boolean {
                Log.d("inae click position",groupPosition.toString()+","+childPosition.toString())

                if (mGattCharacteristics != null) {
                    val characteristic =
                        mGattCharacteristics[groupPosition][childPosition]
                    Log.d("inae",characteristic.toString())
                    val charaProp = characteristic.properties
                    if (charaProp or BluetoothGattCharacteristic.PROPERTY_READ > 0) {
                        // If there is an active notification on a characteristic, clear
                        // it first so it doesn't update the data field on the user interface.
                        if (mNotifyCharacteristic != null) {
                            bluetoothLeService?.setCharacteristicNotification(
                                mNotifyCharacteristic!!, false
                            )
                            mNotifyCharacteristic = null
                        }
                        bluetoothLeService?.readCharacteristic(characteristic)
                    }
                    if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                        mNotifyCharacteristic = characteristic
                        bluetoothLeService?.setCharacteristicNotification(
                            characteristic, true
                        )
                    }
                    return true
                }
                return false
            }

        })
//        mGattServicesList.setAdapter(gattServiceAdapter)
    }

    private fun sendData(data: String) {
        mWriteCharacteristic?.let {
            if (it.properties or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE > 0)
                bluetoothLeService?.writeCharacteristic(it, data)
        }

        mNotifyCharacteristic?.let {
            if (it.properties or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0)
                bluetoothLeService?.setCharacteristicNotification(it, true)
        }
    }

    private fun updateData(){
        val characteristic = mGattCharacteristics[2][0]
        val charaProp = characteristic.properties
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_READ > 0) {
            Log.d("inae updateData read","1")
            // If there is an active notification on a characteristic, clear
            // it first so it doesn't update the data field on the user interface.
            if (mNotifyCharacteristic != null) {
                bluetoothLeService?.setCharacteristicNotification(
                    mNotifyCharacteristic!!, false
                )
                mNotifyCharacteristic = null
            }
            bluetoothLeService?.readCharacteristic(characteristic)
        }
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            Log.d("inae updateData notify","2")
            mNotifyCharacteristic = characteristic
            bluetoothLeService?.setCharacteristicNotification(
                characteristic, true
            )
        }

    }




    companion object {
        const val EXTRAS_DEVICE_NAME = "DEVICE_NAME"
        const val EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS"
        val UUID_DATA_WRITE: UUID = UUID.fromString("0000fff2-0000-1000-8000-00805F9B34FB")
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter? {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
        return intentFilter
    }




}

