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

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

class MySensorComponent : ViewModel(), SensorEventListener {
    companion object {
        val TAG = DLog.forTag(MySensorComponent::class.java)
    }

    private val _mutableGyroscopeValue = mutableStateOf("")
    val mutableGyroscopeValue: MutableState<String> = _mutableGyroscopeValue
    private val _mutableAccelerometerValue = mutableStateOf("")
    val mutableAccelerometerValue: MutableState<String> = _mutableAccelerometerValue

    fun init(context: Context) {
        DLog.method(TAG, "init()")
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        for (sensor in sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        DLog.method(TAG, "onAccuracyChanged(): $sensor, $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                mutableGyroscopeValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_ACCELEROMETER -> {
                mutableAccelerometerValue.value =
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
}

@Composable
fun MySensor(modifier: Modifier, viewModel: MySensorComponent = viewModel()) {
    DLog.method(MySensorComponent.TAG, "MySensor()")

    val context = LocalContext.current

    viewModel.init(context)

    val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

    Row {
        Column { MyDropMenu("Sensors", sensorManager.getSensorList(Sensor.TYPE_ALL)) }
        Column {
            MyText(Modifier, "Gyroscope: ${viewModel.mutableGyroscopeValue.value}")
            MyText(Modifier, "Accelerometer: ${viewModel.mutableAccelerometerValue.value}")
        }
    }
}
