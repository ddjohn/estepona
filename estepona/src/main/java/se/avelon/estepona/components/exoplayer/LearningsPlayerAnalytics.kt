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
package se.avelon.estepona.components.exoplayer

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.analytics.AnalyticsListener.EventTime
import se.avelon.estepona.logging.DLog

@UnstableApi
class LearningsPlayerAnalytics(private val exoPlayer: ExoPlayer?) : AnalyticsListener {
    companion object {
        val TAG = DLog.forTag(LearningsPlayerAnalytics::class.java)
    }

    @OptIn(UnstableApi::class)
    override fun onPlayerReleased(eventTime: EventTime) {
        DLog.method(TAG, "onPlayerReleased(): $eventTime")
        super.onPlayerReleased(eventTime)
    }

    override fun onPlaybackStateChanged(eventTime: EventTime, state: Int) {
        DLog.method(TAG, "onPlaybackStateChanged(): $eventTime, $state")

        val itemId = currentMediaItemTag()

        when (state) {
            Player.STATE_READY -> {
                DLog.info(TAG, "Playback Ready time = ${eventTime.realtimeMs} and itemId = $itemId")
            }

            Player.STATE_ENDED -> {
                DLog.info(TAG, "Playback Ended time = ${eventTime.realtimeMs} and itemId = $itemId")
            }

            Player.STATE_BUFFERING -> {
                DLog.info(TAG, "Buffering time = ${eventTime.realtimeMs} and itemId = $itemId")
            }

            Player.STATE_IDLE -> {
                DLog.info(TAG, "Player Idle time = ${eventTime.realtimeMs} and itemId = $itemId")
            }
        }
    }

    override fun onPlayerError(eventTime: EventTime, error: PlaybackException) {
        DLog.method(TAG, "onPlayerError(): $eventTime, $error")

        currentMediaItemTag().let { itemId ->
            {
                DLog.info(
                    TAG,
                    "Playback Error: ${error.message}  time = ${eventTime.realtimeMs} and itemId = $itemId",
                )
            }
        }
    }

    override fun onIsPlayingChanged(eventTime: EventTime, isPlaying: Boolean) {
        DLog.method(TAG, "onIsPlayingChanged(): $eventTime, $isPlaying")

        currentMediaItemTag().let { itemId ->
            DLog.info(
                TAG,
                "Is Playing: $isPlaying time = ${eventTime.realtimeMs} and itemId = $itemId",
            )
        }
    }

    override fun onMediaItemTransition(eventTime: EventTime, mediaItem: MediaItem?, reason: Int) {
        DLog.method(TAG, "onMediaItemTransition(): $eventTime, ${mediaItem?.mediaId}, $reason}")
        super.onMediaItemTransition(eventTime, mediaItem, reason)
    }

    private fun currentMediaItemTag(): String? =
        exoPlayer?.currentMediaItem?.localConfiguration?.tag as? String
}
