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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import se.avelon.estepona.R
import se.avelon.estepona.logging.DLog

class MyFloatingButtonComponent {
    companion object {
        val TAG = DLog.forTag(MyFloatingButtonComponent::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFloatingButton(modifier: Modifier = Modifier) {
    DLog.method(MyFloatingButtonComponent.TAG, "MyFloatingButton()")

    FloatingActionButton(onClick = {
        DLog.method(MyFloatingButtonComponent.TAG, "onClick()")
    }) {
        Icon(
            contentDescription = "Email",
            painter = painterResource(R.drawable.floating),
            tint = Color.White,
        )
    }
}
