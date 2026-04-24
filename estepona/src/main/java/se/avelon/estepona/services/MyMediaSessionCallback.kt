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
package se.avelon.estepona.services

import android.content.Intent
import android.drm.DrmStore
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.SystemClock
import se.avelon.estepona.logging.DLog

class MyMediaSessionCallback(val mediaSession: MediaSession) : MediaSession.Callback(), Runnable {
    companion object {
        val TAG = DLog.forTag(MyMediaSessionCallback::class.java)
    }

    val playbackStateBuilder = PlaybackState.Builder()
    val defaultActions = PlaybackState.ACTION_SKIP_TO_PREVIOUS or PlaybackState.ACTION_SKIP_TO_NEXT
    //PlaybackState.ACTION_PLAY_PAUSE or PlaybackState.ACTION_SEEK_TO or PlaybackState.ACTION_PLAY or PlaybackState.ACTION_FAST_FORWARD or PlaybackState.ACTION_PLAY_FROM_MEDIA_ID or PlaybackState.ACTION_PLAY_FROM_SEARCH or PlaybackState.ACTION_PLAY_FROM_URI or PlaybackState.ACTION_PLAY_PAUSE
    var speed = 0f
    var position = PlaybackState.PLAYBACK_POSITION_UNKNOWN
    var state = PlaybackState.STATE_NONE

    val thread: Thread = Thread(this)

    init {
        DLog.method(TAG, "init()")
        thread.start()
    }

    override fun onPrepare() {
        DLog.method(TAG, "onPrepare()")
        super.onPrepare()
    }

    override fun onPlay() {
        DLog.method(TAG, "onPlay()")
        super.onPlay()

        mediaSession.isActive = true
        state = PlaybackState.STATE_PLAYING
        position = 0
        speed = 1f
    }

    override fun onStop() {
        DLog.method(TAG, "onStop()")
        super.onStop()

        mediaSession.isActive = false
        state = PlaybackState.STATE_STOPPED
        speed = 0f
    }

    override fun onPause() {
        DLog.method(TAG, "onStop()")
        super.onStop()

        mediaSession.isActive = false
        state = PlaybackState.STATE_PAUSED
        speed = 0f
    }

    override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
        DLog.method(TAG, "onMediaButtonEvent()")
        return super.onMediaButtonEvent(mediaButtonIntent)
    }

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        DLog.method(TAG, "onPlayFromMediaId(): $mediaId, $extras")
        super.onPlayFromMediaId(mediaId, extras)

        mediaSession.isActive = true
        state = PlaybackState.STATE_PLAYING
        position = 0
        speed = 1f
    }

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        DLog.method(TAG, "onPlayFromMediaId(): $query, $extras")
        super.onPlayFromSearch(query, extras)
    }

    override fun run() {
        DLog.method(TAG, "run()")

        while (true) {
            when(state) {
                PlaybackState.STATE_PLAYING -> {
                    playbackStateBuilder.setActions(defaultActions or
                        PlaybackState.ACTION_PAUSE
                                or PlaybackState.ACTION_STOP
                                or PlaybackState.ACTION_PAUSE
                    )

                    if(position == PlaybackState.PLAYBACK_POSITION_UNKNOWN)
                        position = 0

                    position += 1000
                }
                PlaybackState.STATE_PAUSED -> {
                    playbackStateBuilder.setActions(defaultActions or
                        PlaybackState.ACTION_PLAY
                                or PlaybackState.ACTION_PAUSE
                    )
                }
                else -> {}
            }


            if (position > 240000L) {
                state = PlaybackState.STATE_NONE
                speed = 0f
                position = PlaybackState.PLAYBACK_POSITION_UNKNOWN
            }

            DLog.debug(TAG, "PlaybackState = $state, $position")
            mediaSession.setPlaybackState(
                playbackStateBuilder
                    .setState(state, position, speed, SystemClock.elapsedRealtime())
                    .build()
            )

            mediaSession.setMetadata(
                MediaMetadata.Builder()
                    .putString(MediaMetadata.METADATA_KEY_TITLE, "Track title: $position")
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, "Artist: $state")
                    .build()
            )

            Thread.sleep(1000)
        }
    }
}
