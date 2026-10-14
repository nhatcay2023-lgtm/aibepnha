package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealEntity
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.ChefLogo
import com.example.ui.components.DishThumbnail
import com.example.ui.theme.BackgroundApp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.LightMint
import com.example.ui.theme.LightMintSurface
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.SecondaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeScreen(
    meals: List<MealEntity>,
    onNavigate: (AppScreen) -> Unit,
    onOpenQuickInput: () -> Unit,
    onOpenDevDrawer: () -> Unit,
    onDishClick: (MealEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMealTime by remember { mutableStateOf("Trưa") } // Sáng, Trưa, Tối
    var cookedMealIds by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
    ) {
        // Content Area (Scrollable)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            // ==========================================
            // 1. TOP HEADER: Professional & Modern Bar
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Chef Avatar with Online Green Dot
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(LightMint)
                            .clickable { onOpenDevDrawer() },
                        contentAlignment = Alignment.Center
                    ) {
                        ChefLogo(size = 40.dp)
                        // Online indicator badge
                        Box(
                            modifier = Modifier
                                .size(11.dp)
                                .clip(CircleShape)
                                .background(SecondaryGreen)
                                .border(1.5.dp, SurfaceWhite, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Xin chào, Anh!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Premium Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(LightMint)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "AI BẾP",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Thứ Ba, 13/10/2026 • 28°C",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 13-Screen Drawer Button
                    Surface(
                        shape = CircleShape,
                        color = LightMint,
                        modifier = Modifier.size(38.dp)
                    ) {
                        IconButton(onClick = onOpenDevDrawer) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu 13 màn hình",
                                tint = PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Notification Bell with badge
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            IconButton(onClick = { /* Notifications */ }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Thông báo",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp, end = 6.dp)
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 2. CARD: Tổng quan hôm nay (Enhanced Smart Budget)
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Gradient Emerald Header Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(PrimaryGreen, Color(0xFF1E9B52))
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 11.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = SurfaceWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tổng quan hôm nay",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SurfaceWhite
                                )
                            }
                            Text(
                                text = "Hạn mức: 200.000 đ/ngày",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    // Main Metric Columns (Exact 1:1 figures: 320.000 đ & 2.680.000 đ)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Chi tiêu hôm nay",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "320.000 đ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48), // Highlighted expenditure
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        // Divider line
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(38.dp)
                                .background(BorderLight)
                        )

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Còn lại trong tháng",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "2.680.000 đ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    // Monthly Budget Progress Bar & Ratio
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 12.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { 0.55f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = PrimaryGreen,
                            trackColor = Color(0xFFE2E8F0)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Đã dùng: 55% ngân sách",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Tiết kiệm tốt (+12%)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = SecondaryGreen
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 3. SMART QUICK SHORTCUTS (4 Compact Pills)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip(
                    icon = Icons.Default.ShoppingCart,
                    label = "Đi chợ",
                    bgColor = Color(0xFFF3E8FF),
                    tint = Color(0xFF9333EA),
                    onClick = { onNavigate(AppScreen.SHOPPING) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon = Icons.Default.AutoAwesome,
                    label = "Gợi ý AI",
                    bgColor = LightMint,
                    tint = PrimaryGreen,
                    onClick = onOpenQuickInput,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon = Icons.Default.AccountBalanceWallet,
                    label = "Sổ thu chi",
                    bgColor = Color(0xFFFFEDD5),
                    tint = Color(0xFFEA580C),
                    onClick = { onNavigate(AppScreen.EXPENSE) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    icon = Icons.Default.CalendarMonth,
                    label = "Tuần này",
                    bgColor = Color(0xFFE0F2FE),
                    tint = Color(0xFF0284C7),
                    onClick = { onNavigate(AppScreen.MENU) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 4. SMART AI TIP OF THE DAY BANNER
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenQuickInput() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LightMintSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFBBF7D0)))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SurfaceWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gợi ý AI Bếp Nhà hôm nay",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreenDark
                        )
                        Text(
                            text = "Mâm cơm Cá kho & Bắp cải đủ 650 kcal, giàu đạm và thanh nhiệt cho 4 người.",
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 5. SECTION: Thực đơn hôm nay (2 món)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Thực đơn hôm nay",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${meals.size.coerceAtLeast(2)} món)",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }

                Text(
                    text = "Xem thêm >",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryGreen,
                    modifier = Modifier.clickable { onNavigate(AppScreen.MENU) }
                )
            }

            // Meal Time Selector Pills (Sáng | Trưa | Tối)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Sáng", "Trưa", "Tối").forEach { time ->
                    val isSelected = selectedMealTime == time
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) PrimaryGreen else Color(0xFFF1F5F9))
                            .clickable { selectedMealTime = time }
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Bữa $time",
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) SurfaceWhite else TextSecondary
                        )
                    }
                }
            }

            // Dish Cards with rich detail & cook status
            val displayMeals = if (meals.isNotEmpty()) meals else listOf(
                MealEntity(name = "Cá kho gừng", portion = "400g cá"),
                MealEntity(name = "Bắp cải luộc", portion = "600g bắp cải")
            )

            displayMeals.take(2).forEachIndexed { index, meal ->
                val isCooked = cookedMealIds.contains(index)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { onDishClick(meal) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isCooked) Color(0xFF86EFAC) else BorderLight
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Dish Thumbnail Art
                        DishThumbnail(dishName = meal.name, size = 60.dp)

                        Spacer(modifier = Modifier.width(14.dp))

                        // Dish Details
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = meal.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                if (isCooked) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Đã nấu",
                                        tint = SecondaryGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                text = meal.portion,
                                fontSize = 12.5.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            // Quick Nutrition & Cook Time Tags
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = if (index == 0) "35p" else "10p",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = Color(0xFFF97316),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = if (index == 0) "320 kcal" else "85 kcal",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }

                        // Toggle cooked / View recipe chevron
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isCooked) LightMint else Color(0xFFF8FAFC))
                                .clickable {
                                    cookedMealIds = if (isCooked) {
                                        cookedMealIds - index
                                    } else {
                                        cookedMealIds + index
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCooked) Icons.Default.Check else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Xem",
                                tint = if (isCooked) PrimaryGreen else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 6. CARD: Nhập nhanh with prominent mic action
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenQuickInput() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Nhập nhanh",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "Giọng nói & AI",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706)
                                )
                            }
                        }
                        Text(
                            text = "Sử dụng micro để ghi nhận thực đơn, chi tiêu...",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Green Circular Mic Button (Interactive)
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(PrimaryGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Micro",
                            tint = SurfaceWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // 7. BOTTOM NAVIGATION BAR (5 items)
        // ==========================================
        AppBottomNavBar(
            currentScreen = AppScreen.HOME,
            onNavigate = onNavigate,
            onMicClick = onOpenQuickInput
        )
    }
}

@Composable
private fun QuickActionChip(
    icon: ImageVector,
    label: String,
    bgColor: Color,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceWhite,
        modifier = modifier
            .shadow(1.5.dp, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}
