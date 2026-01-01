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

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import se.avelon.estepona.components.MyStatusComponent.TAG
import se.avelon.estepona.logging.DLog

object MyStatisticsComponent {
    val TAG = DLog.forTag(MyStatisticsComponent::class.java)
}

@Composable
fun MyStatistics(modifier: Modifier) {
    DLog.method(TAG, "MyStatistics()")

    Row {
        Column(Modifier.weight(1f).fillMaxHeight()) {
            Row(Modifier.weight(1f).background(Blue).fillMaxWidth()) {
                ChartComponent(modifier = Modifier.fillMaxSize())
            }
            Row(Modifier.weight(1f).background(Yellow).fillMaxWidth()) {
                LineChartComponent(modifier = Modifier.fillMaxSize())
            }
        }
        Column(Modifier.weight(1f).fillMaxHeight()) {
            Row(Modifier.weight(1f).background(Red).fillMaxWidth()) {
                BubbleChartComponent(modifier = Modifier.fillMaxSize())
            }
            Row(Modifier.weight(1f).background(Green).fillMaxWidth()) {
                PieChartComponent(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ChartComponent(modifier: Modifier) {
    val entries = arrayListOf<CandleEntry>()
    entries.add(CandleEntry(0.0f, 2.2f, 1.2f, 1.1f, 1.3f))
    entries.add(CandleEntry(1.0f, 2.4f, 1.6f, 1.5f, 1.5f))
    entries.add(CandleEntry(2.0f, 1.5f, 1.2f, 1.5f, 1.3f))

    val dataSet = CandleDataSet(entries, "label").apply { shadowWidth = 0.7f }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            CandleStickChart(context).apply {
                data = CandleData(dataSet)
                invalidate()
            }
        },
        update = { view -> }, // Add animation here
    )
}

@Composable
fun LineChartComponent(modifier: Modifier = Modifier) {
    val entries = arrayListOf<Entry>()
    entries.add(Entry(0.0f, 2.2f))
    entries.add(Entry(1.0f, 2.4f))
    entries.add(Entry(2.0f, 1.5f))

    val entries2 = arrayListOf<Entry>()
    entries2.add(Entry(0.0f, 2.1f))
    entries2.add(Entry(1.0f, 1.8f))
    entries2.add(Entry(2.0f, 1.6f))

    val dataSet = LineDataSet(entries, "label").apply { setColor(Color.RED, 255) }
    val dataSet2 = LineDataSet(entries2, "label2").apply {}

    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                data = LineData(dataSet, dataSet2).apply { setBorderColor(Color.BLUE) }
            }
        },
        update = { view -> }, // Add animation here
    )
}

@Composable
fun BubbleChartComponent(modifier: Modifier = Modifier) {
    val entries = arrayListOf<BubbleEntry>()
    entries.add(BubbleEntry(0.0f, 2.2f, 1f))
    entries.add(BubbleEntry(1.0f, 2.4f, 2f))
    entries.add(BubbleEntry(-0.5f, 1.5f, 1f))

    val entries2 = arrayListOf<BubbleEntry>()
    entries2.add(BubbleEntry(-1.0f, 2.2f, 1f))
    entries2.add(BubbleEntry(-1.0f, 2.4f, 2f))
    entries2.add(BubbleEntry(-0.5f, 1.5f, 1f))

    val dataSet = BubbleDataSet(entries, "label").apply {}
    val dataSet2 = BubbleDataSet(entries2, "label2").apply {}

    AndroidView(
        modifier = modifier,
        factory = { context ->
            BubbleChart(context).apply { data = BubbleData(dataSet, dataSet2).apply {} }
        },
        update = { view -> }, // Add animation here
    )
}

@Composable
fun PieChartComponent(modifier: Modifier = Modifier) {
    val entries = arrayListOf<PieEntry>()
    entries.add(PieEntry(15f, "Alpha"))
    entries.add(PieEntry(20f, "Beta"))
    entries.add(PieEntry(18f, "Gamma"))

    val dataSet = PieDataSet(entries, "label")

    AndroidView(
        modifier = modifier,
        factory = { context -> PieChart(context).apply { data = PieData(dataSet) } },
        update = { view -> }, // Add animation here
    )
}

/*
fun LineChart.setupLineChart(): LineChart = this.apply {
    setTouchEnabled(true)
    isDragEnabled = true
    setScaleEnabled(true)
    setPinchZoom(true)
    description.isEnabled = false

    // set up x-axis
    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        // axisMinimum = -10f
        // axisMaximum = 10f
    }

    // set up y-axis
    axisLeft.apply {
        // axisMinimum = -5f
        // axisMaximum = 5f
        setDrawGridLines(false)
    }

    axisRight.isEnabled = false
}
fun List<Float>.createDataSetWithColor(datasetColor: Int = android.graphics.Color.GREEN, label: String = "No Label"): LineDataSet {
    // List<Float> -> List<Entry>
    val entries = this.mapIndexed { index, value ->
        Entry(index.toFloat(), value)
    }
    // List<Entry> -> LineDataSet
    return LineDataSet(entries, label).apply {
        color = datasetColor
        setDrawFilled(false)
        setDrawCircles(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
}


 */
