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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.StringTokenizer
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

class MySensorComponent :
    ViewModel(), SensorEventListener, OnNmeaMessageListener, LocationListener {
    companion object {
        val TAG = DLog.forTag(MySensorComponent::class.java)
    }

    private val _mutableGyroscopeValue = mutableStateOf("")
    val mutableGyroscopeValue: MutableState<String> = _mutableGyroscopeValue
    private val _mutableAccelerometerValue = mutableStateOf("")
    val mutableAccelerometerValue: MutableState<String> = _mutableAccelerometerValue
    private val _mutableRotationVectorValue = mutableStateOf("")
    val mutableRotationVectorValue: MutableState<String> = _mutableRotationVectorValue
    private val _mutableGyroscopeLimitedAxesValue = mutableStateOf("")
    val mutableGyroscopeLimitedAxesValue: MutableState<String> = _mutableGyroscopeLimitedAxesValue
    private val _mutableAccelerometerLimitedAxesValue = mutableStateOf("")
    val mutableAccelerometerLimitedAxesValue: MutableState<String> =
        _mutableAccelerometerLimitedAxesValue
    private val _mutableGameRotationVectorValue = mutableStateOf("")
    val mutableGameRotationVectorValue: MutableState<String> = _mutableGameRotationVectorValue
    private val _mutableGravityValue = mutableStateOf("")
    val mutableGravityValue: MutableState<String> = _mutableGravityValue
    private val _mutableLinearAccelerationValue = mutableStateOf("")
    val mutableLinearAccelerationValue: MutableState<String> = _mutableLinearAccelerationValue

    private val _mutableLocationValue = mutableStateOf("")
    val mutableLocationValue: MutableState<String> = _mutableLocationValue

    private val _mutableGNGLLValue = mutableStateOf("")
    val mutableGNGLLValue: MutableState<String> = _mutableGNGLLValue
    private val _mutableGNRMCValue = mutableStateOf("")
    val mutableGNRMCValue: MutableState<String> = _mutableGNRMCValue
    private val _mutableGNGSAValue = mutableStateOf("")
    val mutableGNGSAValue: MutableState<String> = _mutableGNGSAValue
    private val _mutableGLGSVValue = mutableStateOf("")
    val mutableGLGSVValue: MutableState<String> = _mutableGLGSVValue
    private val _mutableGAGSVValue = mutableStateOf("")
    val mutableGAGSVValue: MutableState<String> = _mutableGAGSVValue

    fun init(context: Context) {
        DLog.method(TAG, "init()")
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
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
                mutableGyroscopeValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_ACCELEROMETER -> {
                mutableAccelerometerValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_ROTATION_VECTOR -> {
                mutableRotationVectorValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_ACCELEROMETER_LIMITED_AXES -> {
                mutableAccelerometerLimitedAxesValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_GYROSCOPE_LIMITED_AXES -> {
                mutableGyroscopeLimitedAxesValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_GAME_ROTATION_VECTOR -> {
                mutableGameRotationVectorValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_GRAVITY -> {
                mutableGravityValue.value =
                    "${event.values[0]}, ${event.values[1]}, ${event.values[2]}"
            }
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                mutableLinearAccelerationValue.value =
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
            mutableGNGLLValue.value = "$message,"
        } else if (type == "${'$'}GNRMC") {
            mutableGNRMCValue.value = "$message,"
        } else if (type == "${'$'}GNGSA") {
            mutableGNGSAValue.value = "$message,"
        } else if (type == "${'$'}GLGSV") {
            mutableGLGSVValue.value = "$message,"
        } else if (type == "${'$'}GAGSV") {
            mutableGAGSVValue.value = "$message,"
        }
    }

    override fun onLocationChanged(location: Location) {
        mutableLocationValue.value = "$location"
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
            MyText(Modifier, "Rotation Vector: ${viewModel.mutableRotationVectorValue.value}")
            MyText(
                Modifier,
                "Accelerometer Limit Axes: ${viewModel.mutableAccelerometerLimitedAxesValue.value}",
            )
            MyText(
                Modifier,
                "Gyroscope Limit xes: ${viewModel.mutableGyroscopeLimitedAxesValue.value}",
            )
            MyText(
                Modifier,
                "Game Rotation Vector: ${viewModel.mutableGameRotationVectorValue.value}",
            )
            MyText(Modifier, "Gravity: ${viewModel.mutableGravityValue.value}")
            MyText(
                Modifier,
                "Linear Acceleration: ${viewModel.mutableLinearAccelerationValue.value}",
            )
        }
        Column {
            MyText(Modifier, "Location: ${viewModel.mutableLocationValue.value}")

            MyText(Modifier, "GNGLL: ${viewModel.mutableGNGLLValue.value}")
            MyText(Modifier, "GNRMC: ${viewModel.mutableGNRMCValue.value}")
            MyText(Modifier, "GNGSA: ${viewModel.mutableGNGSAValue.value}")
            MyText(Modifier, "GLGSV: ${viewModel.mutableGLGSVValue.value}")
            MyText(Modifier, "GAGSV: ${viewModel.mutableGAGSVValue.value}")
        }
    }
}
