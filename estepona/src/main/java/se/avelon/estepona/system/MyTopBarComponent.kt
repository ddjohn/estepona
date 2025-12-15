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
package se.avelon.estepona.system

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import se.avelon.estepona.R
import se.avelon.estepona.logging.DLog

class MyTopBarComponent {
    companion object {
        val TAG = DLog.forTag(MyTopBarComponent::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(modifier: Modifier = Modifier) {
    TopAppBar(title = {
        Row {
            Badge {
                Icon(
                    contentDescription = "Email",
                    painter = painterResource(R.drawable.logo),
                )
            }
            Text("Estepona")

            HorizontalDivider()


        }
    })
}
