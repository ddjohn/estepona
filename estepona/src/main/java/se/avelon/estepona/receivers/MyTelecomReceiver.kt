/*
 * Copyright 2025 David Johansson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.avelon.estepona.receivers

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import se.avelon.estepona.logging.DLog

class MyTelecomReceiver : BroadcastReceiver() {
    companion object {
        val TAG = DLog.forTag(MyTelecomReceiver::class.java)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onReceive(context: Context, intent: Intent) {
        DLog.method(TAG, "onReceive(): ${intent}")
        DLog.method(TAG, "onReceive(): ${intent.extras}")

        if(intent.action == BluetoothDevice.ACTION_FOUND) {
            DLog.error(TAG, "" +  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
            DLog.error(TAG, "" +  intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS))
            DLog.error(TAG, "" + intent.extras?.get(BluetoothDevice.EXTRA_NAME))
//            DLog.error(TAG, "" +  intent.getParcelableExtra(BluetoothDevice.EXTRA_RSSI))
 //           DLog.error(TAG, "" +  intent.getParcelableExtra(BluetoothDevice.EXTRA_IS_COORDINATED_SET_MEMBER))
        }
        if(intent.extras?.getString(BluetoothDevice.EXTRA_NAME) == "Davids S22") {
            DLog.info(TAG, "bind...")
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            val method = device?.javaClass?.getMethod("createBond" )
            method?.invoke(device)

            device?.fetchUuidsWithSdp()
        }
    }
}
