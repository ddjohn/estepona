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

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.logging.DLog

object MySensorComponent {
    val TAG = DLog.forTag(MySensorComponent::class.java)
}

@Composable
fun MySensor(modifier: Modifier) {
    DLog.method(MySensorComponent.TAG, "MySensor()")

    val context = LocalContext.current

    val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

    Column(modifier) {
        for (sensor in sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            Button(onClick = {}) {
                Text("sensor=${sensor.name} (${sensor.type}, ${sensor.vendor})")
            }

            sensorManager.registerListener(
                object : SensorEventListener {
                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                        DLog.method(MySensorComponent.TAG, "onAccuracyChanged(): $sensor, $accuracy")
                    }

                    override fun onSensorChanged(event: SensorEvent?) {
                        DLog.method(MySensorComponent.TAG, "onSensorChanged(): ${event?.sensor?.name}, ${event?.accuracy}, ${event?.values}")
                    }
                },
                sensor,
                SensorManager.SENSOR_DELAY_UI,
            )
        }
    }
}
