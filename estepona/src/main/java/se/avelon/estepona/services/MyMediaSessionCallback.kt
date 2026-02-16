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
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Bundle
import se.avelon.estepona.logging.DLog

class MyMediaSessionCallback(val mediaSession: MediaSession) : MediaSession.Callback() {
    companion object {
        val TAG = DLog.forTag(MyMediaSessionCallback::class.java)
    }

    override fun onPrepare() {
        DLog.method(TAG, "onPrepare()")
        super.onPrepare()
    }

    override fun onPlay() {
        DLog.method(TAG, "onPlay()")
        super.onPlay()
    }

    override fun onStop() {
        DLog.method(TAG, "onStop()")
        super.onPlay()
    }

    override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
        DLog.method(TAG, "onMediaButtonEvent()")
        return super.onMediaButtonEvent(mediaButtonIntent)
    }

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        DLog.method(TAG, "onPlayFromMediaId(): $mediaId, $extras")
        super.onPlayFromMediaId(mediaId, extras)

        mediaSession.setPlaybackState(
            PlaybackState.Builder().setState(PlaybackState.STATE_PLAYING, 1200, 1f).build()
        )
    }

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        DLog.method(TAG, "onPlayFromMediaId(): $query, $extras")
        super.onPlayFromSearch(query, extras)
    }
}
