package se.avelon.estepona

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
        Row() {
            Badge() {
                Icon(
                    contentDescription = "Email",
                    painter = painterResource(R.drawable.logo),
                )
            }
            Text("Estepona")
        }
    })
}