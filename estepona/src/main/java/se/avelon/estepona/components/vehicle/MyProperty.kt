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
package se.avelon.estepona.components.vehicle

import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue

class MyProperty<T>(val value: CarPropertyValue<T>) {

    fun getPropertyId(): Int {
        return value.propertyId
    }

    override fun toString(): String {
        val v = when (value.value) {
            is Boolean -> "${value.value} (bool)"
            is Int -> "${value.value} (int)"
            is Float -> "${value.value} (float)"
            else -> "${value.value} (some kind of array)"
        }

        val area = when (value.areaId) {
            0 -> { "global" }
            else -> { "unknown" }
        }
        return "${value.propertyId} [area=${value.areaId}] ${VehiclePropertyIds.toString(value.propertyId)} = $v"
    }
}
