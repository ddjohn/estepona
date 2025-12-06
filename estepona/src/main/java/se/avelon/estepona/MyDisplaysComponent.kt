package se.avelon.estepona

import android.content.Context
import android.content.Context.DISPLAY_SERVICE
import android.hardware.display.DisplayManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.logging.DLog

object MyDisplaysComponent {
    val TAG = DLog.forTag(MyDisplaysComponent::class.java)
}


@Composable
fun MyDisplays(modifier: Modifier) {
    DLog.method(MyDisplaysComponent.TAG, "MyDisplays()")

    val context = LocalContext.current

    val displayManager = context.getSystemService(DISPLAY_SERVICE) as DisplayManager
    val displays = displayManager.displays

    Column(modifier) {
        for(display in displays) {
            Button(onClick = { }) {
                Text("id=${display.deviceProductInfo} name=${display.name} mode=${display.mode}")
            }
        }
    }
}
