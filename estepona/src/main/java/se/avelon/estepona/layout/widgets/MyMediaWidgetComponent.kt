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
package se.avelon.estepona.layout.widgets

import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.MainActivity.Companion.TAG
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.CleanButton
import se.avelon.estepona.compose.CleanText
import se.avelon.estepona.layout.MyScreenComponent
import se.avelon.estepona.layout.PackageManagerHelper
import se.avelon.estepona.logging.DLog

class MyMediaWidgetComponent : ViewModel(), MediaSessionManager.OnActiveSessionsChangedListener {
    companion object {
        val TAG = DLog.forTag(MyMediaWidgetComponent::class.java)
    }

    val mediaSession = mutableStateOf<MediaController?>(null)

    init {
        DLog.method(TAG, "init()")
        val context = MainApplication.getApplication().applicationContext

        val mediaSessionManager =
            context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

        mediaSessionManager.addOnActiveSessionsChangedListener(this, null)

        for (session in mediaSessionManager.getActiveSessions(null)) {
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.packageName}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.tag}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.extras}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.sessionInfo}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.sessionToken}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.flags}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.metadata}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.playbackInfo}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.playbackState}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.ratingType}")
            DLog.info(MyScreenComponent.Companion.TAG, "Session: ${session.transportControls}")
        }
        mediaSession.value = mediaSessionManager.getActiveSessions(null).get(0)
    }

    override fun onActiveSessionsChanged(controllers: List<MediaController?>?) {
        DLog.method(TAG, "onActiveSessionsChanged(): ${controllers?.size}")

        val context = MainApplication.getApplication().applicationContext

        val mediaSessionManager =
            context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

        mediaSession.value = mediaSessionManager.getActiveSessions(null).get(0)
    }
}

@Composable
fun MyMediaWidget(viewModel: MyMediaWidgetComponent = viewModel()) {
    val context = LocalContext.current

    val mediaController = MediaController(context, viewModel.mediaSession.value?.sessionToken!!)

    Card(
        modifier = Modifier.fillMaxWidth(),
        // .background( shape = RoundedCornerShape(8.dp)),
        border = BorderStroke(2.dp, Color.Red),
    ) {
        val name = viewModel.mediaSession.value?.packageName

        Image(
            bitmap = PackageManagerHelper.getBitmap(context, name),
            contentDescription = "description",
        )

        Image(
            imageVector = Icons.Outlined.SkipPrevious,
            contentDescription = "Test",
            colorFilter = ColorFilter.tint(Color.White),
        )
        CleanText("Title: " + mediaController.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE))

        CleanText(
            "Artist: " + mediaController.metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST)
        )
        Row {
            CleanButton(
                content = {
                    Image(
                        imageVector = Icons.Outlined.SkipPrevious,
                        contentDescription = "Test",
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                },
                onClick = {
                    DLog.info(TAG, "Previous...")
                    viewModel.mediaSession.value?.transportControls?.skipToPrevious()
                },
            )

            CleanButton(
                content = {
                    Image(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = "Test",
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                },
                onClick = {
                    DLog.info(TAG, "Play...")
                    viewModel.mediaSession.value?.transportControls?.play()
                },
            )

            CleanButton(
                content = {
                    Image(
                        imageVector = Icons.Outlined.SkipNext,
                        contentDescription = "Test",
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                },
                onClick = {
                    DLog.info(TAG, "Next...")
                    viewModel.mediaSession.value?.transportControls?.skipToNext()
                },
            )
        }
    }
}
