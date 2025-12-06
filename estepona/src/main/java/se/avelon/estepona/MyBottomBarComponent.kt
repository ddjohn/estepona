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

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import se.avelon.estepona.logging.DLog

class MyBottomBarComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyBottomBarComponent::class.java)
    }

    var selected by mutableStateOf(1)
}

@Composable
fun MyBottomBar(navController: NavController, viewModel: MyBottomBarComponent = MyBottomBarComponent(), modifier: Modifier = Modifier) {
    DLog.method(MyBottomBarComponent.TAG, "MyBottomBar()")

    NavigationBar(modifier = modifier) {
        MyBarItem(viewModel, navController, "Map", R.drawable.navigation_map, 1)
        MyBarItem(viewModel, navController, "Package", R.drawable.navigation_package, 2)
        MyBarItem(viewModel, navController, "Movie", R.drawable.navigtion_movie, 3)
        MyBarItem(viewModel, navController, "Vehicle", R.drawable.navigation_auto, 4)
        MyBarItem(viewModel, navController, "Audio", R.drawable.navigation_audio, 5)
        MyBarItem(viewModel, navController, "Camera", R.drawable.navigation_camera, 6)
        MyBarItem(viewModel, navController, "Statistics", R.drawable.navigation_statistics, 7)
        MyBarItem(viewModel, navController, "Stocks", R.drawable.navigation_stocks, 8)
        MyBarItem(viewModel, navController, "Settings", R.drawable.navigation_settings, 9)
    }
}

@Composable
fun MyBarItem(viewModel: MyBottomBarComponent, navController: NavController, text: String, res: Int, index: Int) {
    DLog.method(MyBottomBarComponent.TAG, "MyBarItem(): $index")

    ShortNavigationBarItem(
        selected = viewModel.selected == index,
        onClick = {
            DLog.method(MyBottomBarComponent.TAG, "onClick(): $index")
            navController.navigate(text)
        },
        icon = { Icon(painter = painterResource(res), null) },
        label = { Text(text) },
    )
}
