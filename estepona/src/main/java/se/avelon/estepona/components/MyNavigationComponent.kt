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

import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.GnssAntennaInfo
import android.location.GnssMeasurementsEvent
import android.location.GnssNavigationMessage
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.location.OnNmeaMessageListener
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import se.avelon.estepona.components.MyNavigationComponent.Companion.printWriter
import se.avelon.estepona.logging.DLog
import java.io.File
import java.io.PrintWriter
import java.util.Calendar
import java.util.StringTokenizer

class MyNavigationComponent {
    companion object {
        val TAG = DLog.forTag(MyNavigationComponent::class.java)
        lateinit var printWriter: PrintWriter
    }
}

@Composable
fun MyNavigation(modifier: Modifier = Modifier) {
    DLog.method(MyNavigationComponent.TAG, "MyMapbox()")

    val context = LocalContext.current

    if (context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
        return
    }
    if (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
        return
    }

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

    val file = File("${context.dataDir}/navi_${Calendar.getInstance().timeInMillis}.log")
    DLog.info(MyNavigationComponent.TAG, "Logging to $file")

    file.parentFile?.mkdirs()
    printWriter = PrintWriter(file)

    val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    for (provide in locationManager.allProviders) {
        DLog.info(MyNavigationComponent.TAG, "provide=$provide")
    }

    locationManager.addNmeaListener(
        context.mainExecutor,
        object : OnNmeaMessageListener {
            override fun onNmeaMessage(message: String?, timestamp: Long) {
                DLog.method(MyNavigationComponent.TAG, "onNmeaMessage(): $timestamp $message")
                printWriter.println("onNmeaMessage(): $timestamp $message")

                val tokenizer = StringTokenizer(message, ",")

                val type = tokenizer.nextElement()
                if (type == "${'$'}GNGLL") {
                    DLog.info(MyNavigationComponent.TAG, "GNGLL")
                    // val latitude = tokenizer.nextElement()
                    // val directionLatitude = tokenizer.nextElement()
                    // val longitude = tokenizer.nextElement()
                    // val directionLongitude = tokenizer.nextElement()
                    // val utc = tokenizer.nextElement()
                    // val status = tokenizer.nextElement()
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}PAPTT") { // $PAPTT,17335561282390*6E
                    val checksum = tokenizer.nextElement()
                    DLog.info(MyNavigationComponent.TAG, "PAPTT ($checksum)")
                } else if (type == "${'$'}GNRMC") {
                    DLog.info(MyNavigationComponent.TAG, "GNRMC")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GNGLL") {
                    DLog.info(MyNavigationComponent.TAG, "GNGLL")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GNGSA") {
                    DLog.info(MyNavigationComponent.TAG, "GNGSA")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GLGSV") { // GLONASS Satellites
                    DLog.info(MyNavigationComponent.TAG, "GLGSV")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GAGSV") { // Galileo satellites
                    DLog.info(MyNavigationComponent.TAG, "GAGSV")
                    satellites(tokenizer)
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GBGSV") { // BeiDou satellites
                    DLog.info(MyNavigationComponent.TAG, "GBGSV")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GPGSV") { // GPS and SBAS satellites
                    DLog.info(MyNavigationComponent.TAG, "GPGSV")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GNGGA") {
                    DLog.info(MyNavigationComponent.TAG, "GNGGA")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GQGSV") { // QZSS Satellites
                    DLog.info(MyNavigationComponent.TAG, "GQGSV")
                    val utc = tokenizer.nextElement()
                    // val latitude = tokenizer.nextElement()
                    // val directionLatitude = tokenizer.nextElement()
                    // val longitude = tokenizer.nextElement()
                    // val directionLongitude = tokenizer.nextElement()
                    // val quality = tokenizer.nextElement()
                    // val svs = tokenizer.nextElement()
                    // val hdop = tokenizer.nextElement()
                    // val height = tokenizer.nextElement()
                    // val heightUnit = tokenizer.nextElement()
                    // val geoid = tokenizer.nextElement()
                    // val geoidUnit = tokenizer.nextElement()
                    // val age = tokenizer.nextElement()
                    // val ref = tokenizer.nextElement()
                    // val checksum = tokenizer.nextElement()
                    // DLog.info(TAG, "quality=$quality")
                    // DLog.info(TAG, "hdop=$hdop")
                    // DLog.info(TAG, "height=$height")
                } else if (type == "${'$'}GPGGA") {
                    DLog.info(MyNavigationComponent.TAG, "GPGGA")
                    val checksum = tokenizer.nextElement()
                } else if (type == "${'$'}GPRMC") {
                    DLog.info(MyNavigationComponent.TAG, "GPRMC")
                    val checksum = tokenizer.nextElement()
                } else {
                    throw RuntimeException("david")
                }
            }
        },
    )
    locationManager.registerAntennaInfoListener(
        context.mainExecutor,
        object : GnssAntennaInfo.Listener {
            override fun onGnssAntennaInfoReceived(gnssAntennaInfos: List<GnssAntennaInfo?>) {
                DLog.info(MyNavigationComponent.TAG, "onGnssAntennaInfoReceived(): $gnssAntennaInfos")
                printWriter.println("onGnssAntennaInfoReceived(): $gnssAntennaInfos")
            }
        },
    )
    locationManager.registerGnssMeasurementsCallback(
        context.mainExecutor,
        object : GnssMeasurementsEvent.Callback() {
            override fun onGnssMeasurementsReceived(eventArgs: GnssMeasurementsEvent?) {
                DLog.info(MyNavigationComponent.TAG, "onGnssMeasurementsReceived(): $eventArgs")
                printWriter.println("onGnssMeasurementsReceived(): $eventArgs")
                super.onGnssMeasurementsReceived(eventArgs)
            }
        },
    )
    locationManager.registerGnssNavigationMessageCallback(
        context.mainExecutor,
        object : GnssNavigationMessage.Callback() {
            override fun onGnssNavigationMessageReceived(event: GnssNavigationMessage?) {
                DLog.info(MyNavigationComponent.TAG, "onGnssNavigationMessageReceived(): $event")
                printWriter.println("onGnssNavigationMessageReceived(): $event")
                super.onGnssNavigationMessageReceived(event)
            }
        },
    )
    locationManager.registerGnssStatusCallback(
        context.mainExecutor,
        object : GnssStatus.Callback() {
            override fun onSatelliteStatusChanged(status: GnssStatus) {
                DLog.info(MyNavigationComponent.TAG, "onSatelliteStatusChanged(): $status")
                printWriter.println("onSatelliteStatusChanged(): $status")
                super.onSatelliteStatusChanged(status)
            }
        },
    )

    val locationRequest = LocationRequest.Builder(1000).build()
    locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        locationRequest,
        context.mainExecutor,
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                DLog.info(MyNavigationComponent.TAG, "onLocationChanged(): $location")
                printWriter.println("onLocationChanged(): $location")
            }
        },
    )
}

private fun satellites(tokenizer: StringTokenizer) {
    val sentences = tokenizer.nextElement()
    val number = tokenizer.nextElement()
    val satellites = tokenizer.nextElement()
    DLog.info(MyNavigationComponent.TAG, "sentences=$sentences, number=$number, satellites=$satellites")
}
