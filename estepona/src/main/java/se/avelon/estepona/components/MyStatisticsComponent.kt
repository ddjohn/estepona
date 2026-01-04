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

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import okhttp3.Cache
import okhttp3.OkHttpClient
import se.avelon.estepona.Constants
import se.avelon.estepona.MyUtils
import se.avelon.estepona.OMX
import se.avelon.estepona.TestActivity
import se.avelon.estepona.logging.DLog

class MyStatisticsComponent(val context: Context, val chart: BubbleChart) :
    OnChartValueSelectedListener, Runnable {
    companion object {
        val TAG = DLog.forTag(MyStatisticsComponent::class.java)
    }

    private lateinit var client: OkHttpClient

    init {
        client =
            OkHttpClient.Builder()
                // .addInterceptor(HttpLoggingInterceptor())
                .cache(Cache(context.cacheDir, 1000))
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MILLISECONDS)
                .build()
        DLog.info(TestActivity.Companion.TAG, "client: $client")

        Thread(this).start()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        DLog.info(MyStatusComponent.TAG, "onValueSelected():  $e $h")

        Toast.makeText(context, "onValueSelected():  ${e?.data}", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        DLog.info(MyStatusComponent.TAG, "onNothingSelected()")
    }

    override fun run() {
        DLog.info(MyStatusComponent.TAG, "run()")
        for (stock in Constants.stocks) {
            DLog.info(MyStatusComponent.TAG, "stock=$stock")

            val latch = CountDownLatch(1)

            OMX.chart(
                client,
                stock,
                object : OMX.Callback {
                    override fun resultChartData(data: OMX.ChartData) {
                        var high = 0.0
                        var low = 99999.9
                        var avg = 0.0
                        var avgvol = 0

                        for (day in data.list) {
                            if (low > day.close) low = day.close
                            if (high < day.close) high = day.close
                            avg += day.close
                            avgvol += day.volume
                        }
                        avg /= data.list.size
                        avgvol /= data.list.size

                        val donchian = 100.0f * (data.price - low) / (high - low)
                        val vol = 100.0f * data.volume / avgvol
                        val trend = 100.0 * data.price / avg

                        val d =
                            BubbleEntry(donchian.toFloat(), vol, (trend / 100.0).toFloat(), data.id)
                        DLog.info(MyStatusComponent.TAG, "dd=$d ${d.size}")
                        val ds = BubbleDataSet(arrayListOf(d), data.id)

                        chart.data.addDataSet(ds)
                        chart.notifyDataSetChanged()
                        chart.invalidate()

                        latch.countDown()
                    }

                    override fun error() {
                        latch.countDown()
                    }
                },
            )
            latch.await()

            MyUtils.sleep(1000)
        }
    }
}

@Composable
fun MyStatistics(modifier: Modifier) {
    DLog.method(MyStatisticsComponent.TAG, "MyStatistics()")

    Row {
        Column(Modifier.weight(1f).fillMaxHeight()) {
            BubbleChartComponent(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun BubbleChartComponent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val chart =
        BubbleChart(context).apply {
            setBackgroundColor(Color.WHITE)
            data = BubbleData().apply {}
            description.text = "x = Donchian, y = Volume pct"
        }

    AndroidView(
        modifier = modifier,
        factory = { context -> chart },
        update = { view -> }, // Add animation here
    )

    val comp = MyStatisticsComponent(context, chart)
    chart.setOnChartValueSelectedListener(comp)
}
