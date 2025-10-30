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

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.random.Random

const val TAG = "@@.DragAndDropBoxes"

@ExperimentalFoundationApi
@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier) {
    DLog.info(TAG, "DragAndDropBoxes(): $modifier")

    var startDrag by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val boxCount = 5
        var dragBoxIndex by remember {
            mutableIntStateOf(0)
        }
        var color = remember {
            (1..boxCount).map {
                Color(Random.nextLong()).copy(alpha = 1f)
            }
        }
        repeat(boxCount) { index ->
            DLog.info(TAG, "DragAndDropBoxes(): $index")

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(color.get(index))
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = { event ->
                            DLog.info(TAG, "shouldStartDragAndDrop(): $event")

                            event
                                .mimeTypes()
                                .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                        },
                        target = remember {
                            object : DragAndDropTarget {
                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                    DLog.info(TAG, "onDrop(): $event")

                                    val text = event.toAndroidDragEvent().clipData.getItemAt(0).text
                                    DLog.info(TAG, "Drag data(): $text")

                                    dragBoxIndex = index
                                    return true
                                }
                            }
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                this@Column.AnimatedVisibility(
                    visible = index == dragBoxIndex,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    Text(
                        text = "Drag me!",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = { startDrag = true })
                            }
                            .dragAndDropSource(transferData = { offset ->
                                DLog.info(TAG, "transferData(): $offset")

                                if (startDrag) {
                                    startDrag = false
                                }
                                DragAndDropTransferData(
                                    clipData = ClipData.newPlainText("label", "Dragged text"),
                                )
                            }),
                    )
                }
            }
        }
    }
}
