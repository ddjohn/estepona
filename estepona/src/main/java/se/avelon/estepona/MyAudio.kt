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
package se.avelon.estepona

import android.car.Car
import android.car.CarOccupantZoneManager
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.logging.DLog

object MyAudio {
    val TAG = DLog.forTag(MyAudio::class.java)
}

@Composable
fun MyAudio(modifier: Modifier) {
    DLog.method(MyAudio.TAG, "MyAudio()")

    val context = LocalContext.current

    val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    val car = Car.createCar(context)
    val occupantZoneManager = car.getCarManager(Car.CAR_OCCUPANT_ZONE_SERVICE) as CarOccupantZoneManager

    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            for (device in audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = { Text("${device.id} - ${device.address} (${deviceType(device.type)})") },
                )
                DLog.info(MyAudio.TAG, "---")
                DLog.info(MyAudio.TAG, "input device: ${device.id}")
                DLog.info(MyAudio.TAG, "input device: ${device.type}")
                DLog.info(MyAudio.TAG, "input device: ${device.isSink}")
                DLog.info(MyAudio.TAG, "input device: ${device.isSource}")
                DLog.info(MyAudio.TAG, "input device: ${device.address}")
            }

            for (device in audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = { Text("${device.id} - ${device.address} (${deviceType(device.type)})") },
                )
                DLog.info(MyAudio.TAG, "---")
                DLog.info(MyAudio.TAG, "input device: ${device.id}")
                DLog.info(MyAudio.TAG, "input device: ${device.type}")
                DLog.info(MyAudio.TAG, "input device: ${device.isSink}")
                DLog.info(MyAudio.TAG, "input device: ${device.isSource}")
                DLog.info(MyAudio.TAG, "input device: ${device.address}")
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            for (device in audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)) {
                val occupancyType = occupancyType(device.type)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = { Text("${device.id} - ${device.address} (${deviceType(device.type)})") },
                )
                DLog.info(MyAudio.TAG, "---")
                DLog.info(MyAudio.TAG, "output device: ${device.id}")
                DLog.info(MyAudio.TAG, "output device: ${device.type}")
                DLog.info(MyAudio.TAG, "output device: ${device.isSink}")
                DLog.info(MyAudio.TAG, "output device: ${device.isSource}")
                DLog.info(MyAudio.TAG, "output device: ${device.address}")
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            for (zone in occupantZoneManager.allOccupantZones) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = { Text("${zone.zoneId} - $zone (${occupancyType(zone.occupantType)})") },
                )
                DLog.info(MyAudio.TAG, "---")
                DLog.info(MyAudio.TAG, "zone: ${zone.zoneId}")
                DLog.info(MyAudio.TAG, "zone: ${zone.occupantType}")
                DLog.info(MyAudio.TAG, "zone: $zone")
                DLog.info(MyAudio.TAG, "zone: $zone")
                DLog.info(MyAudio.TAG, "zone: $zone")
                DLog.info(MyAudio.TAG, "zone: $zone")
            }
        }
    }
}

fun deviceType(type: Int): String {
    return when (type) {
        15 -> "TYPE_LINE_DIGITAL"
        16 -> "TYPE_FM"
        21 -> "TYPE_BUILTIN_MIC"
        25 -> "TYPE_REMOTE_SUBMIX"
        28 -> "TYPE_ECHO_REFERENCE"
        else -> "Not defined"
    }
}

fun occupancyType(type: Int): String {
    return when (type) {
        0 -> "OCCUPANT_TYPE_DRIVER"
        1 -> "OCCUPANT_TYPE_FRONT_PASSENGER"
        2 -> "OCCUPANT_TYPE_REAR_PASSENGER"
        else -> "Not defined"
    }
}
