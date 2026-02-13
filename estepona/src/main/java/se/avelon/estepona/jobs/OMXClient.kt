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
package se.avelon.estepona.jobs

import android.content.Context
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.math.tanh
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import se.avelon.estepona.logging.DLog

class OMXClient(context: Context) {
    companion object {
        val TAG = DLog.forTag(OMXClient::class.java)

        val url_v8 = "https://query2.finance.yahoo.com/v8/finance"
    }

    private lateinit var client: OkHttpClient

    init {
        DLog.method(TAG, "init()")
        client =
            OkHttpClient.Builder()
                // .cache(Cache(context.cacheDir, 1000))
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                .writeTimeout(1000, TimeUnit.MILLISECONDS)
                .build()
    }

    fun getStocks(): List<String> {
        DLog.method(TAG, "getStocks()")
        return listOf("ERIC-B.ST")
    }

    fun get(stock: String): BubbleEntity {
        DLog.method(TAG, "get(): $stock")

        var x = 50.0f
        var y = 50.0f
        var size = 1.0f

        val end = System.currentTimeMillis() / 1000
        val start = end - 3 * 30 * 24 * 60 * 60

        val request =
            Request.Builder()
                .url("$url_v8/chart/$stock?period1=$start&period2=$end&interval=1d")
                .get()
                .build()
        DLog.info(TAG, "request=$request")

        val latch = CountDownLatch(1)
        client
            .newCall(request)
            .enqueue(
                object : okhttp3.Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        latch.countDown()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = JSONObject(response.body.string())
                        val meta =
                            json
                                .getJSONObject("chart")
                                .getJSONArray("result")
                                .getJSONObject(0)
                                .getJSONObject("meta")
                        val quotes =
                            json
                                .getJSONObject("chart")
                                .getJSONArray("result")
                                .getJSONObject(0)
                                .getJSONObject("indicators")
                                .getJSONArray("quote")
                                .getJSONObject(0)
                        val timestamps =
                            json
                                .getJSONObject("chart")
                                .getJSONArray("result")
                                .getJSONObject(0)
                                .getJSONArray("timestamp")

                        var lowest: Double = 999999.0
                        var highest: Double = 0.0
                        var latest: Double = 0.0
                        var average: Double = 0.0

                        val arrClosed = quotes.getJSONArray("close")
                        for (i in 0 until arrClosed.length()) {
                            lowest = lowest.coerceAtMost(arrClosed.getDouble(i))
                            highest = highest.coerceAtLeast(arrClosed.getDouble(i))
                            latest = arrClosed.getDouble(i)

                            average += arrClosed.getDouble(i)
                        }
                        average /= arrClosed.length()
                        DLog.info(
                            TAG,
                            "lowest=$lowest, highest=$highest, latest=$latest, average=$average",
                        )

                        val arrVolume = quotes.getJSONArray("volume")

                        var volaverage: Double = 0.0
                        var vollatest: Double = 0.0

                        for (i in 0 until arrVolume.length()) {
                            vollatest = arrVolume.getDouble(i)
                            volaverage += arrVolume.getDouble(i)
                        }
                        volaverage /= arrVolume.length()

                        DLog.info(TAG, "vollatest=$vollatest, volaverage=$volaverage")

                        x = 100.0f * (latest - lowest).toFloat() / (highest - lowest).toFloat()
                        y = 100.0f * (vollatest / volaverage).toFloat()
                        size = 10.0f * (1.0 + tanh((latest - average) / average)).toFloat()

                        // DLog.info(TAG, "meta=$meta")
                        // DLog.info(TAG, "quotes=$quotes")
                        // DLog.info(TAG, "timestamps=$timestamps")
                        latch.countDown()
                    }
                }
            )
        latch.await()

        return BubbleEntity(label = stock, x = x, y = y, size = size)
    }
}
