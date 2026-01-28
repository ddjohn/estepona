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
package se.avelon.estepona.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.avelon.estepona.logging.DLog

object MyComponents {
    val TAG = DLog.forTag(MyComponents::class.java)
}

@Composable
fun MyButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = { MyText(text = text) },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.DarkGray),
        colors = ButtonDefaults.outlinedButtonColors(),
    )
}

@Composable
fun MyText(modifier: Modifier = Modifier, text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
}

@Composable
fun <T> MyDropMenu(label: String, list: List<T>) {
    var expanded by remember { mutableStateOf(false) }
    MyButton(Modifier, label) { expanded = true }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in list) {
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (item in list) {
                    DropdownMenuItem(
                        text = { MyText(Modifier, text = "${(item as T)?.text()}") },
                        onClick = {},
                    )
                }
            }
            DropdownMenuItem(
                text = { MyText(Modifier, text = "${(item as T)?.text()}") },
                onClick = {},
            )
        }
    }
}

@Composable
fun <T> MyDropMenu(label: String, list: Array<T>) {
    var expanded by remember { mutableStateOf(false) }
    MyButton(Modifier, label) { expanded = true }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in list) {
            DropdownMenuItem(
                text = { MyText(Modifier, text = "${(item as T)?.text()}") },
                onClick = {},
            )
        }
    }
}

fun <T> T.text(): String = "<$this>"
