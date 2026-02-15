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
package se.avelon.estepona.system

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Context.DISPLAY_SERVICE
import android.content.Context.USER_SERVICE
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.hardware.display.DisplayManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.UserManager
import android.view.Surface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ChargingStation
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.concurrent.thread
import se.avelon.estepona.R
import se.avelon.estepona.logging.DLog

class MyTopBarComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyTopBarComponent::class.java)
    }

    val wifi: MutableState<Boolean> = mutableStateOf(false)
    val ssid: MutableState<String> = mutableStateOf("")
    val time: MutableState<String> = mutableStateOf("")
    val charging: MutableState<Boolean> = mutableStateOf(false)
    val percent: MutableState<String> = mutableStateOf("")
    val bluetooth: MutableState<Boolean> = mutableStateOf(false)
    val user: MutableState<String> = mutableStateOf("")

    fun init(context: Context) {
        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        wifi.value = false
        ssid.value = wifiManager.connectionInfo.ssid

        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    DLog.method(TAG, "Wifi::onReceive()")
                    DLog.bundle(TAG, intent?.extras)

                    val wifiState =
                        intent!!.getIntExtra(
                            WifiManager.EXTRA_WIFI_STATE,
                            WifiManager.WIFI_STATE_UNKNOWN,
                        )

                    wifi.value = wifiState == WifiManager.WIFI_STATE_ENABLED
                    ssid.value = wifiManager.connectionInfo.ssid
                }
            },
            IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION),
        )

        val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
        charging.value = batteryManager.isCharging
        percent.value = "" + batteryManager.computeChargeTimeRemaining()
        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    DLog.method(TAG, "Charging::Receive()")
                    DLog.bundle(TAG, intent?.extras)

                    val state =
                        intent!!.getIntExtra(
                            BatteryManager.EXTRA_STATUS,
                            BatteryManager.BATTERY_STATUS_UNKNOWN,
                        )

                    charging.value = state == BatteryManager.BATTERY_STATUS_CHARGING
                    percent.value = "" + batteryManager.computeChargeTimeRemaining()
                }
            },
            IntentFilter(BatteryManager.ACTION_CHARGING),
        )

        val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetooth.value = bluetoothManager.adapter.isEnabled
        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    DLog.method(TAG, "Bluetooth:onReceive()")
                    val state =
                        intent!!.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

                    bluetooth.value = state == BluetoothAdapter.STATE_ON
                }
            },
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
        )

        val userManager = context.getSystemService(USER_SERVICE) as UserManager
        user.value = userManager.userName

        thread(true) {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            while (true) {
                //DLog.method(TAG, "Time::thread()")
                val calendar = Calendar.getInstance()
                time.value = dateFormat.format(calendar.time)

                Thread.sleep(2000)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(modifier: Modifier = Modifier, viewModel: MyTopBarComponent = viewModel()) {
    DLog.method(MyTopBarComponent.TAG, "MyTopBar()")

    viewModel.init(LocalContext.current)

    TopAppBar(
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        title = {
            Row {
                Badge {
                    Icon(contentDescription = "Email", painter = painterResource(R.drawable.logo))
                    Text(" ")
                    Text("Estepona")
                }

                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    Icons.Default.Wifi,
                    "Wifi",
                    tint = if (viewModel.wifi.value) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp),
                )
                Text(" ")
                Text(viewModel.ssid.value)

                Spacer(modifier = Modifier.width(32.dp))
                Icon(
                    Icons.Default.Bluetooth,
                    "Bluetooth",
                    tint = if (viewModel.bluetooth.value) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(modifier = Modifier.width(32.dp))
                Icon(
                    Icons.Default.ChargingStation,
                    "Charging",
                    tint = if (viewModel.charging.value) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp),
                )
                Text(" ")
                Text(viewModel.percent.value)

                Spacer(modifier = Modifier.width(32.dp))
                Orientation()

                Spacer(modifier = Modifier.width(32.dp))
                Text(viewModel.user.value)
                Text("Version 1.15")

                Spacer(Modifier.weight(2f, true))
                Text(viewModel.time.value)
                Text(" ")
            }
        },
    )
}

@Composable
fun Orientation() {
    var orientation by remember { mutableStateOf("unknown") }

    val context = LocalContext.current

    val displayManager = context.getSystemService(DISPLAY_SERVICE) as DisplayManager
    val rotation = displayManager.getDisplay(0).orientation
    orientation =
        when (rotation) {
            Surface.ROTATION_0 -> "Portrait"
            Surface.ROTATION_90 -> "Landscape"
            Surface.ROTATION_180 -> "Reverse portrait"
            else -> "Reverse landscape"
        }
    DLog.info(MyTopBarComponent.TAG, "orientation=$orientation")

    Text(text = orientation)
}
