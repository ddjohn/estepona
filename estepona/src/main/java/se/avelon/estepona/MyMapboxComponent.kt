package se.avelon.estepona

import androidx.compose.foundation.layout.fillMaxSize
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
                zoom(1.0)
                center(Point.fromLngLat(-98.0, 39.5))
            }
        },
    )
}

