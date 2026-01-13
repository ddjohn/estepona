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

import android.os.Build
import android.os.Environment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.components.MyStatusComponent.TAG
import se.avelon.estepona.compose.MyButton
import se.avelon.estepona.logging.DLog

object MyStatusComponent {
    val TAG = DLog.forTag(MyStatusComponent::class.java)
}

@Composable
fun MyStatus(modifier: Modifier) {
    DLog.method(MyStatusComponent.TAG, "MyStatus()")

    val context = LocalContext.current

    Column(modifier.drawBehind { drawRect(Color.Black) }) {
        var myEnvironment by remember { mutableStateOf(false) }
        var myBuild by remember { mutableStateOf(false) }

        Row {
            MyButton(Modifier, "Environment") { myEnvironment = !myEnvironment }
            MyButton(Modifier, "Build") { myBuild = !myBuild }
        }
        AnimatedVisibility(
            visible = myEnvironment,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            val environments =
                arrayOf(
                    Environment.getExternalStorageState(),
                    Environment.DIRECTORY_DOCUMENTS,
                    Environment.getRootDirectory(),
                    Environment.getDownloadCacheDirectory(),
                    Environment.getDataDirectory(),
                )

            Column {
                Text("getExternalStorageDirectory=${Environment.getExternalStorageDirectory()}")
                Text("getDownloadCacheDirectory=${Environment.getDownloadCacheDirectory()}")
                Text("getStorageDirectory=${Environment.getStorageDirectory()}")
                Text("getRootDirectory=${Environment.getRootDirectory()}")
                Text("getDataDirectory=${Environment.getDataDirectory()}")
                Text("getDownloadCacheDirectory=${Environment.getDownloadCacheDirectory()}")
                Text("getExternalStorageState=${Environment.getExternalStorageState()}")
                Text("DIRECTORY_DOCUMENTS=${Environment.DIRECTORY_DOCUMENTS}")
                Text("DIRECTORY_DCIM=${Environment.DIRECTORY_DCIM}")
                Text("MEDIA_SHARED=${Environment.MEDIA_SHARED}")
                Text("MEDIA_MOUNTED=${Environment.MEDIA_MOUNTED}")
            }
        }

        AnimatedVisibility(
            visible = myBuild,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            Column {
                Text("MANUFACTURER=${Build.MANUFACTURER}")
                Text("DEVICE=${Build.DEVICE}")
                Text("MODEL=${Build.MODEL}")
                Text("BOARD=${Build.BOARD}")
                Text("BOOTLOADER=${Build.BOOTLOADER}")
                Text("BRAND=${Build.BRAND}")
                Text("DISPLAY=${Build.DISPLAY}")
                Text("FINGERPRINT=${Build.FINGERPRINT}")
                Text("HARDWARE=${Build.HARDWARE}")
                Text("HOST=${Build.HOST}")
                Text("ID=${Build.ID}")
                Text("PRODUCT=${Build.PRODUCT}")
                Text("TAGS=${Build.TAGS}")
                Text("TYPE=${Build.TYPE}")
                Text("USER=${Build.USER}")
                Text("TYPE=${Build.TYPE}")
                Text("RELEASE=${Build.VERSION.RELEASE}")
            }
        }
    }
}
