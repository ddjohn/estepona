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
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.avelon.estepona.logging.DLog

object MyBluetoothComponent {
    val TAG = DLog.forTag(MyBluetoothComponent::class.java)
}

@Composable
fun MyBlutooth(modifier: Modifier) {
    DLog.method(MyBluetoothComponent.TAG, "MyBluetooth")

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    Column(modifier) {
        for (device in bluetoothAdapter.bondedDevices) {
            Button(onClick = {}) {
                Text("device=${device.name}")
            }
        }

        Button(onClick = {}) {
            Text("address=${bluetoothAdapter.address}")
        }
    }
}
