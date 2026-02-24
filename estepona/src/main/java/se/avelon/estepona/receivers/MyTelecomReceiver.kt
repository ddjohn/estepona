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
package se.avelon.estepona.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import se.avelon.estepona.logging.DLog

class MyTelecomReceiver : BroadcastReceiver() {
    companion object {
        val TAG = DLog.forTag(MyTelecomReceiver::class.java)
    }

    override fun onReceive(context: Context, intent: Intent) {
        DLog.method(TAG, "onReceive(): $intent")
    }
}
