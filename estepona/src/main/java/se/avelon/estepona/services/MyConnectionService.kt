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
package se.avelon.estepona.services

import android.telecom.CallAudioState
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.widget.Toast
import se.avelon.estepona.logging.DLog

class MyConnectionService : ConnectionService() {
    companion object {
        val TAG = DLog.forTag(MyConnectionService::class.java)
    }

    override fun onCreate() {
        DLog.method(TAG, "onCreate()")
        super.onCreate()
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest,
    ): Connection {
        DLog.method(TAG, "onCreateIncomingConnection()")

        // return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request);
        val callConnection: MyConnection = MyConnection()
        callConnection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)
        callConnection.setCallerDisplayName("David Johansson", TelecomManager.PRESENTATION_ALLOWED)
        // callConnection.setConnectionCapabilities(CAPABILITY_HOLD|CAPABILITY_SUPPORT_HOLD);
        callConnection.setAddress(request.getAddress(), TelecomManager.PRESENTATION_ALLOWED)

        return callConnection
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest,
    ): Connection {
        DLog.method(TAG, "onCreateOutgoingConnection(): " + " - " + request.getAddress())

        // return super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request);
        val callConnection: MyConnection = MyConnection()
        callConnection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)
        callConnection.setCallerDisplayName("David Johansson", TelecomManager.PRESENTATION_ALLOWED)
        // callConnection.setConnectionCapabilities(CAPABILITY_HOLD|CAPABILITY_SUPPORT_HOLD);
        callConnection.setAddress(request.getAddress(), TelecomManager.PRESENTATION_ALLOWED)

        return callConnection
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest,
    ) {
        DLog.method(TAG, "onCreateIncomingConnectionFailed(): " + request.getAddress())
        Toast.makeText(getApplicationContext(), "Incoming failed...", Toast.LENGTH_SHORT).show()
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest,
    ) {
        DLog.method(TAG, "onCreateOutgoingConnectionFailed(): " + request.getAddress())
        Toast.makeText(getApplicationContext(), "Outgoing failed...", Toast.LENGTH_SHORT).show()
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    class MyConnection : Connection() {
        public override fun onCallAudioStateChanged(state: CallAudioState?) {
            DLog.method(TAG, "onCallAudioStateChanged(): " + state)
            super.onCallAudioStateChanged(state)
        }

        public override fun onDisconnect() {
            DLog.method(TAG, "onDisconnect()")
            super.onDisconnect()
        }

        public override fun onHold() {
            DLog.method(TAG, "onHold()")
            super.onHold()
        }

        public override fun onUnhold() {
            DLog.method(TAG, "onUnhold()")
            super.onUnhold()
        }

        public override fun onAnswer() {
            DLog.method(TAG, "onAnswer()")
            super.onAnswer()
        }

        public override fun onReject() {
            DLog.method(TAG, "onReject()")
            super.onReject()
        }

        public override fun onShowIncomingCallUi() {
            DLog.method(TAG, "onShowIncomingCallUi()")
            super.onShowIncomingCallUi()
        }

        companion object {
            private val TAG: String = MyConnection::class.java.getSimpleName()
        }
    }
}
