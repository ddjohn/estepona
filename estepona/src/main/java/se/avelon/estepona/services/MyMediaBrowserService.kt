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

import android.media.MediaDescription
import android.media.browse.MediaBrowser
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Bundle
import android.service.media.MediaBrowserService
import se.avelon.estepona.logging.DLog

class MyMediaBrowserService : MediaBrowserService() {
    companion object {
        val TAG = DLog.forTag(MyMediaBrowserService::class.java)
    }

    override fun onCreate() {
        DLog.method(TAG, "onCreate()")
        super.onCreate()

        val mediaSession =
            MediaSession(this, TAG).apply {
                setFlags(
                    MediaSession.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS
                )
                setPlaybackState(
                    PlaybackState.Builder()
                        .setActions(
                            PlaybackState.ACTION_PLAY or
                                PlaybackState.ACTION_PAUSE or
                                PlaybackState.ACTION_STOP or
                                PlaybackState.ACTION_PLAY_FROM_MEDIA_ID or
                                PlaybackState.ACTION_PLAY_FROM_SEARCH or
                                PlaybackState.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackState.ACTION_SKIP_TO_NEXT
                        )
                        .build()
                )
                setSessionToken(sessionToken)
                setPlaybackState(
                    PlaybackState.Builder().setState(PlaybackState.STATE_STOPPED, 3200, 1f).build()
                )
                isActive = true
            }

        mediaSession.setCallback(MyMediaSessionCallback(mediaSession))
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot? {
        DLog.method(TAG, "onGetRoot(): $clientPackageName, $clientUid, $rootHints")
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(parentId: String, result: Result<List<MediaBrowser.MediaItem?>?>) {
        DLog.method(TAG, "onLoadChildren(): $parentId, $result")

        val mediaItems = ArrayList<MediaBrowser.MediaItem>()

        var mediaItem: MediaBrowser.MediaItem
        var mediaItem2: MediaBrowser.MediaItem
        if (parentId == "root") {
            mediaItem =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("disk")
                        .setTitle("Davids Songs")
                        .setSubtitle("Led Zeppelin, Pink Floyd, ...")
                        .setDescription("Some Disk Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_BROWSABLE,
                )
            mediaItem2 =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("disk2")
                        .setTitle("Pias Songs")
                        .setSubtitle("Thomas Stenström, ...")
                        .setDescription("Some Disk Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_BROWSABLE,
                )
        } else if (parentId == "disk") {
            mediaItem =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("album")
                        .setTitle("Songs From An Empty Chair")
                        .setSubtitle("Tears For Fears")
                        .setDescription("Some Album Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_BROWSABLE,
                )
            mediaItem2 =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("album2")
                        .setTitle("Hanky Panky")
                        .setSubtitle("David Bowie")
                        .setDescription("Some Album Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_BROWSABLE,
                )
        } else if (parentId == "disk2") {
            mediaItem =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("album3")
                        .setTitle("Songs From An Empty Chair")
                        .setSubtitle("1984")
                        .setDescription("Some Album Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_BROWSABLE,
                )
            mediaItem2 =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("album4")
                        .setTitle("Final Cut")
                        .setSubtitle("Pink Floyd")
                        .setDescription("Some Album Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_BROWSABLE,
                )
        } else if (parentId == "album") {
            mediaItem =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("song")
                        .setTitle("Led Zeppelin IV")
                        .setSubtitle("Led Zeppelin")
                        .setDescription("Some Song Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_PLAYABLE,
                )
            mediaItem2 =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("song2")
                        .setTitle("Seeds of Love")
                        .setSubtitle("1984")
                        .setDescription("Some Song Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_PLAYABLE,
                )
        } else {
            mediaItem =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("songX")
                        .setTitle("White Noice")
                        .setSubtitle("1984")
                        .setDescription("Some Song Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_PLAYABLE,
                )
            mediaItem2 =
                MediaBrowser.MediaItem(
                    MediaDescription.Builder()
                        .setMediaId("song4")
                        .setTitle("White Noice")
                        .setSubtitle("1984")
                        .setDescription("Some Song Description")
                        .build(),
                    MediaBrowser.MediaItem.FLAG_PLAYABLE,
                )
        }
        mediaItems.add(mediaItem)
        mediaItems.add(mediaItem2)
        result.sendResult(mediaItems)
    }
}
