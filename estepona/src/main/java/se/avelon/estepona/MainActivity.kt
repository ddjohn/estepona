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

import android.app.UiModeManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import se.avelon.estepona.components.MyMapboxComponent
import se.avelon.estepona.logging.DLog

class MainActivity : ComponentActivity() {
    companion object {
        val TAG = DLog.forTag(MainActivity::class.java)
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        DLog.method(TAG, "onCreate(): $savedInstanceState")
        super.onCreate(savedInstanceState)

        DLog.test()

        DLog.info(MyMapboxComponent.TAG, "Request permissions")
        requestPermissions(arrayOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"), 666)
        DLog.info(MyMapboxComponent.TAG, "Check permissions")
        checkSelfPermission("android.permission.ACCESS_FINE_LOCATION")
        checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION")

        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)

        enableEdgeToEdge()

        DLog.info(TAG, "SetContent...")
        setContent { MyMainScreen() }
    }
}
