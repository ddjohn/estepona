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
package se.avelon.estepona.components.vehicle

import android.bluetooth.BluetoothAdapter
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.logging.DLog

object MyStatusComponent {
    val TAG = DLog.forTag(MyStatusComponent::class.java)
}

@Composable
fun MyStatus(modifier: Modifier) {
    DLog.method(MyStatusComponent.TAG, "MyStatus()")

    val context = LocalContext.current

    val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    Column(modifier) {
        if (batteryManager.isCharging) {
            Button(onClick = {}, content = { Text("Charging") })
        } else {
            Button(onClick = {}, content = { Text("Not Charging") })
        }

        if (bluetoothAdapter.isEnabled) {
            Button(onClick = {}, content = { Text("Bluetooth Enabled") })
        } else {
            Button(onClick = {}, content = { Text("Bluetooth Disabled") })
        }
    }
}
