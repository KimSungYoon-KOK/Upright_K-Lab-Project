package com.klab.upright.ui.home

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.SimpleExpandableListAdapter
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.klab.upright.BluetoothLeService
import com.klab.upright.MainActivity
import com.klab.upright.R
import com.klab.upright.SampleGattAttributes
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    val LIST_NAME = "NAME"
    val LIST_UUID = "UUID"
    val unknown_characteristic = "Unknown characteristic"
    val unknown_service = "Unknown service"
    val TAG = "homeFragment_log"
    var colorFrom=0
    var colorTo=0
    lateinit var imageList:ArrayList<Pair<Drawable,Int>>
    var imageCount=0
    var imageCount2=5
    lateinit var dataList:ArrayList<PostureData>

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
                    updateConnectionState(R.string.connected)

                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    mConnected = false
                    updateConnectionState(R.string.disconnected)
                    clearUI()
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    displayGattServices(bluetoothLeService?.getSupportedGattServices())
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA)!!)
                    updateData()
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

        //BLEConnectActivity 로 부터 intent 받기
        deviceName = (activity as MainActivity).getData_Name()
        deviceAddress = (activity as MainActivity).getData_Adress()

        colorFrom = ContextCompat.getColor(requireContext(),R.color.white)
        colorTo = ContextCompat.getColor(requireContext(),R.color.white)
        dataList = arrayListOf()
        testImage()
        savePostureData()
    }

    private fun savePostureData() {
//        reset.setOnClickListener {
//            dataList.clear()
//            Toast.makeText(requireContext(),"reset",Toast.LENGTH_SHORT).show()
//
//        }
//        save.setOnClickListener {
//            if(dataList.isNotEmpty()){
////                val os = resources.openRawResource(R.raw.data) as OutputStream
////                val stream = OutputStreamWriter(os,"utf-8")
////                val file = getResources().openRawResource(R.raw.data);
////                val f = File(file)
////                val fw = FileWriter(file)
////                print("fw : "+fw.toString())
//
//                var text = ""
//                for(data in dataList){
//                    val str = data.x.toString()+","+data.y.toString()+","+data.z.toString()+"\n"
//                    text += str
//                }
//
//                val bw =
//                    BufferedWriter(FileWriter( activity?.filesDir.toString()+"data.txt"))
//                bw.write(text)
//                bw.close()
//                Log.d("textsave",text)
//
//                Toast.makeText(requireContext(),"save "+text,Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(requireContext(),"empty!",Toast.LENGTH_SHORT).show()
//
//            }
//        }
    }



    private fun testImage(){
        imageList = arrayListOf()
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.sit1)!!,R.color.sit_color1))
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.sit2)!!,R.color.sit_color2))
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.sit3)!!,R.color.sit_color3))
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.sit4)!!,R.color.sit_color2))
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.sit5)!!,R.color.sit_color1))

        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.left)!!,R.color.sit_color1))
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.good)!!,R.color.sit_color3))
        imageList.add(Pair(ContextCompat.getDrawable(requireContext(),R.drawable.right)!!,R.color.sit_color1))

        image_posture.setOnClickListener {
            imageCount++
            if(imageCount == 5)
                imageCount=0
            image_posture.setImageDrawable(imageList[imageCount].first)
            image_posture.setTint(imageList[imageCount].second)
//            image_posture.im
//            val colorStateList = ContextCompat.getColorStateList()
//            colorStateList.
//            image_posture.imageTintList(ColorStateList.valueOf(imageList[imageCount].second))
//            image_posture.setColorFilter(imageList[imageCount].second,android.graphics.PorterDuff.Mode.SRC_IN)
        }

        image_posture2.setOnClickListener {
            imageCount2++
            if(imageCount2 == 8)
                imageCount2 = 5
            image_posture2.setImageDrawable(imageList[imageCount2].first)
            image_posture2.setTint(imageList[imageCount2].second)
//            image_posture.im
//            val colorStateList = ContextCompat.getColorStateList()
//            colorStateList.
//            image_posture.imageTintList(ColorStateList.valueOf(imageList[imageCount].second))
//            image_posture.setColorFilter(imageList[imageCount].second,android.graphics.PorterDuff.Mode.SRC_IN)
        }

    }

    fun ImageView.setTint(@ColorRes colorRes: Int) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
    }

    private fun init() {
        if(deviceName.isNotEmpty()){
            //GATT Intent
            val gattServiceIntent = Intent(requireContext(), BluetoothLeService::class.java)
            activity?.bindService(gattServiceIntent, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)

            //onresume
            activity?.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
            if (bluetoothLeService != null) {
                val result: Boolean = bluetoothLeService!!.connect(deviceAddress)
                Log.d(TAG, "Connect request result=$result")
            }else{
                Log.d(TAG, "Connect request result=null")
            }
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")

    }

    private fun clearUI() {
        data_value.text = getText(R.string.no_data)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        init()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        if(deviceName.isNotEmpty()){
            activity?.unregisterReceiver(gattUpdateReceiver)
            activity?.unbindService(serviceConnection)
            bluetoothLeService = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        if(deviceName.isNotEmpty()){
//            activity?.unbindService(serviceConnection)
//            bluetoothLeService = null
        }

    }

    private fun updateConnectionState(resourceId: Int) {
        activity?.runOnUiThread { connection_state.text = getText(resourceId) }
    }

    private fun displayData(data: String) {
        activity?.runOnUiThread {
            data_value.text = data
//            val value_split = data.split(",")
//            val v = value_split[0].toDouble()
////            for(v in value_split){
////                colorFrom = colorTo
////                if(v.toInt() > 0.9){
////                    colorTo = ContextCompat.getColor(requireContext(),R.color.orange_dark)
////                }else if(v.toInt() > 0.5){
////                    colorTo = ContextCompat.getColor(requireContext(),R.color.orange_middle)
////                }else{
////                    colorTo = ContextCompat.getColor(requireContext(),R.color.orange_light)
////                }
////            }
//            colorFrom = colorTo
//            if(v > 0.9){
//                colorTo = ContextCompat.getColor(requireContext(),R.color.orange_dark)
//            }else if(v.toInt() > 0.5){
//                colorTo = ContextCompat.getColor(requireContext(),R.color.orange_middle)
//            }else{
//                colorTo = ContextCompat.getColor(requireContext(),R.color.orange_light)
//            }
//            val colorAnimation =
//                ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
//            colorAnimation.duration = 900// milliseconds
//            colorAnimation.addUpdateListener { animator -> homeLayout.setBackgroundColor(animator.animatedValue as Int) }
//            colorAnimation.start()


            val value_split = data.split(",")
            val x1 = value_split[0].toDouble()
            val x2 = value_split[1].toDouble()
            val x3 = value_split[2].toDouble()

            val y = x1*0.0569 + x2*0.0101 + x3*(-0.0048) + 5.5510
            val result = (y+0.5).toInt()
            postureView.text = result.toString()
            dataList.add(PostureData(x1,x2,x3))

//            img1.visibility=View.INVISIBLE
//            img2.visibility=View.INVISIBLE
//            img3.visibility=View.INVISIBLE
//            when(result){
//
//                0->{
//                }
//                1->{
//                    img1.visibility=View.VISIBLE
//                }
//                2->{
//                    img2.visibility=View.VISIBLE
//                }
//                3->{
//                    img3.visibility=View.VISIBLE
//                }
//                else->{
//
//                }
//            }

        }


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


    inner class PostureData(var x:Double,var y:Double,var z:Double){

    }



}

