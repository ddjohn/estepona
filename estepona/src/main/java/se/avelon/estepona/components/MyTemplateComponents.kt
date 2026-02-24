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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.MainApplication
import se.avelon.estepona.logging.DLog

class MyTemplateComponents : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyTemplateComponents::class.java)
    }

    init {
        DLog.method(TAG, "init()")
        val context = MainApplication.getApplication().applicationContext
    }
}

@Composable
fun MyTemplate(modifier: Modifier, viewModel: MyTemplateComponents = viewModel()) {
    DLog.method(MyTemplateComponents.TAG, "MyTemplate()")

    val context = LocalContext.current
}
