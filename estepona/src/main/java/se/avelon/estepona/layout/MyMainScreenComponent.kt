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
package se.avelon.estepona.layout

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.DISPLAY_SERVICE
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.view.SurfaceView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.avelon.estepona.MainActivity.Companion.TAG
import se.avelon.estepona.MainApplication
import se.avelon.estepona.components.MyAccount
import se.avelon.estepona.components.MyAudio
import se.avelon.estepona.components.MyBlutooth
import se.avelon.estepona.components.MyCamera
import se.avelon.estepona.components.MyDisplays
import se.avelon.estepona.components.MyInput
import se.avelon.estepona.components.MyMedia
import se.avelon.estepona.components.MyNavigation
import se.avelon.estepona.components.MyPackage
import se.avelon.estepona.components.MyPerformance
import se.avelon.estepona.components.MyPower
import se.avelon.estepona.components.MySensor
import se.avelon.estepona.components.MySettings
import se.avelon.estepona.components.MySpeech
import se.avelon.estepona.components.MyStatistics
import se.avelon.estepona.components.MyStatus
import se.avelon.estepona.components.MyTelecom
import se.avelon.estepona.components.MyTime
import se.avelon.estepona.components.MyVehicle
import se.avelon.estepona.layout.widgets.MyMediaWidget
import se.avelon.estepona.logging.DLog
import se.avelon.estepona.ui.theme.EsteponaTheme

class MyScreenComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyScreenComponent::class.java)
    }

    init {
        val context = MainApplication.getApplication().applicationContext
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyMainScreen(viewModel: MyScreenComponent = viewModel()) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val displayManager = context.getSystemService(DISPLAY_SERVICE) as DisplayManager
    val w = displayManager.getDisplay(0).width
    val h = displayManager.getDisplay(0).height

    val min = if (w < h) w else h
    DLog.error(MyScreenComponent.TAG, "==> w=$w, h=$h, min=$min")

    EsteponaTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = { MyFloatingButton() },
            topBar = { MyTopBar() },
            bottomBar = { MyBottomBar(navController = navController) },
        ) { innerPadding ->
            DLog.method(TAG, "Scaffold(): $innerPadding")

            Row {
                DLog.method(TAG, "Row()")

                MyDragAndDropBoxes(modifier = Modifier.padding(innerPadding).fillMaxWidth(0.15f))
                Column(Modifier.padding(innerPadding).fillMaxWidth(0.15f)) {
                    MyMediaWidget()
                    VirtualDevices()

                    Button(content = { Text("Button") }, onClick = {})
                    Text("Text")
                    Checkbox(true, onCheckedChange = {})
                    Switch(checked = true, onCheckedChange = {})
                    Label(content = { Text("Label") }, label = {})
                    TextField(state = TextFieldState("TextField"))
                }
                NavHost(
                    navController = navController,
                    startDestination = "Account",
                    modifier = Modifier.padding(innerPadding).fillMaxWidth(0.7f),
                ) {
                    composable("Account") { MyAccount(modifier = Modifier.fillMaxSize()) }
                    composable("Audio") { MyAudio(modifier = Modifier.fillMaxSize()) }
                    composable("Bluetooth") { MyBlutooth(modifier = Modifier.fillMaxSize()) }
                    composable("Camera") { MyCamera(modifier = Modifier.fillMaxSize()) }
                    composable("Display") { MyDisplays(modifier = Modifier.fillMaxSize()) }
                    composable("Input") { MyInput(modifier = Modifier.fillMaxSize()) }
                    composable("Media") { MyMedia(modifier = Modifier.fillMaxSize()) }
                    composable("Navigation") { MyNavigation(modifier = Modifier.fillMaxSize()) }
                    composable("Package") { MyPackage(modifier = Modifier.fillMaxSize()) }
                    composable("Performance") { MyPerformance(modifier = Modifier.fillMaxSize()) }
                    composable("Power") { MyPower(modifier = Modifier.fillMaxSize()) }
                    composable("Sensor") { MySensor(modifier = Modifier.fillMaxSize()) }
                    composable("Settings") { MySettings(modifier = Modifier.fillMaxSize()) }
                    composable("Speech") { MySpeech(modifier = Modifier.fillMaxSize()) }
                    composable("Statistics") { MyStatistics(modifier = Modifier.fillMaxSize()) }
                    composable("Status") { MyStatus(modifier = Modifier.fillMaxSize()) }
                    composable("Telecom") { MyTelecom(modifier = Modifier.fillMaxSize()) }
                    composable("Time") { MyTime(modifier = Modifier.fillMaxSize()) }
                    composable("Vehicle") { MyVehicle(modifier = Modifier.fillMaxSize()) }
                }
            }
        }
    }
}

object PackageManagerHelper {
    fun getBitmap(context: Context, name: String?): ImageBitmap {
        val packageManager = context.packageManager
        val packageInfo =
            packageManager.getPackageInfo(
                name!!,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong()),
            )
        return packageInfo.applicationInfo?.loadIcon(packageManager)?.toBitmap()?.asImageBitmap()!!
    }
}

@Composable
fun VirtualDevices() {
    val context = LocalContext.current

    val displayManager = context.getSystemService(DISPLAY_SERVICE) as DisplayManager

    val localSurfaceView: SurfaceView = remember {
        val surfaceView = SurfaceView(context)
        surfaceView
    }
    /*
    displayManager.createVirtualDisplay(
        "Hello",
        300,
        200,
        121,
        localSurfaceView.holder.surface,
        DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
    )
     */

    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        // .background( shape = RoundedCornerShape(8.dp)),
        border = BorderStroke(2.dp, Color.Red),
    ) {
        AndroidView(factory = { localSurfaceView })
    }

    /*
    val canvas = localSurfaceView.holder.lockCanvas()
    canvas.drawCircle(100f, 100f, 50f, Paint())
    localSurfaceView.holder.unlockCanvasAndPost(canvas)
     */
}
