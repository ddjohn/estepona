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

        mediaSession.setPlaybackState(PlaybackState.Builder()
            .setState(PlaybackState.STATE_PLAYING, 1200, 1f)
            .build())
    }

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        DLog.method(TAG, "onPlayFromMediaId(): $query, $extras")
        super.onPlayFromSearch(query, extras)
    }
}
