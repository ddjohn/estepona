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
package se.avelon.estepona.jobs

import android.app.job.JobParameters
import android.app.job.JobService
import se.avelon.estepona.logging.DLog

class MyJobService : JobService(), Runnable {
    companion object {
        val TAG = DLog.forTag(MyJobService::class.java)
    }

    private lateinit var db: BubbleDatabase
    var params: JobParameters? = null

    override fun onCreate() {
        DLog.method(TAG, "onCreate()")
        super.onCreate()

        db = BubbleDatabase.getDatabase(this)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        DLog.method(TAG, "onStartJob(): $params")
        this.params = params
        Thread(this).start()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        DLog.method(TAG, "onStopJob(): $params")
        return (params != null)
    }

    override fun run() {
        DLog.method(TAG, "run()")

        DLog.info(TAG, "Doing something....")

        val omxClient = OMXClient(this)
        for (stock in omxClient.getStocks()) {
            DLog.info(TAG, "stock=$stock")
            val entity = omxClient.get(stock)
            DLog.info(TAG, "$stock insert $entity")
            db.dao().insert(entity)
        }

        jobFinished(params, false)
        params = null
    }
}
