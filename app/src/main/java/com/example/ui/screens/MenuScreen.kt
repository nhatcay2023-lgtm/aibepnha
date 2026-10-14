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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MealEntity
import com.example.data.model.ShoppingItemEntity
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.DishDetailScalingDialog
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
fun MenuScreen(
    meals: List<MealEntity>,
    shoppingItems: List<ShoppingItemEntity>,
    aiResponse: String?,
    isAiLoading: Boolean,
    onNavigate: (AppScreen) -> Unit,
    onOpenQuickInput: () -> Unit,
    onOpenDevDrawer: () -> Unit,
    onToggleMealCooked: (Int, Boolean) -> Unit,
    onAddMeal: (String, String, String, String, String, String, String, Long) -> Unit,
    onDeleteMeal: (MealEntity) -> Unit,
    onCopyMeal: (MealEntity, String, String) -> Unit,
    onScaleIngredients: (String, Int, Int) -> String,
    onGenerateShoppingList: () -> Unit,
    onToggleShoppingItem: (Int, Boolean) -> Unit,
    onDeleteShoppingItem: (Int) -> Unit,
    onAskAi: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter State: "Hôm nay", "Tuần này", "Tháng này"
    var selectedTimePeriod by remember { mutableStateOf("Tuần này") }
    var currentWeekOffset by remember { mutableIntStateOf(0) } // 0: current week (Tuần 42), -1: last week, 1: next week

    // Dialog & Sheet States
    var selectedDishForDetail by remember { mutableStateOf<MealEntity?>(null) }
    var showAddMealDialog by remember { mutableStateOf(false) }
    var preselectedDateForAdd by remember { mutableStateOf("13/10/2026") }
    var showShoppingListDialog by remember { mutableStateOf(false) }
    var showAiMenuDialog by remember { mutableStateOf(false) }

    // Week days definitions
    val weekDays = listOf(
        Pair("Thứ Hai", "12/10/2026"),
        Pair("Thứ Ba (Hôm nay)", "13/10/2026"),
        Pair("Thứ Tư", "14/10/2026"),
        Pair("Thứ Năm", "15/10/2026"),
        Pair("Thứ Sáu", "16/10/2026"),
        Pair("Thứ Bảy", "17/10/2026"),
        Pair("Chủ Nhật", "18/10/2026")
    )

    // Filter meals according to selected period
    val displayedMeals = when (selectedTimePeriod) {
        "Hôm nay" -> meals.filter { it.date == "13/10/2026" }
        "Tháng này" -> meals
        else -> meals // Tuần này
    }

    val totalWeeklyCost = displayedMeals.sumOf { it.estimatedCost }
    val cookedCount = displayedMeals.count { it.isCooked }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
    ) {
        // ==========================================
        // 1. THANH TIÊU ĐỀ (HEADER)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Thực đơn",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LightMint)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "KẾ HOẠCH BỮA ĂN",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                }
                Text(
                    text = "Lên kế hoạch bữa ăn, nấu ngon và tiết kiệm",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Button AI Gợi ý
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = LightMint,
                    modifier = Modifier
                        .clickable { showAiMenuDialog = true }
                        .testTag("btn_ai_suggest")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Gợi ý",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Gợi ý",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Button Thêm thực đơn mới
                Surface(
                    shape = CircleShape,
                    color = PrimaryGreen,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            preselectedDateForAdd = "13/10/2026"
                            showAddMealDialog = true
                        }
                        .testTag("btn_add_meal")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Thêm món",
                            tint = SurfaceWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Menu 13 màn hình
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.size(36.dp)
                ) {
                    IconButton(onClick = onOpenDevDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu drawer",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // ==========================================
        // SCROLLABLE CONTENT BODY
        // ==========================================
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 2. CHUYỂN ĐỔI THỜI GIAN (Hôm nay | Tuần này | Tháng này)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Hôm nay", "Tuần này", "Tháng này").forEach { tab ->
                        val isSelected = selectedTimePeriod == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) SurfaceWhite else Color.Transparent)
                                .clickable { selectedTimePeriod = tab }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PrimaryGreen else TextSecondary
                            )
                        }
                    }
                }
            }

            // Thanh điều hướng tuần (khi chọn Tuần này)
            if (selectedTimePeriod == "Tuần này") {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LightMintSurface)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { currentWeekOffset-- },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Tuần trước",
                                tint = PrimaryGreen
                            )
                        }

                        val weekLabel = when (currentWeekOffset) {
                            0 -> "Tuần 42 (12/10 - 18/10/2026)"
                            -1 -> "Tuần 41 (05/10 - 11/10/2026)"
                            1 -> "Tuần 43 (19/10 - 25/10/2026)"
                            else -> "Tuần ${42 + currentWeekOffset}"
                        }

                        Text(
                            text = weekLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryGreenDark
                        )

                        IconButton(
                            onClick = { currentWeekOffset++ },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Tuần sau",
                                tint = PrimaryGreen
                            )
                        }
                    }
                }
            }

            // 3. TÓM TẮT THỰC ĐƠN & KẾT NỐI ĐI CHỢ
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(BorderLight)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Chi phí dự kiến tuần",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "%,d đ".format(if (totalWeeklyCost > 0) totalWeeklyCost else 850000).replace(',', '.'),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Tiến độ nấu nướng",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${displayedMeals.size} món",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = " • $cookedCount đã nấu",
                                        fontSize = 12.sp,
                                        color = SecondaryGreen,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Nút Tạo danh sách đi chợ từ thực đơn
                        Button(
                            onClick = {
                                onGenerateShoppingList()
                                onNavigate(AppScreen.SHOPPING)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("btn_open_shopping_list")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🛒 Mở module Đi chợ từ thực đơn (${shoppingItems.size} mục)",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 4. DANH SÁCH THỰC ĐƠN THEO NGÀY
            val targetDays = when (selectedTimePeriod) {
                "Hôm nay" -> listOf(Pair("Thứ Ba (Hôm nay)", "13/10/2026"))
                "Tháng này" -> weekDays
                else -> weekDays
            }

            items(targetDays) { dayInfo ->
                val dayTitle = dayInfo.first
                val dayDate = dayInfo.second
                val dayMeals = displayedMeals.filter { it.date == dayDate }
                val isToday = dayDate == "13/10/2026"

                DailyMenuCard(
                    dayTitle = dayTitle,
                    dayDate = dayDate,
                    isToday = isToday,
                    meals = dayMeals,
                    onAddMealToDay = {
                        preselectedDateForAdd = dayDate
                        showAddMealDialog = true
                    },
                    onDishClick = { meal ->
                        selectedDishForDetail = meal
                    },
                    onToggleCooked = onToggleMealCooked
                )
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // ==========================================
        // 5. BOTTOM NAVIGATION BAR
        // ==========================================
        AppBottomNavBar(
            currentScreen = AppScreen.MENU,
            onNavigate = onNavigate,
            onMicClick = onOpenQuickInput
        )
    }

    // ==========================================
    // DIALOGS & OVERLAYS
    // ==========================================

    // Chi tiết món ăn & co giãn khẩu phần
    selectedDishForDetail?.let { meal ->
        DishDetailScalingDialog(
            meal = meal,
            onScaleIngredients = onScaleIngredients,
            onCopyMeal = onCopyMeal,
            onDeleteMeal = onDeleteMeal,
            onAskAi = onAskAi,
            onDismiss = { selectedDishForDetail = null }
        )
    }

    // Dialog thêm món ăn mới
    if (showAddMealDialog) {
        CreateMealDialog(
            initialDate = preselectedDateForAdd,
            onDismiss = { showAddMealDialog = false },
            onSaveMeal = { name, portion, category, ingredients, recipe, mealTime, date, cost ->
                onAddMeal(name, portion, category, ingredients, recipe, mealTime, date, cost)
                showAddMealDialog = false
            }
        )
    }

    // Dialog danh sách đi chợ từ thực đơn
    if (showShoppingListDialog) {
        ShoppingListDialog(
            shoppingItems = shoppingItems,
            onToggleBought = onToggleShoppingItem,
            onDeleteItem = onDeleteShoppingItem,
            onRegenerate = onGenerateShoppingList,
            onDismiss = { showShoppingListDialog = false }
        )
    }

    // Dialog AI gợi ý thực đơn
    if (showAiMenuDialog) {
        AiMenuSuggestionDialog(
            aiResponse = aiResponse,
            isLoading = isAiLoading,
            onAskAi = onAskAi,
            onApplyToMenu = { dishName, mealTime, ingredients, cost ->
                onAddMeal(
                    dishName,
                    "4 người",
                    "Món chính",
                    ingredients,
                    "Nấu theo gợi ý chuẩn của AI Bếp Nhà",
                    mealTime,
                    "13/10/2026",
                    cost
                )
            },
            onDismiss = { showAiMenuDialog = false }
        )
    }
}

/**
 * Thẻ thực đơn cho từng ngày trong tuần
 */
@Composable
private fun DailyMenuCard(
    dayTitle: String,
    dayDate: String,
    isToday: Boolean,
    meals: List<MealEntity>,
    onAddMealToDay: () -> Unit,
    onDishClick: (MealEntity) -> Unit,
    onToggleCooked: (Int, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isToday) 4.dp else 1.5.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isToday) Color(0xFF86EFAC) else BorderLight
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header ngày: Thứ & Ngày tháng + Nút Thêm món
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isToday) PrimaryGreen else Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dayTitle,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isToday) PrimaryGreenDark else TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• $dayDate",
                        fontSize = 11.5.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightMint,
                    modifier = Modifier.clickable { onAddMealToDay() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Thêm món",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Thêm món",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryGreen
                        )
                    }
                }
            }

            HorizontalDivider(
                color = if (isToday) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // Meals listing
            if (meals.isEmpty()) {
                // Empty state for this day
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .clickable { onAddMealToDay() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chưa có thực đơn • Bấm để thêm món",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                // Group meals by mealTime (Sáng, Trưa, Tối)
                val mealTimes = listOf("Trưa", "Tối", "Sáng")
                mealTimes.forEach { time ->
                    val mealsInTime = meals.filter { it.mealTime.equals(time, ignoreCase = true) }
                    if (mealsInTime.isNotEmpty()) {
                        MealTimeSection(
                            time = time,
                            meals = mealsInTime,
                            onDishClick = onDishClick,
                            onToggleCooked = onToggleCooked
                        )
                    }
                }
            }
        }
    }
}

/**
 * Bữa ăn con (Bữa Trưa, Bữa Tối) bên trong thẻ ngày
 */
@Composable
private fun MealTimeSection(
    time: String,
    meals: List<MealEntity>,
    onDishClick: (MealEntity) -> Unit,
    onToggleCooked: (Int, Boolean) -> Unit
) {
    val totalTimeCost = meals.sumOf { it.estimatedCost }
    val isAllCooked = meals.all { it.isCooked }

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        // MealTime Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (time == "Trưa") Color(0xFFFEF3C7) else Color(0xFFE0F2FE))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Bữa $time",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (time == "Trưa") Color(0xFFD97706) else Color(0xFF0284C7)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${meals.firstOrNull()?.portion ?: "4 người"} • %,d đ".format(totalTimeCost).replace(',', '.'),
                    fontSize = 11.5.sp,
                    color = TextSecondary
                )
            }

            // Status Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isAllCooked) LightMint else Color(0xFFF1F5F9))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isAllCooked) "✓ Đã nấu" else "Chờ nấu",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isAllCooked) PrimaryGreen else TextSecondary
                )
            }
        }

        // Dish Cards
        meals.forEach { meal ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .clickable { onDishClick(meal) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dish Thumbnail
                DishThumbnail(dishName = meal.name, size = 46.dp)

                Spacer(modifier = Modifier.width(10.dp))

                // Name & Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meal.name,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = if (meal.ingredients.isNotBlank()) meal.ingredients.split(",").firstOrNull()?.trim() ?: "" else meal.portion,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${meal.cookTimeMinutes}p",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Toggle Cooked Checkbox / Circle
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(if (meal.isCooked) LightMint else Color(0xFFE2E8F0))
                        .clickable { onToggleCooked(meal.id, meal.isCooked) },
                    contentAlignment = Alignment.Center
                ) {
                    if (meal.isCooked) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Đã nấu",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Hộp thoại tạo / thêm món ăn mới
 */
@Composable
private fun CreateMealDialog(
    initialDate: String,
    onDismiss: () -> Unit,
    onSaveMeal: (String, String, String, String, String, String, String, Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mealTime by remember { mutableStateOf("Trưa") }
    var date by remember { mutableStateOf(initialDate) }
    var portion by remember { mutableStateOf("4 người") }
    var category by remember { mutableStateOf("Món mặn") }
    var ingredients by remember { mutableStateOf("") }
    var recipe by remember { mutableStateOf("") }
    var estimatedCostStr by remember { mutableStateOf("85000") }

    // Popular dish presets
    val quickDishes = listOf(
        Pair("Cá kho gừng", "400g cá quả, 1 củ gừng, 3 thìa nước mắm"),
        Pair("Bắp cải luộc", "600g bắp cải, 1 củ gừng nhỏ"),
        Pair("Thịt kho trứng cút", "500g thịt ba chỉ, 10 quả trứng cút"),
        Pair("Canh chua cá lóc", "350g cá lóc, 2 quả cà chua, 50g dứa"),
        Pair("Sườn xào chua ngọt", "500g sườn thăn, 1 quả ớt chuông, 2 thìa giấm"),
        Pair("Gà rang gừng", "500g thịt gà ta, 1 củ gừng già, 2 thìa mắm"),
        Pair("Đậu sốt cà chua", "4 bìa đậu phụ, 3 quả cà chua, hành hoa"),
        Pair("Canh ngao nấu chua", "500g ngao, 2 quả cà chua, me chua, rau răm")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Thêm món vào thực đơn",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Lên lịch món ăn cho gia đình",
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderLight)

                // Quick Presets chips
                Text(
                    text = "Gợi ý món gia đình phổ biến:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                LazyRow(
                    modifier = Modifier.padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(quickDishes) { preset ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (name == preset.first) LightMint else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable {
                                name = preset.first
                                ingredients = preset.second
                            }
                        ) {
                            Text(
                                text = preset.first,
                                fontSize = 11.sp,
                                fontWeight = if (name == preset.first) FontWeight.Bold else FontWeight.Normal,
                                color = if (name == preset.first) PrimaryGreen else TextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Dish Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên món ăn (ví dụ: Sườn xào chua ngọt)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderLight
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // MealTime selector (Sáng | Trưa | Tối)
                Text(
                    text = "Bữa ăn trong ngày:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Sáng", "Trưa", "Tối").forEach { t ->
                        val isSel = mealTime == t
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) PrimaryGreen else Color(0xFFF1F5F9))
                                .clickable { mealTime = t }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Bữa $t",
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) SurfaceWhite else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Date selector
                Text(
                    text = "Ngày lên lịch:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                LazyRow(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val dates = listOf("12/10/2026", "13/10/2026", "14/10/2026", "15/10/2026", "16/10/2026", "17/10/2026", "18/10/2026")
                    items(dates) { d ->
                        val isSel = date == d
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) LightMint else Color(0xFFF1F5F9),
                            modifier = Modifier.clickable { date = d }
                        ) {
                            Text(
                                text = d,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) PrimaryGreen else TextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Portions & Cost Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = portion,
                        onValueChange = { portion = it },
                        label = { Text("Khẩu phần") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            unfocusedBorderColor = BorderLight
                        )
                    )

                    OutlinedTextField(
                        value = estimatedCostStr,
                        onValueChange = { estimatedCostStr = it },
                        label = { Text("Chi phí ước tính (đ)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            unfocusedBorderColor = BorderLight
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Ingredients Input
                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text("Nguyên liệu & định lượng (ngăn cách bằng dấu phẩy)") },
                    placeholder = { Text("Ví dụ: 400g cá, 1 củ gừng, 2 thìa mắm") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Save button
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val cost = estimatedCostStr.toLongOrNull() ?: 85000
                            onSaveMeal(name, portion, category, ingredients, recipe, mealTime, date, cost)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(text = "Lưu vào thực đơn", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Hộp thoại xem và quản lý Danh sách đi chợ (được tổng hợp từ thực đơn tuần)
 */
@Composable
private fun ShoppingListDialog(
    shoppingItems: List<ShoppingItemEntity>,
    onToggleBought: (Int, Boolean) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onRegenerate: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LightMint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Danh sách đi chợ tuần",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Đã cộng dồn nguyên liệu từ các món",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderLight)

                // Summary Row & Refresh button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val boughtCount = shoppingItems.count { it.isBought }
                    Text(
                        text = "Đã mua: $boughtCount / ${shoppingItems.size} mục",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen
                    )

                    TextButton(onClick = onRegenerate) {
                        Text("Tổng hợp lại", fontSize = 12.sp, color = SecondaryGreen)
                    }
                }

                // Shopping Items List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (shoppingItems.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Chưa có nguyên liệu nào trong danh sách.\nBấm 'Tổng hợp lại' để quét từ các món ăn tuần này.",
                                    fontSize = 12.5.sp,
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    } else {
                        items(shoppingItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (item.isBought) Color(0xFFF1F5F9) else LightMintSurface)
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = item.isBought,
                                        onCheckedChange = { onToggleBought(item.id, item.isBought) },
                                        colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                                    )
                                    Column {
                                        Text(
                                            text = item.name,
                                            fontSize = 13.5.sp,
                                            fontWeight = if (item.isBought) FontWeight.Normal else FontWeight.SemiBold,
                                            color = if (item.isBought) TextMuted else TextPrimary
                                        )
                                        Text(
                                            text = "Khối lượng: ${if (item.quantity % 1.0 == 0.0) item.quantity.toInt() else item.quantity} ${item.unit}",
                                            fontSize = 11.5.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { onDeleteItem(item.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xóa",
                                        tint = Color(0xFF94A3B8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderLight)

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đã xong", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Trợ lý Gemini AI gợi ý thực đơn
 */
@Composable
private fun AiMenuSuggestionDialog(
    aiResponse: String?,
    isLoading: Boolean,
    onAskAi: (String) -> Unit,
    onApplyToMenu: (String, String, String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var budget by remember { mutableStateOf("120000") }
    var peopleCount by remember { mutableStateOf("4") }
    var tastePreference by remember { mutableStateOf("Thanh đạm, chuẩn vị Bắc") }
    var fridgeIngredients by remember { mutableStateOf("Thịt băm, trứng gà, cà chua, hành hoa") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SurfaceWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AI Gợi ý thực đơn",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreenDark
                            )
                            Text(
                                text = "Gemini 3.5 Flash tối ưu chi phí",
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BorderLight)

                // Input fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = budget,
                        onValueChange = { budget = it },
                        label = { Text("Ngân sách (đ)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = peopleCount,
                        onValueChange = { peopleCount = it },
                        label = { Text("Số người ăn") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tastePreference,
                    onValueChange = { tastePreference = it },
                    label = { Text("Khẩu vị yêu thích") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fridgeIngredients,
                    onValueChange = { fridgeIngredients = it },
                    label = { Text("Nguyên liệu có sẵn trong tủ lạnh") },
                    placeholder = { Text("Ví dụ: Thịt băm, trứng, cà chua...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val prompt = "Hãy gợi ý cho tôi thực đơn mâm cơm gia đình cho $peopleCount người ăn, với ngân sách khoảng $budget đ. Khẩu vị: $tastePreference. Trong tủ lạnh đang có sẵn: $fridgeIngredients. Nêu rõ tên từng món, định lượng nguyên liệu cần mua thêm và chi phí ước tính."
                        onAskAi(prompt)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = SurfaceWhite, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI đang suy nghĩ thực đơn...", fontSize = 13.sp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("✨ AI Lên thực đơn ngay", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // AI Response Area
                aiResponse?.let { resp ->
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Thực đơn đề xuất từ AI Bếp Nhà:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreenDark
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LightMintSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = resp,
                                fontSize = 12.5.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    onApplyToMenu(
                                        "Mâm cơm gợi ý từ AI",
                                        "Tối",
                                        "Thịt nạc 300g, Cà chua 2 quả, Trứng gà 3 quả",
                                        budget.toLongOrNull() ?: 120000
                                    )
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Áp dụng món này vào thực đơn", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
