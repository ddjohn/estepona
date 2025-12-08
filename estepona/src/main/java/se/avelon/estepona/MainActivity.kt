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
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import se.avelon.estepona.components.MyNavigationComponent
import se.avelon.estepona.permission.MyPermissions
import se.avelon.estepona.logging.DLog

class MainActivity : ComponentActivity() {
    companion object {
        val TAG = DLog.forTag(MainActivity::class.java)
    }

    lateinit var permissions: MyPermissions

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        DLog.method(TAG, "onCreate(): $savedInstanceState")
        super.onCreate(savedInstanceState)

        DLog.test()

        permissions = MyPermissions(this)

        DLog.info(MyNavigationComponent.TAG, "Request permissions")
        permissions.request()

        DLog.info(MyNavigationComponent.TAG, "Check permissions")
        permissions.check()

        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)

        enableEdgeToEdge()

        DLog.info(TAG, "SetContent...")
        setContent { MyMainScreen() }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        DLog.method(TAG, "onActivityResult(): $resultCode, $data")
        super.onActivityReenter(resultCode, data)

        permissions.onActivityResult(resultCode, data)
    }
}
