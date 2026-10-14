package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyExpenseBar
import com.example.data.model.ExpenseCategorySummary
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DonutChartView(
    categories: List<ExpenseCategorySummary>,
    totalText: String = "920.000 đ",
    subText: String = "Tổng chi",
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = this.size.width * 0.22f
            val arcSize = Size(this.size.width - strokeWidth, this.size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            var currentAngle = -90f
            for (cat in categories) {
                val sweepAngle = (cat.percentage / 100f) * 360f
                drawArc(
                    color = Color(cat.colorHex),
                    startAngle = currentAngle,
                    sweepAngle = sweepAngle - 2f, // slight gap between segments
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )
                currentAngle += sweepAngle
            }
        }

        // Center Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = totalText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subText,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun DailyExpenseBarChart(
    bars: List<DailyExpenseBar>,
    maxAmountInK: Int = 200,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 130.dp
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            bars.forEach { bar ->
                val ratio = (bar.amountInK.toFloat() / maxAmountInK).coerceIn(0.1f, 1f)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${bar.amountInK}K",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(width = 14.dp, height = (chartHeight.value * 0.65f * ratio).dp)
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(
                                if (bar.dayLabel == "T6") SecondaryGreen else PrimaryGreen
                            )
                    )
                    Text(
                        text = bar.dayLabel,
                        fontSize = 11.sp,
                        fontWeight = if (bar.dayLabel == "T6") FontWeight.Bold else FontWeight.Normal,
                        color = if (bar.dayLabel == "T6") PrimaryGreen else TextSecondary,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }
    }
}
