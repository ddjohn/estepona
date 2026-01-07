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
package se.avelon.estepona.components

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import se.avelon.estepona.logging.DLog

object MyTimeComponents {
    val TAG = DLog.forTag(MyTimeComponents::class.java)
}

@Composable
fun MyTime(modifier: Modifier) {
    DLog.method(MyTimeComponents.TAG, "MyTime()")

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(System.currentTimeMillis())
    calendar.set(Calendar.HOUR_OF_DAY, 16)
    calendar.set(Calendar.MINUTE, 45)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val info = AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 3600, null)

    val intent: Intent = Intent(context, MyTimeComponents::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.getTimeInMillis(),
        AlarmManager.INTERVAL_DAY,
        pendingIntent,
    )
    // alarmManager.setAlarmClock(info, pendingIntent)
}
