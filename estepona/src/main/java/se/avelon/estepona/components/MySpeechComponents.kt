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

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.components.MySpeechComponents.textToSpeech
import se.avelon.estepona.logging.DLog
import java.util.Locale

object MySpeechComponents {
    val TAG = DLog.forTag(MySpeechComponents::class.java)

    lateinit var textToSpeech: TextToSpeech
}

@Composable
fun MySpeech(modifier: Modifier) {
    DLog.method(MySpeechComponents.TAG, "MySpeech()")

    val context = LocalContext.current

    textToSpeech =
        TextToSpeech(
            context,
            object : TextToSpeech.OnInitListener {
                override fun onInit(status: Int) {
                    DLog.method(MySpeechComponents.TAG, "onInit(); $status")
                    val result = textToSpeech.setLanguage(Locale.US)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        DLog.error(MySpeechComponents.TAG, "Language not supported")
                    } else {
                        DLog.info(MySpeechComponents.TAG, "Speak....")
                        textToSpeech.speak("Hello World", TextToSpeech.QUEUE_ADD, null, "david")
                    }
                }
            },
        )
    DLog.info(MySpeechComponents.TAG, "textToSpeech=$textToSpeech")
}
