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
package se.avelon.estepona.layout.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import se.avelon.estepona.MainApplication
import se.avelon.estepona.logging.DLog

class MyMGaugeWidgetComponent : ViewModel() {
    companion object {
        val TAG = DLog.forTag(MyMGaugeWidgetComponent::class.java)
    }

    init {
        DLog.method(TAG, "init()")
        val context = MainApplication.getApplication().applicationContext
    }
}

@Composable
fun MyGaugeWidget(size: Int, value: Int, unit: String = "%", min: Int = 0, max: Int = 100) {
    Box(
        modifier =
            Modifier.size(size.dp)
                .border(width = 2.dp, color = Red, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier =
                Modifier.size((0.85 * size).dp)
                    .border(width = 2.dp, color = Blue, shape = RoundedCornerShape(16.dp))
        ) {
            drawArc(
                color = Color.DarkGray,
                startAngle = 150f,
                sweepAngle = 240f,
                useCenter = false,
                style = Stroke(width = 0.13f * size, cap = StrokeCap.Round),
            )

            drawArc(
                color = Color.Gray,
                startAngle = 150f,
                sweepAngle = 240f * value / (max - min),
                useCenter = false,
                style = Stroke(width = 0.13f * size, cap = StrokeCap.Round),
            )

            drawCircle(
                Brush.radialGradient(listOf(Color.Black.copy(alpha = 0.1f), Color.Transparent)),
                size / 2f,
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "$value", fontSize = (0.1 * size).sp, lineHeight = 28.sp)
            Text(text = "$unit", fontSize = (0.08 * size).sp, lineHeight = 24.sp)
        }
    }
}
