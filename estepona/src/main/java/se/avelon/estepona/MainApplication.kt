package se.avelon.estepona

import android.app.Application
import se.avelon.estepona.logging.DLog

class MainApplication: Application() {
    companion object {
        val TAG: String = DLog.forTag(MainApplication::class.java)
    }

    override fun onCreate() {
        DLog.method(TAG, "onCreate()")
        super.onCreate()
    }
}