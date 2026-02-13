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
package se.avelon.estepona

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import kotlin.concurrent.thread
import se.avelon.estepona.jobs.MyJobService
import se.avelon.estepona.logging.DLog

class MainApplication : Application() {
    companion object {
        val TAG = DLog.forTag(MainApplication::class.java)
    }

    override fun onCreate() {
        DLog.method(TAG, "onCreate()")
        super.onCreate()

        val jobInfo =
            JobInfo.Builder(666, ComponentName(this, MyJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setTraceTag("FromMain")
                .setPeriodic((15 * 60 * 1000).toLong())
                .build()

        val jobScheduler =
            applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)

        thread(true) {
            while (true) {
                jobScheduler.allPendingJobs.forEach { DLog.debug(TAG, "job: $it") }
                Thread.sleep(10000)
            }
        }
    }
}
