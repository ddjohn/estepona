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
package se.avelon.estepona.os.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.R
import androidx.core.app.NotificationCompat
import se.avelon.estepona.logging.DLog

class Notif(val ctx: Context) {
    init {
        val notificationManager =
            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(CHANNELS)
    }

    fun default(id: Int, title: String, content: String) {
        DLog.method(TAG, "default(): $id, $title, $content")
        val notificationManager =
            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var builder =
            NotificationCompat.Builder(ctx, "D")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(id, builder.build())
    }

    companion object {
        private val TAG = DLog.forTag(Notif::class.java)
        private val CHANNELS =
            listOf(
                NotificationChannel("H", "High", NotificationManager.IMPORTANCE_HIGH),
                NotificationChannel("L", "Low", NotificationManager.IMPORTANCE_LOW),
                // NotificationChannel("Max", "Max", NotificationManager.IMPORTANCE_MAX),
                NotificationChannel("Min", "Min", NotificationManager.IMPORTANCE_MIN),
                NotificationChannel("N", "None", NotificationManager.IMPORTANCE_NONE),
                NotificationChannel("D", "Default", NotificationManager.IMPORTANCE_DEFAULT),
                // xNotificationChannel("U", "Unspecified",
                // NotificationManager.IMPORTANCE_UNSPECIFIED),
            )
    }
}
