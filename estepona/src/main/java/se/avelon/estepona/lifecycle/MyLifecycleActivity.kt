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
package se.avelon.estepona.lifecycle

import android.app.Activity
import android.os.Bundle
import se.avelon.estepona.logging.DLog

open class MyLifecycleActivity : Activity() {
  companion object {
    val TAG = DLog.forTag(MyLifecycleActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    DLog.method(TAG, "onCreate(): $savedInstanceState")
    super.onCreate(savedInstanceState)
  }

  override fun onDestroy() {
    DLog.method(TAG, "onDestroy()")
    super.onDestroy()
  }

  override fun onStart() {
    DLog.method(TAG, "onStart()")
    super.onStart()
  }

  override fun onResume() {
    DLog.method(TAG, "onResume()")
    super.onResume()
  }

  override fun onPause() {
    DLog.method(TAG, "onPause()")
    super.onPause()
  }

  override fun onStop() {
    DLog.method(TAG, "onStop()")
    super.onStop()
  }

  override fun onRestart() {
    DLog.method(TAG, "onRestart()")
    super.onRestart()
  }
}
