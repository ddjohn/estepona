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
package se.avelon.estepona.os.permission

import android.Manifest
import android.app.Activity
import android.car.Car
import android.content.Intent
import android.content.pm.PackageManager
import se.avelon.estepona.logging.DLog

class MyPermissions(val activity: Activity) {
    companion object {
        val TAG = DLog.forTag(MyPermissions::class.java)

        const val REQUEST_CODE: Int = 666
        private val permissions =
            arrayOf<String?>(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.CAMERA,
                "android.permission.LOCAL_MAC_ADDRESS",
                // Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.BIND_ACCESSIBILITY_SERVICE,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.POST_NOTIFICATIONS,
                "android.permission.INJECT_EVENTS", // Manifest.permission.INJECT_EVENTS,
                Manifest.permission.MEDIA_CONTENT_CONTROL,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.WRITE_SETTINGS,
                Car.PERMISSION_ENERGY,
                Car.PERMISSION_SPEED,
                // Car.PERMISSION_VENDOR_EXTENSION,
            )
    }

    fun request() {
        DLog.method(TAG, "request(): $permissions")
        activity.requestPermissions(permissions, REQUEST_CODE)
    }

    fun check(): Boolean {
        DLog.method(TAG, "check()")
        for (permission in permissions) {
            DLog.info(TAG, "Checking permission: $permission")
            if (activity.checkSelfPermission(permission!!) != PackageManager.PERMISSION_GRANTED) {
                DLog.error(TAG, "Permission not approved: $permission")
                // return false
            }
        }
        return true
    }

    fun onActivityResult(resultCode: Int, data: Intent?) {
        DLog.method(TAG, "onActivityResult(): $resultCode, $data")
    }
}
