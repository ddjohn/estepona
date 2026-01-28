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

import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.content.Context.ACCOUNT_SERVICE
import android.os.Handler
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.logging.DLog

object MyAccountComponent {
    val TAG = DLog.forTag(MyAccountComponent::class.java)
}

@Composable
fun MyAccount(modifier: Modifier) {
    DLog.method(MyTemplateComponents.TAG, "MyAccount()")

    val context = LocalContext.current
    val accountManager = context.getSystemService(ACCOUNT_SERVICE) as AccountManager

    accountManager.addOnAccountsUpdatedListener(
        OnAccountsUpdateListener {
            DLog.method(MyAccountComponent.TAG, "OnAccountsUpdatedListener(): $it")
            Toast.makeText(context, "OnAccountsUpdatedListener(): $it", Toast.LENGTH_SHORT).show()
        },
        Handler.createAsync(context.mainLooper),
        true,
    )

    val account = accountManager.addAccount("account", "token", arrayOf(), null, null, null, null)
    DLog.info(MyAccountComponent.TAG, "account=$account")

    Row {
        MyDropMenu("Accounts", accountManager.accounts)
        MyDropMenu("Authentication Types", accountManager.authenticatorTypes)
    }
}
