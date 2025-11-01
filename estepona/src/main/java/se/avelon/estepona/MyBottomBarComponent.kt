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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import se.avelon.estepona.logging.DLog

class MyBottomBarComponent: ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyBottomBarComponent::class.java)
    }

    var selected by mutableStateOf(1)

}

@Composable
fun MyBottomBar(viewModel: MyBottomBarComponent = MyBottomBarComponent(), modifier: Modifier = Modifier) {
    DLog.method(MyBottomBarComponent.TAG, "MyBottomBar2()")

    //var selected by rememberSaveable { mutableIntStateOf(1) }

    NavigationBar(modifier = modifier) {
        MyBarItem(viewModel, text = "Map", res = R.drawable.navigation_map, index = 1)
        MyBarItem(viewModel, text = "Camera", res = R.drawable.navigation_camera, index = 2)
        MyBarItem(viewModel, text = "Statistics", res = R.drawable.navigation_statistics, index = 3)
        MyBarItem(viewModel, text = "Stocks", res = R.drawable.navigation_stocks, index = 4)
        MyBarItem(viewModel, text = "Movie", res = R.drawable.navigtion_movie, index = 5)
        MyBarItem(viewModel, text = "Settings", res = R.drawable.navigation_settings, index = 6)
    }
}

@Composable
fun MyBarItem(viewModel: MyBottomBarComponent, modifier: Modifier = Modifier, text: String, res: Int, index: Int) {
    DLog.method(MyBottomBarComponent.TAG, "MyBarItem()")

    ShortNavigationBarItem(
        selected = viewModel.selected == index,
        onClick = { viewModel.selected = index},
        icon = { Icon(painter = painterResource(id = res), contentDescription = null) },
        label = { Text(text) },
    )
}
