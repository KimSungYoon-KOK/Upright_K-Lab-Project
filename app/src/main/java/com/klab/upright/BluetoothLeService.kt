package com.klab.upright

import android.app.Service
import android.bluetooth.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*


class BluetoothLeService: Service() {

    val STATE_DISCONNECTED = 0
    val STATE_CONNECTING = 1
    val STATE_CONNECTED = 2

    var connectionState = STATE_DISCONNECTED
    var bluetoothGatt: BluetoothGatt? = null
    var bluetoothManager: BluetoothManager? = null
    var deviceAddress: String = ""
    val bluetoothAdapter: BluetoothAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    inner class LocalBinder: Binder() {
        val service = this@BluetoothLeService
    }

    val binder = LocalBinder()
    override fun onBind(intent: Intent?): IBinder? = binder
    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    fun connect(address: String): Boolean {
        bluetoothGatt?.let {
            if (address == deviceAddress) {
                return if (it.connect()) {
                    connectionState = STATE_CONNECTING
                    true
                } else false
            }
        }

        val device = bluetoothAdapter.getRemoteDevice(address)
        bluetoothGatt = device.connectGatt(this, false, gattCallback)
        connectionState = STATE_CONNECTING
        return true
    }

    fun disconnect() {
        bluetoothGatt?.disconnect()
    }

    fun close() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    // Various callback methods defined by the BLE API.
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            val intentAction: String
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    intentAction = ACTION_GATT_CONNECTED
                    connectionState = STATE_CONNECTED
                    broadcastUpdate(intentAction)
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    intentAction = ACTION_GATT_DISCONNECTED
                    connectionState = STATE_DISCONNECTED
                    broadcastUpdate(intentAction)
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
                else -> Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        // Result of a characteristic read operation
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            if (characteristic != null) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
            }
        }
    }

    private fun broadcastUpdate(action: String) {
        sendBroadcast(Intent(action))
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)
        if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            val flag = characteristic.properties
            var format = -1
            if (flag and 0x01 != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16
                Log.d(TAG, "Heart rate format UINT16.")
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8
                Log.d(TAG, "Heart rate format UINT8.")
            }
            val heartRate = characteristic.getIntValue(format, 1)
            Log.d(TAG, String.format("Received heart rate: %d", heartRate))
            intent.putExtra(EXTRA_DATA, heartRate.toString())
        } else {
            // For all other profiles, writes the data formatted in HEX.
            val data = characteristic.value
            if (data != null && data.isNotEmpty()) {
                val stringBuilder = StringBuilder(data.size)
                for (byteChar in data) stringBuilder.append(String.format("%02X ", byteChar))
                intent.putExtra(EXTRA_DATA, data.toString() + "\n" + stringBuilder.toString())
            }
        }
        sendBroadcast(intent)
    }

    fun getSupportedGattServices(): MutableList<BluetoothGattService>? {
        return bluetoothGatt?.services
    }

    /**
     * Request a read on a given `BluetoothGattCharacteristic`. The read result is reported
     * asynchronously through the `BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)`
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        bluetoothGatt!!.readCharacteristic(characteristic)
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        bluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT == characteristic.uuid) {
            val descriptor = characteristic.getDescriptor(
                UUID.fromString(SampleGattAttributes().CLIENT_CHARACTERISTIC_CONFIG)
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            bluetoothGatt!!.writeDescriptor(descriptor)
        }
    }


    companion object {
        val ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED"
        val ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED"
        val ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_DISCOVERED"
        val ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE"
        val EXTRA_DATA = "EXTRA_DATA"
        val UUID_HEART_RATE_MEASUREMENT: UUID = UUID.fromString(SampleGattAttributes().HEART_RATE_MEASUREMENT)

//        const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
//        const val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
//        const val ACTION_GATT_SERVICES_DISCOVERED =
//            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
//        const val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
//        const val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
    }
}