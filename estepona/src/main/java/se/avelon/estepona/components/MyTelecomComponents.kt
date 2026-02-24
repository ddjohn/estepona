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

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.MyButton
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.logging.DLog
import se.avelon.estepona.services.MyConnectionService

class MyTelecomComponents : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyTelecomComponents::class.java)
    }

    lateinit var selfManagedphoneAccounts: MutableList<PhoneAccountHandle>
    lateinit var cpablePhoneAccounts: MutableList<PhoneAccountHandle>
    val telecomManager: TelecomManager
    val context: Context

    init {
        DLog.method(TAG, "init()")
        context = MainApplication.getApplication().applicationContext

        telecomManager = context.getSystemService(TELECOM_SERVICE) as TelecomManager

        val phoneHandle =
            PhoneAccountHandle(
                ComponentName(context, MyConnectionService::class.java),
                MyConnectionService::class.java.simpleName,
            )
        val phoneAccount =
            PhoneAccount.builder(phoneHandle, "label")
                .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
                .build()
        telecomManager.registerPhoneAccount(phoneAccount)

        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            cpablePhoneAccounts = telecomManager.callCapablePhoneAccounts.toMutableList()
            selfManagedphoneAccounts = telecomManager.selfManagedPhoneAccounts.toMutableList()
        }
    }

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    fun endCall() {
        DLog.method(TAG, "endCall()")
        telecomManager.endCall()
    }

    fun addNewIncomingCall() {
        DLog.method(TAG, "addNewIncomingCall()")
        val phoneHandle =
            PhoneAccountHandle(ComponentName(context, MyConnectionService::class.java), "id")
        val extra = Bundle()
        telecomManager.addNewIncomingCall(phoneHandle, extra)
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    fun placeCall(number: String?) {
        DLog.method(TAG, "placeCall()")

        val phoneHandle =
            PhoneAccountHandle(ComponentName(context, MyConnectionService::class.java), "Id2")
        val extra = Bundle()
        extra.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneHandle)

        val uri = Uri.fromParts("tel", number, null)
        telecomManager.placeCall(uri, extra)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MyTelecom(modifier: Modifier, viewModel: MyTelecomComponents = viewModel()) {
    DLog.method(MyTelecomComponents.TAG, "MyTelecom()")

    val context = LocalContext.current

    Row {
        MyButton(Modifier, "Place call") { viewModel.placeCall("0371-128 52") }
        MyButton(Modifier, "End ongoing call") { viewModel.endCall() }
        MyButton(Modifier, "Add new incoming call") { viewModel.addNewIncomingCall() }

        MyDropMenu("Capable phone accounts", viewModel.cpablePhoneAccounts.toList())
        MyDropMenu("Self managed phone accounts", viewModel.selfManagedphoneAccounts.toList())
    }
}
