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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import se.avelon.estepona.logging.DLog

class MyBottomBarComponent {
    companion object {
        val TAG = DLog.forTag(MyBottomBarComponent::class.java)
    }
}

@Composable
fun MyBottomBar(modifier: Modifier = Modifier) {
    DLog.method(MyBottomBarComponent.TAG, "MyBottomBar2()")

    NavigationBar(modifier = modifier) {
        MyBarItem(text = "Map", res = R.drawable.navigation_map)
        MyBarItem(text = "Camera", res = R.drawable.navigation_camera)
        MyBarItem(text = "Statistics", res = R.drawable.navigation_statistics)
        MyBarItem(text = "Stocks", res = R.drawable.navigation_stocks)
        MyBarItem(text = "Movie", res = R.drawable.navigtion_movie)
        MyBarItem(text = "Settings", res = R.drawable.navigation_settings)
    }
}

@Composable
fun MyBarItem(modifier: Modifier = Modifier, text: String, res: Int) {
    DLog.method(MyBottomBarComponent.TAG, "MyBarItem()")

    ShortNavigationBarItem(
        selected = false,
        onClick = {},
        icon = { Icon(painter = painterResource(id = res), contentDescription = null) },
        label = { Text(text) },
    )
}
