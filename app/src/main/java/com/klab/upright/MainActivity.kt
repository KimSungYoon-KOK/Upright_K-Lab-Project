package com.klab.upright

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.BluetoothState.REQUEST_ENABLE_BT
import app.akexorcist.bluetotohspp.library.DeviceList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.klab.upright.ui.guide.GuideActivity
import com.klab.upright.ui.guide.GuideViewPagerAdapter
import kotlinx.android.synthetic.main.drawer_main.*
import kotlinx.android.synthetic.main.drawer_main_header.view.*

class MainActivity : AppCompatActivity() {

    var bt:BluetoothSPP?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_main)
        initView()
    }



    private fun initView() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_analysis, R.id.navigation_memo, R.id.navigation_massage))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        (application as MyApplication).initBluetooth(this)
        bt = (application as MyApplication).bt
        initBtn()
    }

    private fun initBtn() {
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_textSize -> {
                    Toast.makeText(this, "Chage Font", Toast.LENGTH_SHORT).show()
                }
                R.id.action_notify -> {
                    Toast.makeText(this, "Notice", Toast.LENGTH_SHORT).show()
                }
                R.id.action_guide -> {
                    val intent = Intent(this, GuideActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Tutorial", Toast.LENGTH_SHORT).show()
                }
                R.id.action_help -> {
                    Toast.makeText(this, "Customer Service", Toast.LENGTH_SHORT).show()
                }
                R.id.action_version -> {
                    Toast.makeText(this, "Version : 1.0.0", Toast.LENGTH_SHORT).show()
                }
            }
            return@setNavigationItemSelectedListener false
        }

        navigationView.getHeaderView(0).bluetoothBtn.setOnClickListener {
            onClickBlueTooth()
        }
    }

    private fun onClickBlueTooth() {
        if(bt != null){
            if (bt!!.serviceState == BluetoothState.STATE_CONNECTED) {
                bt!!.disconnect()
            } else {
                val intent = Intent(this, BLEConnectActivity::class.java)
                startActivityForResult(intent, RESULT_OK)
            }
        }
        Toast.makeText(this,"initbluetooth",Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt?.connect(data)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt?.setupService()
                bt?.startService(BluetoothState.DEVICE_OTHER)
            } else {
                Toast.makeText(
                    this
                    , "Bluetooth was not enabled."
                    , Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_drawer -> {
                layout_drawer_main.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() { //뒤로가기 처리
        if(layout_drawer_main.isDrawerOpen(GravityCompat.END)){
            layout_drawer_main.closeDrawers()
        } else{
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        bt?.stopService()
    }

    override fun onStart() {
        super.onStart()
        if(bt != null){
            if (!bt!!.isBluetoothEnabled) { //
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)
            } else {
                if (!bt!!.isServiceAvailable) {
                    bt!!.setupService()
                    bt!!.startService(BluetoothState.DEVICE_OTHER) //DEVICE_ANDROID는 안드로이드 기기 끼리
                }
            }
        }
    }
}

