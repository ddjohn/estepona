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

import android.car.Car
import android.car.hardware.power.CarPowerManager
import android.car.hardware.power.CarPowerPolicy
import android.car.hardware.power.CarPowerPolicyFilter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.concurrent.thread
import se.avelon.estepona.MainApplication
import se.avelon.estepona.compose.MyText
import se.avelon.estepona.logging.DLog

class MyPowerComponent : ViewModel(), CarPowerManager.CarPowerPolicyListener {
    companion object {
        val TAG = DLog.forTag(MyPowerComponent::class.java)
    }

    val policy: MutableState<String> = mutableStateOf("")
    val power: MutableState<Int> = mutableStateOf(1)

    val num: MutableState<Int> = mutableIntStateOf(0)

    init {
        DLog.method(TAG, "init()")
        val context = MainApplication.getApplication().applicationContext

        val car = Car.createCar(context)
        val powerManager = car.getCarManager(Car.POWER_SERVICE) as CarPowerManager

        policy.value = powerManager.currentPowerPolicy.policyId
        powerManager.addPowerPolicyListener(
            context.mainExecutor,
            CarPowerPolicyFilter.Builder().build(),
            this as CarPowerManager.CarPowerPolicyListener?,
        )

        thread(true) {
            while (true) {
                Thread.sleep(1000)
                num.value++
            }
        }

        val method = powerManager.javaClass.getMethod("getPowerState")
        power.value = method.invoke(powerManager) as Int

        val carPowerStateListener =
            Class.forName("android.car.hardware.power.CarPowerManager\$CarPowerStateListener")
        val proxy =
            Proxy.newProxyInstance(
                context.classLoader,
                arrayOf(carPowerStateListener),
                object : InvocationHandler {
                    override fun invoke(
                        proxy: Any?,
                        method: Method?,
                        args: Array<out Any?>?,
                    ): Any? {
                        DLog.method(TAG, "invoke(): $method, $args")
                        return null
                    }
                },
            )

        for (method in powerManager.javaClass.methods) {
            DLog.method(TAG, "method(): $method")
            if (method.name.equals("setListener")) {
                method.invoke(powerManager, context.mainExecutor, proxy)
                break
            }
        }
        // val method = powerManager.javaClass.getMethod("setListener", Executor::class.java,
        // proxy.javaClass)
    }

    override fun onPolicyChanged(p0: CarPowerPolicy?) {
        DLog.method(TAG, "onPolicyChanged(): $p0")
        policy.value = "${p0?.policyId}"
    }
}

@Composable
fun MyPower(modifier: Modifier, viewModel: MyPowerComponent = viewModel()) {
    DLog.method(MyPowerComponent.TAG, "MyTemplate()")

    Row {
        Column {
            MyText(Modifier, "Policy: ${viewModel.policy.value}")
            MyText(Modifier, "Power: ${viewModel.power.value}")
            MyText(Modifier, "Num: ${viewModel.num.value}")
        }
    }
}
