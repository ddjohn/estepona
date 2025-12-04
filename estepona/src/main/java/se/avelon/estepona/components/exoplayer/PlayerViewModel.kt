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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.avelon.estepona.logging.DLog

@UnstableApi
class PlayerViewModel : ViewModel() {
    companion object {
        val TAG = DLog.forTag(PlayerViewModel::class.java)

        // Source for videos: https://gist.github.com/jsturgis/3b19447b304616f18657
        const val Video_1 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        const val Video_2 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        const val Video_3 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        const val Video_4 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
    }

    private val _playerState = MutableStateFlow<ExoPlayer?>(null)
    val playerState: StateFlow<ExoPlayer?> = _playerState

    private val hashMapVideoStates = mutableMapOf<String, VideoItem>()
    private lateinit var analytics: LearningsPlayerAnalytics

    fun createPlayerWithMediaItems(context: Context) {
        DLog.method(TAG, "createPlayerWithMediaItems()")

        if (_playerState.value == null) {
            val mediaItems = listOf(
                MediaItem.Builder().setUri(Video_1).setMediaId("Video_1").setTag("Video_1").build(),
                MediaItem.Builder().setUri(Video_2).setMediaId("Video_2").setTag("Video_2").build(),
                MediaItem.Builder().setUri(Video_3).setMediaId("Video_3").setTag("Video_3").build(),
                MediaItem.Builder().setUri(Video_4).setMediaId("Video_4").setTag("Video_4").build(),
            )

            _playerState.update {
                ExoPlayer.Builder(context)
                    .build().apply {
                        setMediaItems(mediaItems)
                        prepare()
                        playWhenReady = true
                    }
            }

            trackMediaItemTransitions()
            addAnalytics()
        }
    }

/*
    fun buildOkHttoDataSourceFactory(context: Context): DataSource.Factory {
        val simpleCache = ExoPlayerCache.getSimpleCache(context)

        val okHttpClient = OkHttpClient.Builder().build()
        val okHttpDataSourceFactory = OkHttpDataSource.Factory(okHttpClient)

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(okHttpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        return cacheDataSourceFactory
    }
*/
    fun executeAction(playerAction: PlayerAction) {
        DLog.method(TAG, "executeAction(): $playerAction")

        when (playerAction.actionType) {
            ActionType.PLAY -> _playerState.value?.play()
            ActionType.PAUSE -> _playerState.value?.pause()
            ActionType.REWIND -> _playerState.value?.rewind()
            ActionType.FORWARD -> _playerState.value?.forward()
            ActionType.NEXT -> _playerState.value?.playNext()
            ActionType.PREVIOUS -> _playerState.value?.playPrevious()
            ActionType.SEEK -> _playerState.value?.seekWithValidation(playerAction.data as? Long)
        }
    }

    private fun ExoPlayer.seekWithValidation(position: Long?) {
        DLog.method(TAG, "seekWithValidation(): $position")

        position?.let {
            seekTo(position)
        }
    }

    private fun ExoPlayer.rewind() {
        DLog.method(TAG, "rewind()")

        val newPosition = (currentPosition - 10_000).coerceAtLeast(0)
        seekTo(newPosition)
    }

    private fun ExoPlayer.forward() {
        DLog.method(TAG, "forward()")

        val newPosition = (currentPosition + 10_000).coerceAtMost(duration)
        seekTo(newPosition)
    }

    private fun ExoPlayer.playNext() {
        DLog.method(TAG, "playNext()")

        if (hasNextMediaItem()) {
            val nextIndex = currentMediaItemIndex + 1
            val mediaItemId = getMediaItemAt(nextIndex)
            val seekPosition = hashMapVideoStates[mediaItemId.mediaId]?.currentPosition ?: 0L
            seekTo(nextIndex, seekPosition)
        }
    }

    private fun ExoPlayer.playPrevious() {
        DLog.method(TAG, "playPrevious()")

        if (isCommandAvailable(Player.COMMAND_SEEK_TO_MEDIA_ITEM) && hasPreviousMediaItem()) {
            val previousIndex = currentMediaItemIndex - 1
            val mediaItemId = getMediaItemAt(previousIndex)
            val seekPosition = hashMapVideoStates[mediaItemId.mediaId]?.currentPosition ?: 0L
            seekTo(previousIndex, seekPosition)
        }
    }

    private fun currentMediaItemTag(): String? = _playerState.value?.currentMediaItem?.localConfiguration?.tag as? String

    private fun trackMediaItemTransitions() {
        DLog.method(TAG, "trackMediaItemTransitions()")

        _playerState.value?.addListener(
            object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    DLog.method(TAG, "onMediaItemTransition(): ${mediaItem?.mediaId}, $reason}")
                    _playerState.value?.currentMediaItemIndex?.let {
                        checkAndResetPreviousMediaItemProgress(it)
                    }
                }
            },
        )
    }

    private fun addAnalytics() {
        DLog.method(TAG, "addAnalytics()")

        if (::analytics.isInitialized.not()) {
            analytics = LearningsPlayerAnalytics(_playerState.value)
        }
        _playerState.value?.addAnalyticsListener(analytics)
    }

    private fun checkAndResetPreviousMediaItemProgress(currentMediaItemIndex: Int) {
        DLog.method(TAG, "checkAndResetPreviousMediaItemProgress(): $currentMediaItemIndex")

        val previousIndex = currentMediaItemIndex - 1
        if (previousIndex >= 0) {
            _playerState.value?.getMediaItemAt(previousIndex)?.let { previousMediaItem ->
                hashMapVideoStates[previousMediaItem.mediaId]?.let { previousVideoItem ->
                    if (previousVideoItem.duration - previousVideoItem.currentPosition <= 3000) {
                        hashMapVideoStates[previousMediaItem.mediaId] = previousVideoItem.copy(currentPosition = 0)
                    }
                }
            }
        }
    }

    fun updateCurrentPosition(id: String, position: Long, duration: Long) {
        DLog.method(TAG, "updateCurrentPosition(): $id, $position, $duration")

        hashMapVideoStates[id] = hashMapVideoStates[id]?.copy(position, duration) ?: VideoItem(position, duration)
    }

    override fun onCleared() {
        DLog.method(TAG, "onCleared()")
        super.onCleared()
        _playerState.value?.release()
    }
}
