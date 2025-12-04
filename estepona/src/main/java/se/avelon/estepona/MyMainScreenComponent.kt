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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.avelon.estepona.MainActivity.Companion.TAG
import se.avelon.estepona.components.MyDragAndDropBoxes
import se.avelon.estepona.components.MyMapbox
import se.avelon.estepona.components.exoplayer.PlayerRoute
import se.avelon.estepona.components.packages.PackageGrid
import se.avelon.estepona.logging.DLog
import se.avelon.estepona.ui.theme.EsteponaTheme

class MyScreenComponent {
    companion object {
        val TAG = DLog.forTag(MyScreenComponent::class.java)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyMainScreen() {
    val navController = rememberNavController()

    EsteponaTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = { MyFloatingButton() }, topBar = { MyTopBar() }, bottomBar = { MyBottomBar(navController) }) { innerPadding ->
            DLog.method(TAG, "Scaffold(): $innerPadding")

            Row() {
                DLog.method(TAG, "Row()")

                MyDragAndDropBoxes(modifier = Modifier.padding(innerPadding).fillMaxWidth(0.2f))
                NavHost(navController = navController, startDestination = "Map", modifier = Modifier.padding(innerPadding)) {
                    composable("Map") {
                        MyMapbox(modifier = Modifier.fillMaxSize())
                    }

                    composable("Package") {
                        PackageGrid(modifier = Modifier.fillMaxSize())
                    }

                    composable("Movie") {
                        PlayerRoute(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
