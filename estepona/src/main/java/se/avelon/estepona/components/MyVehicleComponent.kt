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
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.io.PrintWriter
import java.util.Calendar
import se.avelon.estepona.components.vehicle.MyProperty
import se.avelon.estepona.logging.DLog

class MyVehicleComponent : ViewModel(), CarPropertyManager.CarPropertyEventCallback {
    companion object {
        val TAG = DLog.forTag(MyVehicleComponent::class.java)
    }

    lateinit var printWriter: PrintWriter
    private val listItems = arrayListOf<MyProperty<*>>()
    private lateinit var arrayAdapter: ArrayAdapter<MyProperty<*>>

    fun init(context: Context) {
        DLog.method(TAG, "init()")

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
            Toast.makeText(context, "No auto feature", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File("${context.dataDir}/vehicle_${Calendar.getInstance().timeInMillis}.log")
        DLog.info(TAG, "Logging to $file")
        file.parentFile?.mkdirs()
        printWriter = PrintWriter(file)

        val car = Car.createCar(context)
        val mgr = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

        for (prop in mgr.propertyList) {
            DLog.info(TAG, "prop=$prop")

            try {
                mgr.registerCallback(this, prop.propertyId, CarPropertyManager.SENSOR_RATE_UI)
                mgr.registerCallback(this, prop.propertyId, CarPropertyManager.SENSOR_RATE_ONCHANGE)
            } catch (e: SecurityException) {
                DLog.info(TAG, "Failed to subscribe to permission or wrong type: " + e.message)
            }
        }

        /*

        for (prop in mgr.propertyList) {
            DLog.info(TAG, "prop=$prop")

            if (VehiclePropertyIds.PARKING_BRAKE_ON == prop.propertyId) {
                try {
                    val obj = mgr.getProperty<Object>(prop.propertyId, 0)
                    DLog.error(TAG, "obj=$obj")
                } catch (e: CarInternalErrorException) {
                    DLog.exception(TAG, "exception", e)
                }
            }

            try {
                mgr.registerSupportedValuesChangeCallback(
                    prop.propertyId,
                    object : CarPropertyManager.SupportedValuesChangeCallback {
                        override fun onSupportedValuesChange(p0: Int, p1: Int) {
                            DLog.method(TAG, "onSupportedValuesChange(): $p0, $p1")
                        }
                    },
                )

                mgr.registerCallback(this, prop.propertyId, CarPropertyManager.SENSOR_RATE_UI)
                mgr.registerCallback(this, prop.propertyId, CarPropertyManager.SENSOR_RATE_ONCHANGE)
            } catch (e: SecurityException) {
                DLog.exception(TAG, "exception", e)
            }
        }
         */
    }

    override fun onChangeEvent(propertyValue: CarPropertyValue<*>?) {
        DLog.method(TAG, "onChangeEvent(): $propertyValue")
        printWriter.println("onChangeEvent(): $propertyValue")
        /*
        if (propertyValue?.propertyId == null) {
            return
        }

        for (prop in listItems) {
            if (prop.getPropertyId() == propertyValue.propertyId) {
                listItems.remove(prop)
                break
            }
        }

        listItems.addFirst(MyProperty(propertyValue))
        arrayAdapter.notifyDataSetChanged()
         */
    }

    override fun onErrorEvent(propertyValue: Int, p1: Int) {
        DLog.error(TAG, "onErrorEvent:$propertyValue")
    }
}

@Composable
fun MyVehicle(modifier: Modifier, viewModel: MyVehicleComponent = viewModel()) {
    DLog.method(MyVehicleComponent.TAG, "MyVehicle(): $modifier")

    val context = LocalContext.current
    viewModel.init(context)
}
