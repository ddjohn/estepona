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

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.INPUT_SERVICE
import android.hardware.input.InputManager
import android.view.InputEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.compose.MyButton
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

object MyInputComponents {
    val TAG = DLog.forTag(MyInputComponents::class.java)
}

@Composable
fun MyInput(modifier: Modifier) {
    DLog.method(MyInputComponents.TAG, "MyInput()")

    val context = LocalContext.current
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    val inputManager = context.getSystemService(INPUT_SERVICE) as InputManager

    Column {
        Row {
            for (deviceId in inputManager.inputDeviceIds) {
                DLog.info(MyInputComponents.TAG, "deviceId=$deviceId")
                MyText(Modifier, "$deviceId")
            }
        }
        Row {
            MyButton(Modifier, "Home") {
                DLog.info(MyInputComponents.TAG, "Home")

                val injectInputEvent =
                    inputManager.javaClass.getMethod(
                        "injectInputEvent",
                        InputEvent::class.java,
                        Int::class.java,
                    )
                val inputEvent =
                    MotionEvent.obtain(
                        System.currentTimeMillis(),
                        System.currentTimeMillis(),
                        MotionEvent.ACTION_DOWN,
                        0f,
                        0f,
                        0,
                    )
                injectInputEvent.invoke(
                    inputManager,
                    inputEvent,
                    0,
                ) // INJECT_INPUT_EVENT_MODE_ASYNC
            }

            MyDropMenu("Input Methods:", inputMethodManager.inputMethodList)

            MyButton(Modifier, "Kitchen") {
                DLog.info(MyInputComponents.TAG, "Kitchen")
                context.startActivity(
                    context.packageManager.getLaunchIntentForPackage(
                        "com.google.android.car.kitchensink"
                    )
                )
            }
        }
    }
}
