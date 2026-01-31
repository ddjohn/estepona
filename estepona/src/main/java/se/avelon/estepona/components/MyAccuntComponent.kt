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

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.content.Context
import android.content.Context.ACCOUNT_SERVICE
import android.os.Handler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import se.avelon.estepona.compose.MyDropMenu
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

class MyAccountComponent : ViewModel(), OnAccountsUpdateListener {
    companion object {
        val TAG = DLog.forTag(MyAccountComponent::class.java)
        var initialized = false
    }

    private val _mutableValue = mutableStateOf("")
    val mutableValue: MutableState<String> = _mutableValue

    fun init(context: Context) {
        DLog.method(TAG, "init()")
        if (initialized) {
            return
        }

        val accountManager = context.getSystemService(ACCOUNT_SERVICE) as AccountManager
        accountManager.addOnAccountsUpdatedListener(
            this,
            Handler.createAsync(context.mainLooper),
            true,
        )
        initialized = true
    }

    override fun onAccountsUpdated(accounts: Array<out Account?>?) {
        DLog.method(TAG, "onAccountsUpdated(): ${accounts?.size}")

        val str = StringBuilder()
        str.append(accounts?.size)
        accounts?.forEach { str.append("\n- " + it?.name) }
        _mutableValue.value = str.toString()
    }
}

@Composable
fun MyAccount(modifier: Modifier, viewModel: MyAccountComponent = viewModel()) {
    DLog.method(MyTemplateComponents.TAG, "MyAccount()")

    val context = LocalContext.current
    viewModel.init(context)

    val accountManager = context.getSystemService(ACCOUNT_SERVICE) as AccountManager
    val account = accountManager.addAccount("account", "token", arrayOf(), null, null, null, null)

    Row {
        Column {
            MyDropMenu("Accounts", accountManager.accounts)
            MyDropMenu("Authentication Types", accountManager.authenticatorTypes)
        }
        Column { MyText(Modifier, "Account: ${viewModel.mutableValue.value}") }
    }
}
