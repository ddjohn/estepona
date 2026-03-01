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

import android.app.usage.StorageStatsManager
import android.car.Car
import android.car.watchdog.CarWatchdogManager
import android.car.watchdog.ResourceOveruseStats
import android.content.Context.STORAGE_SERVICE
import android.content.Context.STORAGE_STATS_SERVICE
import android.os.storage.StorageManager
import android.os.storage.StorageManager.StorageVolumeCallback
import android.os.storage.StorageVolume
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

class MyPerformanceComponents : ViewModel(), CarWatchdogManager.ResourceOveruseListener {
    companion object {
        val TAG = DLog.forTag(MyPerformanceComponents::class.java)
    }

    val overUse = mutableStateOf("")
    val storageVolume = mutableStateOf("")

    init {
        DLog.method(TAG, "init()")
        val context = MainApplication.getApplication().applicationContext

        val car = Car.createCar(context)
        val watchdogManager = car.getCarManager(Car.CAR_WATCHDOG_SERVICE) as CarWatchdogManager

        val storageManager = context.getSystemService(STORAGE_SERVICE) as StorageManager
        val storageStatsManager =
            context.getSystemService(STORAGE_STATS_SERVICE) as StorageStatsManager

        watchdogManager.addResourceOveruseListener(
            context.mainExecutor,
            CarWatchdogManager.FLAG_RESOURCE_OVERUSE_IO,
            this,
        )
        overUse.value =
            watchdogManager
                .getResourceOveruseStats(CarWatchdogManager.FLAG_RESOURCE_OVERUSE_IO, 1)
                .toString()

        storageManager.registerStorageVolumeCallback(
            context.mainExecutor,
            object : StorageVolumeCallback() {
                override fun onStateChanged(volume: StorageVolume) {
                    DLog.method(TAG, "onStateChanged(): $volume")
                    super.onStateChanged(volume)
                    storageVolume.value = volume.toString()
                }
            },
        )
    }

    override fun onOveruse(p0: ResourceOveruseStats?) {
        DLog.method(TAG, "onOveruse(): $p0")
        overUse.value = p0.toString()
    }
}

@Composable
fun MyPerformance(modifier: Modifier, viewModel: MyPerformanceComponents = viewModel()) {
    DLog.method(MyTemplateComponents.TAG, "MyPerformance()")

    val context = LocalContext.current
    Column {
        MyText(modifier, "overUser=${viewModel.overUse.value}")
        MyText(modifier, "volume=${viewModel.storageVolume.value}")
    }
}
