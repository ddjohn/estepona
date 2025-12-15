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
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ChargingStation
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import se.avelon.estepona.R
import se.avelon.estepona.logging.DLog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.concurrent.thread

class MyTopBarComponent {
    companion object {
        val TAG = DLog.forTag(MyTopBarComponent::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(modifier: Modifier = Modifier) {
    TopAppBar(title = {
        Row {
            Badge {
                Icon(
                    contentDescription = "Email",
                    painter = painterResource(R.drawable.logo),
                )
            }
            Text("Estepona")

            Spacer(modifier)

            BluetoothIcon()
            WifiIcon()
            ChargingIcon()
            ClockText()
        }
    })
}

@Composable
fun BluetoothIcon() {
    val context = LocalContext.current

    val bluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    var bluetooth by remember { mutableStateOf(bluetoothManager.adapter.isEnabled) }

    AnimatedVisibility(
        visible = bluetooth,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.Bluetooth,
                "Bluetooth",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
    AnimatedVisibility(
        visible = !bluetooth,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.Bluetooth,
                "Bluetooth",
                tint = Color.DarkGray,
                modifier = Modifier.size(24.dp),
            )
        }
    }

    context.registerReceiver(
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                DLog.method(MyTopBarComponent.TAG, "Bluetooth:onReceive()")
                val state =
                    intent!!.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR,
                    )
                if (state == BluetoothAdapter.STATE_ON) {
                    bluetooth = true
                } else {
                    bluetooth = false
                }
            }
        },
        IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
    )
}

@Composable
fun WifiIcon() {
    val context = LocalContext.current

    val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
    var wifi by remember { mutableStateOf(wifiManager.isWifiEnabled) }

    AnimatedVisibility(
        visible = wifi,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.Wifi,
                "Wifi",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
    AnimatedVisibility(
        visible = !wifi,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.Wifi,
                "Wifi",
                tint = Color.DarkGray,
                modifier = Modifier.size(24.dp),
            )
        }
    }

    context.registerReceiver(
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                DLog.method(MyTopBarComponent.TAG, "Wifi:onReceive()")
                val state: SupplicantState? = intent?.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)

                if (state == SupplicantState.ASSOCIATED) {
                    wifi = true
                } else {
                    wifi = false
                }
            }
        },
        IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION),
    )
}

@Composable
fun ChargingIcon() {
    val context = LocalContext.current

    val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
    var charging by remember { mutableStateOf(batteryManager.isCharging) }

    AnimatedVisibility(
        visible = charging,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.ChargingStation,
                "Charging",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
    AnimatedVisibility(
        visible = !charging,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        IconButton(onClick = { }) {
            Icon(
                Icons.Default.ChargingStation,
                "Charging",
                tint = Color.DarkGray,
                modifier = Modifier.size(24.dp),
            )
        }
    }

    context.registerReceiver(
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent?,
            ) {
                DLog.method(MyTopBarComponent.TAG, "Charging::Receive()")
                val state =
                    intent!!.getIntExtra(
                        BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_UNKNOWN,
                    )

                if (state == BatteryManager.BATTERY_STATUS_CHARGING) {
                    charging = true
                } else {
                    charging = false
                }
            }
        },
        IntentFilter(BatteryManager.ACTION_CHARGING),
    )
}

@Composable
fun ClockText() {
    var time by remember { mutableStateOf("00:00") }

    Text(text = time)

    thread(true) {
        val calendar = Calendar.getInstance()
        while (true) {
            Thread.sleep(2000)

            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            time = dateFormat.format(calendar.time)
        }
    }
}
