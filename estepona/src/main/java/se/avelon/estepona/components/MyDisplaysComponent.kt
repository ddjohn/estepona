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

import android.content.Context.DISPLAY_SERVICE
import android.hardware.display.DisplayManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.logging.DLog

object MyDisplaysComponent {
  val TAG = DLog.forTag(MyDisplaysComponent::class.java)
}

@Composable
fun MyDisplays(modifier: Modifier) {
  DLog.method(MyDisplaysComponent.TAG, "MyDisplays()")

  val context = LocalContext.current

  val displayManager = context.getSystemService(DISPLAY_SERVICE) as DisplayManager
  val displays = displayManager.displays

  Column(modifier) {
    for (display in displays) {
      Button(onClick = {}) {
        Text("id=${display.deviceProductInfo} name=${display.name} mode=${display.mode}")
      }
    }
  }
}
