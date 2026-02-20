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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.OnNmeaMessageListener
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.StringTokenizer
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

@SuppressLint("MissingPermission")
class MySensorComponent :
    ViewModel(), SensorEventListener, OnNmeaMessageListener, LocationListener {
    companion object {
        val TAG = DLog.forTag(MySensorComponent::class.java)
    }

    var sensorList: MutableList<Sensor>
    val gyroscope: MutableState<String> = mutableStateOf("")
    val accelerometer: MutableState<String> = mutableStateOf("")
    val rotationVector: MutableState<String> = mutableStateOf("")
    val gyroscopeLimitedAxes: MutableState<String> = mutableStateOf("")
    val accelerometerLimitedAxes: MutableState<String> = mutableStateOf("")
    val gameRotationVector: MutableState<String> = mutableStateOf("")
    val gravity: MutableState<String> = mutableStateOf("")
    val linearAcceleration: MutableState<String> = mutableStateOf("")

    val location: MutableState<String> = mutableStateOf("")

    val gngll: MutableState<String> = mutableStateOf("")
    val gnrmc: MutableState<String> = mutableStateOf("")
    val gngsa: MutableState<String> = mutableStateOf("")
    val glgsv: MutableState<String> = mutableStateOf("")
    val gagsv = mutableStateOf("")

    init {
        DLog.method(TAG, "init()")
        val context = MainApplication.getApplication().applicationContext

        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL).toMutableList()
        for (sensor in sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW)
        }

        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.addNmeaListener(context.mainExecutor, this)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1f, this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        DLog.method(TAG, "onAccuracyChanged(): $sensor, $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                gyroscope.value = "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_ACCELEROMETER -> {
                accelerometer.value = "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_ROTATION_VECTOR -> {
                rotationVector.value = "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_ACCELEROMETER_LIMITED_AXES -> {
                accelerometerLimitedAxes.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_GYROSCOPE_LIMITED_AXES -> {
                gyroscopeLimitedAxes.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_GAME_ROTATION_VECTOR -> {
                gameRotationVector.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_GRAVITY -> {
                gravity.value = "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                linearAcceleration.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }

            else -> {
                DLog.method(
                    TAG,
                    "onSensorChanged(): ${event?.sensor?.name}, ${event?.accuracy}, ${event?.values?.size}",
                )
            }
        }
    }

    override fun onNmeaMessage(message: String?, timestamp: Long) {
        val tokenizer = StringTokenizer(message, ",")
        val type = tokenizer.nextElement()

        if (type == "${'$'}GNGLL") {
            gngll.value = "$message,"
        } else if (type == "${'$'}GNRMC") {
            gnrmc.value = "$message,"
        } else if (type == "${'$'}GNGSA") {
            gngsa.value = "$message,"
        } else if (type == "${'$'}GLGSV") {
            glgsv.value = "$message,"
        } else if (type == "${'$'}GAGSV") {
            gagsv.value = "$message,"
        }
    }

    override fun onLocationChanged(loc: Location) {
        location.value = "$loc"
    }
}

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@Composable
fun MySensor(modifier: Modifier, viewModel: MySensorComponent = viewModel()) {
    DLog.method(MySensorComponent.TAG, "MySensor()")

    Row {
        Column { MyDropMenu("Sensors", viewModel.sensorList.toList()) }
        Column {
            MyText(Modifier, "Gyroscope: ${viewModel.gyroscope.value}")
            MyText(Modifier, "Accelerometer: ${viewModel.accelerometer.value}")
            MyText(Modifier, "Rotation Vector: ${viewModel.rotationVector.value}")
            MyText(
                Modifier,
                "Accelerometer Limit Axes: ${viewModel.accelerometerLimitedAxes.value}",
            )
            MyText(Modifier, "Gyroscope Limit xes: ${viewModel.gyroscopeLimitedAxes.value}")
            MyText(Modifier, "Game Rotation Vector: ${viewModel.gameRotationVector.value}")
            MyText(Modifier, "Gravity: ${viewModel.gravity.value}")
            MyText(Modifier, "Linear Acceleration: ${viewModel.linearAcceleration.value}")
        }
        Column {
            MyText(Modifier, "Location: ${viewModel.location.value}")

            MyText(Modifier, "GNGLL: ${viewModel.gngll.value}")
            MyText(Modifier, "GNRMC: ${viewModel.gnrmc.value}")
            MyText(Modifier, "GNGSA: ${viewModel.gngsa.value}")
            MyText(Modifier, "GLGSV: ${viewModel.glgsv.value}")
            MyText(Modifier, "GAGSV: ${viewModel.gagsv.value}")
        }
    }
}
