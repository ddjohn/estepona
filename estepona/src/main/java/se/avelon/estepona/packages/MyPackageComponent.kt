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
package se.avelon.estepona.packages

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import se.avelon.estepona.TAG
import se.avelon.estepona.logging.DLog

class PackageComponent {
    companion object {
        val TAG = DLog.forTag(PackageComponent::class.java)
    }
}

@Composable
fun PackageButton(modifier: Modifier = Modifier, item: PackageItemData, onClick: () -> Unit) {
    DLog.method(TAG, "PackageButton()")

    Card(modifier = Modifier.size(64.dp, 64.dp).padding(8.dp), onClick = onClick, border = BorderStroke(2.dp, Color.Red)) {
        Column() {
            Image(bitmap = item.icon?.toBitmap()?.asImageBitmap()!!, contentDescription = "description")
            Text(text = item.text)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PackageGrid(modifier: Modifier = Modifier) {
    DLog.method(TAG, "PackageGrid()")
    LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        DLog.method(TAG, "LazyColumn()")

        item {
            Text(modifier = Modifier.padding(bottom = 16.dp), text = "Packages", style = MaterialTheme.typography.titleMedium)
        }

        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DLog.method(TAG, "FlowRow()")

                val context = LocalContext.current
                val packageManager = context.packageManager
                for (pkg in packageManager.getInstalledPackages(PackageManager.MATCH_ALL)) {
                    val item = PackageItemData(
                        pkg.packageName,
                        pkg.applicationInfo?.loadIcon(context?.packageManager),
                        packageManager?.getApplicationLabel(pkg.applicationInfo!!).toString(),
                    )

                    if (context.packageManager?.getLaunchIntentForPackage(pkg.packageName) != null) {
                        PackageButton(item = item, onClick = {
                            DLog.method(TAG, "onClick(): $this")

                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_LAUNCHER)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                            intent.setPackage(item.pkg)
                            context.startActivity(
                                context.packageManager?.getLaunchIntentForPackage(item.pkg),
                            )
                        })
                    }
                }
            }
        }
    }
}
