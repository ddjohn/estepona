package se.avelon.estepona.services

import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService
import se.avelon.estepona.logging.DLog

class MyMediaBrowserService: MediaBrowserService() {
    companion object {
        val TAG = DLog.forTag(MyMediaBrowserService::class.java)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        DLog.method(TAG, "onGetRoot(): $clientPackageName, $clientUid, $rootHints")
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowser.MediaItem?>?>
    ) {
        DLog.method(TAG, "onLoadChildren(): $parentId, $result")
        TODO("Not yet implemented")
    }

}