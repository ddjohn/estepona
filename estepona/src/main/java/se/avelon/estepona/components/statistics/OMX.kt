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
package se.avelon.estepona.components.statistics

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import se.avelon.estepona.logging.DLog

class OMX {
    interface Callback {
        fun resultChartData(data: ChartData)

        fun error()
    }

    data class ChartData(
        val id: String,
        val name: String,
        val volume: Int,
        val price: Double,
        val low: Double,
        val high: Double,
        val list: List<DayData> = listOf(),
    )

    data class DayData(
        val timestamp: Int = 0,
        val high: Double = 0.0,
        val close: Double = 0.0,
        val open: Double = 0.0,
        val low: Double = 0.0,
        val volume: Int = 0,
    )

    companion object {
        val TAG = DLog.forTag(OMX::class.java)

        val url_v8 = "https://query2.finance.yahoo.com/v8/finance"

        // https://query2.finance.yahoo.com/v8/finance/chart/ERIC-B.ST
        fun chart(client: OkHttpClient, stock: String, callback: Callback) {
            val end = System.currentTimeMillis() / 1000
            val start = end - 3 * 30 * 24 * 60 * 60

            val request =
                Request.Builder()
                    .url("$url_v8/chart/$stock?period1=$start&period2=$end&interval=1d")
                    .get()
                    .build()

            client
                .newCall(request)
                .enqueue(
                    object : okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            DLog.exception(TAG, "onFailure(): ${call.request().url}, $e", e)
                            callback.error()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            DLog.method(TAG, "onResponse(): ${call.request().url}, $response")

                            try {
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

                                val days = ArrayList<DayData>()
                                for (i in timestamps.length() - 20 until timestamps.length()) {
                                    days.add(
                                        DayData(
                                            timestamps.getInt(i),
                                            quotes.getJSONArray("high").getDouble(i),
                                            quotes.getJSONArray("close").getDouble(i),
                                            quotes.getJSONArray("open").getDouble(i),
                                            quotes.getJSONArray("low").getDouble(i),
                                            quotes.getJSONArray("volume").getInt(i),
                                        )
                                    )
                                }

                                callback.resultChartData(
                                    ChartData(
                                        meta.getString("symbol"),
                                        meta.getString("longName"),
                                        meta.getInt("regularMarketVolume"),
                                        meta.getDouble("regularMarketPrice"),
                                        meta.getDouble("regularMarketDayLow"),
                                        meta.getDouble("regularMarketDayHigh"),
                                        days,
                                    )
                                )
                            } catch (e: JSONException) {
                                DLog.exception(TAG, "json", e)
                                callback.error()
                            }
                        }
                    }
                )
        }
    }
}
