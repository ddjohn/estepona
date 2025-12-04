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
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import se.avelon.estepona.logging.DLog

class MyMapboxComponent {
    companion object {
        val TAG = DLog.forTag(MyMapboxComponent::class.java)
    }
}

@Composable
fun MyMapbox(modifier: Modifier = Modifier) {
    DLog.method(MyMapboxComponent.TAG, "MyMapbox()")

    MapboxMap(
        modifier,
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(16.0)
                center(Point.fromLngLat(11.997013996301572, 57.68784852211992))
                pitch(60.0)
                bearing(120.0)
            }
        },
    )
}
