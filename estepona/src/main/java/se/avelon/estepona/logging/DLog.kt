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
package se.avelon.estepona.logging

import android.util.Log

class DLog {
    companion object {

        fun forTag(clazz: Class<*>): String {
            return "@@.${clazz.simpleName}"
        }

        fun method(tag: String, o: Any) {
            Log.d(tag, "\uD83D\uDFE6$o") // Blue box
        }

        fun info(tag: String, o: Any) {
            Log.i(tag, "\uD83D\uDFE9$o") // green ball
        }

        fun warning(tag: String, o: Any) {
            Log.i(tag, "\uD83D\uDFE7$o") // orange ball
        }

        fun error(tag: String, o: Any) {
            Log.i(tag, "\uD83D\uDFE5$o") // red box
        }

        fun exception(tag: String, o: Any, e: Exception) {
            Log.e(tag, "\uD83D\uDFE5$o", e) // red box
        }

        fun test() {
            method("@@.DLog", "method")
            info("@@.DLog", "info")
            warning("@@.DLog", "warning")
            error("@@.DLog", "error")
            exception("@@.DLog", "exception", RuntimeException("exception"))
        }
    }
}
