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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import se.avelon.estepona.logging.DLog
import se.avelon.estepona.packages.PackageGrid
import se.avelon.estepona.ui.theme.EsteponaTheme

class MainActivity : ComponentActivity() {
    companion object {
        val TAG = DLog.forTag(MainActivity::class.java)
    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        DLog.method(TAG, "onCreate(): $savedInstanceState")
        super.onCreate(savedInstanceState)

        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)

        enableEdgeToEdge()

        DLog.info(TAG, "SetContent...")
        setContent {
            EsteponaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = { MyFloatingButton() },
                    topBar = { MyTopBar() },
                    bottomBar = { MyBottomBar() },
                ) { innerPadding ->
                    DLog.method(TAG, "Scaffold(): $innerPadding")

                    Row() {
                        DLog.method(TAG, "Row()")
                        DragAndDropBoxes(
                            modifier = Modifier.padding(innerPadding).fillMaxWidth(0.2f),
                        )
                        MapboxMap(
                            Modifier.fillMaxSize(),
                            mapViewportState = rememberMapViewportState {
                                setCameraOptions {
                                    zoom(1.0)
                                    center(Point.fromLngLat(-98.0, 39.5))
                                }
                            },
                        )
                        PackageGrid(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = Color.White,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EsteponaTheme {
        Greeting("Android")
    }
}
