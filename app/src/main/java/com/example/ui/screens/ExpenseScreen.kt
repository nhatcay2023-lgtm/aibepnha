package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyExpenseBar
import com.example.data.model.ExpenseCategorySummary
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.DailyExpenseBarChart
import com.example.ui.components.DonutChartView
import com.example.ui.theme.BackgroundApp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

@Composable
fun ExpenseScreen(
    categorySummaries: List<ExpenseCategorySummary>,
    dailyBars: List<DailyExpenseBar>,
    onNavigate: (AppScreen) -> Unit,
    onOpenQuickInput: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Tuần") } // Ngày, Tuần, Tháng

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Bar: Title & Calendar Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(24.dp))
                Text(
                    text = "Chi tiêu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = { /* Calendar picker */ }) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Lịch",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Segmented Control: Ngày | Tuần | Tháng
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Ngày", "Tuần", "Tháng").forEach { tab ->
                    val isSelected = tab == selectedTab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryGreen else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SurfaceWhite else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Date Range with arrows: < 05/10 - 11/10/2026 >
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Trước",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "  05/10 – 11/10/2026  ",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Sau",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 1: Tổng quan tuần
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Tổng quan tuần",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Two columns: Left (Biểu đồ phân bổ) & Right (Danh sách chi tiêu)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left column
                        Column(modifier = Modifier.weight(1.1f)) {
                            Text(
                                text = "Biểu đồ phân bổ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Donut Chart
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                DonutChartView(
                                    categories = categorySummaries,
                                    size = 125.dp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Legend list
                            categorySummaries.forEach { cat ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(Color(cat.colorHex))
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "${cat.category} ${formatVnd(cat.amount)} (${cat.percentage}%)",
                                        fontSize = 9.5.sp,
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Right column: Danh sách chi tiêu
                        Column(modifier = Modifier.weight(0.9f)) {
                            Text(
                                text = "Danh sách chi tiêu",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            categorySummaries.forEach { item ->
                                ExpenseRowItem(
                                    name = item.category,
                                    amount = formatVnd(item.amount),
                                    icon = getCategoryIcon(item.category),
                                    iconTint = Color(item.colorHex)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Chi tiêu theo ngày
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Chi tiêu theo ngày",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    DailyExpenseBarChart(bars = dailyBars)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Navigation Bar
        AppBottomNavBar(
            currentScreen = AppScreen.EXPENSE,
            onNavigate = onNavigate,
            onMicClick = onOpenQuickInput
        )
    }
}

@Composable
private fun ExpenseRowItem(
    name: String,
    amount: String,
    icon: ImageVector,
    iconTint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = iconTint,
                    modifier = Modifier.size(13.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = name, fontSize = 11.5.sp, color = TextPrimary)
        }
        Text(
            text = amount,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
    }
}

private fun getCategoryIcon(category: String): ImageVector {
    return when (category) {
        "Ăn uống" -> Icons.Default.Restaurant
        "Đi chợ" -> Icons.Default.ShoppingCart
        "Sinh hoạt" -> Icons.Default.Home
        "Cá nhân" -> Icons.Default.Person
        "Đi lại" -> Icons.Default.DirectionsCar
        else -> Icons.Default.MoreHoriz
    }
}

private fun formatVnd(amount: Long): String {
    return "%,d đ".format(amount).replace(',', '.')
}
