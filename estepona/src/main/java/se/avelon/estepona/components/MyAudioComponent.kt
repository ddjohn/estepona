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

import android.car.Car
import android.car.CarOccupantZoneManager
import android.content.Context.AUDIO_SERVICE
import android.media.AudioDeviceInfo
import android.media.AudioManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.logging.DLog

class MyAudioComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyAudioComponent::class.java)
    }

    var occupantZones: MutableList<CarOccupantZoneManager.OccupantZoneInfo>
    var ouputDevices: MutableList<AudioDeviceInfo>
    var inputDevices: MutableList<AudioDeviceInfo>

    init {
        DLog.method(TAG, "init()")

        val context = MainApplication.getApplication().applicationContext

        val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        val car = Car.createCar(context)
        val occupantZoneManager =
            car.getCarManager(Car.CAR_OCCUPANT_ZONE_SERVICE) as CarOccupantZoneManager

        inputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS).toMutableList()
        ouputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).toMutableList()
        occupantZones = occupantZoneManager.allOccupantZones.toMutableList()
    }
}

fun AudioDeviceInfo.text(): String = "ddddd $this"

fun CarOccupantZoneManager.OccupantZoneInfo.text(): String =
    "eeeee ${this.zoneId} ${this.occupantType}"

@Composable
fun MyAudio(modifier: Modifier, viewModel: MyAudioComponent = viewModel()) {
    DLog.method(MyAudioComponent.TAG, "MyAudio()")

    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            MyDropMenu("Audio Input Devices:", viewModel.inputDevices.toList())
            MyDropMenu("Audio output Devices:", viewModel.ouputDevices.toList())
            MyDropMenu("Occupancy Zones:", viewModel.occupantZones.toList())

            for (device in viewModel.inputDevices.toList()) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = {
                        Text("${device.id} - ${device.address} (${deviceType(device.type)})")
                    },
                )
                DLog.info(MyAudioComponent.TAG, "---")
                DLog.info(MyAudioComponent.TAG, "input device: ${device.id}")
                DLog.info(MyAudioComponent.TAG, "input device: ${device.type}")
                DLog.info(MyAudioComponent.TAG, "input device: ${device.isSink}")
                DLog.info(MyAudioComponent.TAG, "input device: ${device.isSource}")
                DLog.info(MyAudioComponent.TAG, "input device: ${device.address}")
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            for (device in viewModel.ouputDevices.toList()) {
                val occupancyType = occupancyType(device.type)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = {
                        Text("${device.id} - ${device.address} (${deviceType(device.type)})")
                    },
                )
                DLog.info(MyAudioComponent.TAG, "---")
                DLog.info(MyAudioComponent.TAG, "output device: ${device.id}")
                DLog.info(MyAudioComponent.TAG, "output device: ${device.type}")
                DLog.info(MyAudioComponent.TAG, "output device: ${device.isSink}")
                DLog.info(MyAudioComponent.TAG, "output device: ${device.isSource}")
                DLog.info(MyAudioComponent.TAG, "output device: ${device.address}")
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            for (zone in viewModel.occupantZones.toList()) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    content = {
                        Text("${zone.zoneId} - $zone (${occupancyType(zone.occupantType)})")
                    },
                )
                DLog.info(MyAudioComponent.TAG, "---")
                DLog.info(MyAudioComponent.TAG, "zone: ${zone.zoneId}")
                DLog.info(MyAudioComponent.TAG, "zone: ${zone.occupantType}")
                DLog.info(MyAudioComponent.TAG, "zone: $zone")
                DLog.info(MyAudioComponent.TAG, "zone: $zone")
                DLog.info(MyAudioComponent.TAG, "zone: $zone")
                DLog.info(MyAudioComponent.TAG, "zone: $zone")
            }
        }
    }
}

fun deviceType(type: Int): String =
    when (type) {
        15 -> "TYPE_LINE_DIGITAL"
        16 -> "TYPE_FM"
        21 -> "TYPE_BUILTIN_MIC"
        25 -> "TYPE_REMOTE_SUBMIX"
        28 -> "TYPE_ECHO_REFERENCE"
        else -> "Not defined"
    }

fun occupancyType(type: Int): String =
    when (type) {
        0 -> "OCCUPANT_TYPE_DRIVER"
        1 -> "OCCUPANT_TYPE_FRONT_PASSENGER"
        2 -> "OCCUPANT_TYPE_REAR_PASSENGER"
        else -> "Not defined"
    }
