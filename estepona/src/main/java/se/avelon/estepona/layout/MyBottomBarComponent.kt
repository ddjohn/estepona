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

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import se.avelon.estepona.R
import se.avelon.estepona.logging.DLog

class MyBottomBarComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyBottomBarComponent::class.java)
    }

    val selected = mutableIntStateOf(0)
}

@Composable
fun MyBottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyBottomBarComponent = MyBottomBarComponent(),
) {
    DLog.method(MyBottomBarComponent.TAG, "MyBottomBar()")

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        MyBarItem(viewModel, navController, "Account", R.drawable.navigation_account, 0)
        MyBarItem(viewModel, navController, "Audio", R.drawable.navigation_audio, 1)
        MyBarItem(viewModel, navController, "Bluetooth", R.drawable.navigation_bluetooth, 2)
        MyBarItem(viewModel, navController, "Camera", R.drawable.navigation_camera, 3)
        MyBarItem(viewModel, navController, "Display", R.drawable.navigation_display, 4)
        MyBarItem(viewModel, navController, "Input", R.drawable.navigation_input, 5)
        MyBarItem(viewModel, navController, "Media", R.drawable.navigtion_movie, 6)
        MyBarItem(viewModel, navController, "Navigation", R.drawable.navigation_map, 7)
        MyBarItem(viewModel, navController, "Package", R.drawable.navigation_package, 8)
        MyBarItem(viewModel, navController, "Sensor", R.drawable.navigation_sensor, 9)
        MyBarItem(viewModel, navController, "Speech", R.drawable.navigation_speech, 10)
        MyBarItem(viewModel, navController, "Statistics", R.drawable.navigation_statistics, 11)
        MyBarItem(viewModel, navController, "Status", R.drawable.navigation_display, 12)
        MyBarItem(viewModel, navController, "Time", R.drawable.navigation_time, 13)
        MyBarItem(viewModel, navController, "Vehicle", R.drawable.navigation_auto, 14)

        MyBarItem(viewModel, navController, "Stocks", R.drawable.navigation_stocks, 15)
        MyBarItem(viewModel, navController, "Settings", R.drawable.navigation_settings, 16)
    }
}

@Composable
fun MyBarItem(
    viewModel: MyBottomBarComponent,
    navController: NavController,
    text: String,
    res: Int,
    index: Int,
) {
    DLog.method(MyBottomBarComponent.TAG, "MyBarItem(): $index, ${viewModel.selected}")

    ShortNavigationBarItem(
        selected = viewModel.selected.intValue == index,
        onClick = {
            DLog.method(MyBottomBarComponent.TAG, "onClick(): $index")
            navController.navigate(text)
            viewModel.selected.intValue = index
        },
        icon = { Icon(painter = painterResource(res), null) },
        label = { Text(text) },
    )
}
