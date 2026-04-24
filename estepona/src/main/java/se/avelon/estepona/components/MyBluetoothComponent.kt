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
package se.avelon.estepona.components

import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.CleanText
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.logging.DLog

class MyBluetoothComponent: ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyBluetoothComponent::class.java)
    }

    init {
        val context = MainApplication.getApplication().applicationContext
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun MyBlutooth(modifier: Modifier) {
    DLog.method(MyBluetoothComponent.TAG, "MyBluetooth")

    val context = LocalContext.current
    if(context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) == false) {
        BasicAlertDialog(
            onDismissRequest = { },
            content = { CleanText("Bluetooth not available!") }
        )
        return
    }

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    Row(modifier) {
        Column  {
            MyDropMenu("Bounded Devices", arrayOf(bluetoothAdapter.bondedDevices))

            for (device in bluetoothAdapter.bondedDevices) {
                Button(onClick = {}) { Text("device=${device.name}") }
            }
        }

        Column {
            Text("address=${bluetoothAdapter.address}")
            Text("name=${bluetoothAdapter.name}")
            Text("state=${bluetoothAdapter.state}")
            Text("enabled=${bluetoothAdapter.isEnabled}")
            Text("disccovering=${bluetoothAdapter.isDiscovering}")
            Text("state=${bluetoothAdapter.state}")
        }
    }
}
