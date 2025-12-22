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

import android.media.MediaDataSource
import se.avelon.estepona.logging.DLog

class MyMediaDataSource : MediaDataSource(), Runnable {
    companion object {
        val TAG = DLog.forTag(MyMediaDataSource::class.java)

        val VIDEO_URL: String =
            "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/fileSequence0.ts"
    }

    private var videoBuffer: ByteArray? = null
    private var listener: VideoDownloadListener? = null
    private var isDownloading = false

    override fun getSize(): Long {
        DLog.method(TAG, "getSize()")
        return videoBuffer!!.size.toLong()
    }

    override fun readAt(position: Long, buffer: ByteArray?, offset: Int, size: Int): Int {
        DLog.method(TAG, "readAt(): $position, $offset, $size")
        val length = videoBuffer!!.size
        if (position >= length) {
            return -1 // -1 indicates EOF
        }

        var size2 = size.toLong()
        if (position + size > length) {
            size2 -= (position + size) - length
        }
        System.arraycopy(videoBuffer, position as Int, buffer, offset, size2.toInt())
        return size2.toInt()
    }

    override fun close() {
        DLog.method(TAG, "close()")
    }

    override fun run() {
        TODO("Not yet implemented")
    }

    interface VideoDownloadListener {
        fun onVideoDownloaded()

        fun onVideoDownloadError(e: Exception?)
    }
}
