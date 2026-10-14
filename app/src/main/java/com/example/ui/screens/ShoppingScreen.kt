package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MealEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.ShoppingListEntity
import com.example.ui.components.AppBottomNavBar
import com.example.ui.theme.BackgroundApp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.LightMint
import com.example.ui.theme.LightMintSurface
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

private val CardBorder = BorderStroke(1.dp, BorderLight)

@Composable
fun ShoppingScreen(
    currentList: ShoppingListEntity?,
    allLists: List<ShoppingListEntity>,
    items: List<ShoppingItemEntity>,
    meals: List<MealEntity>,
    pantryItems: List<PantryItemEntity>,
    onNavigate: (AppScreen) -> Unit,
    onOpenQuickInput: () -> Unit,
    onSelectList: (Int) -> Unit,
    onCreateFromMenu: (meals: List<MealEntity>, title: String, periodLabel: String, dateRange: String, subtractPantry: Boolean, budget: Long) -> Unit,
    onCreateManualList: (title: String, budget: Long, periodLabel: String, dateRange: String) -> Unit,
    onAddItem: (listId: Int, name: String, qty: Double, unit: String, category: String, estPrice: Long, note: String) -> Unit,
    onUpdateItem: (ShoppingItemEntity) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onToggleBought: (id: Int, isBought: Boolean, actualPrice: Long) -> Unit,
    onCompleteTrip: (listId: Int, addToPantry: Boolean, recordExpense: Boolean) -> Unit,
    onReuseList: (Int) -> Unit,
    onDeleteList: (ShoppingListEntity) -> Unit,
    onAddPantryItem: (name: String, qty: Double, unit: String, category: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Dialog states
    var showCreateFromMenuDialog by remember { mutableStateOf(false) }
    var showCreateManualDialog by remember { mutableStateOf(false) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ShoppingItemEntity?>(null) }
    var itemForActualPriceInput by remember { mutableStateOf<ShoppingItemEntity?>(null) }
    var showCompleteTripDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showPantryDialog by remember { mutableStateOf(false) }

    // Filter chip state: "Tất cả", "Thịt/Cá", "Rau củ", "Gia vị", "Khác", "Chưa mua", "Đã mua"
    var selectedCategoryFilter by remember { mutableStateOf("Tất cả") }

    val activeListId = currentList?.id ?: (allLists.firstOrNull { !it.isCompleted }?.id ?: 1)
    val displayedList = currentList ?: allLists.firstOrNull { it.id == activeListId } ?: allLists.firstOrNull()

    // Filter items based on active list
    val listItems = if (displayedList != null) {
        items.filter { it.listId == displayedList.id }
    } else {
        items
    }

    // Calculations
    val totalEstimated = listItems.sumOf { (it.quantity.coerceAtLeast(1.0) * it.estimatedPrice).toLong().coerceAtLeast(it.estimatedPrice) }
    val totalActual = listItems.filter { it.isBought }.sumOf { if (it.actualPrice > 0) it.actualPrice else it.estimatedPrice }
    val budget = displayedList?.budget ?: 800000L
    val remainingBudget = budget - totalActual
    val budgetProgress = if (budget > 0) (totalActual.toFloat() / budget.toFloat()).coerceIn(0f, 1f) else 0f
    val isOverBudget = totalActual > budget

    // Filtered items
    val filteredItems = when (selectedCategoryFilter) {
        "Thịt/Cá" -> listItems.filter { it.category == "Thịt, cá, hải sản" }
        "Rau củ" -> listItems.filter { it.category == "Rau củ, trái cây" }
        "Gia vị" -> listItems.filter { it.category == "Gia vị, đồ khô" }
        "Khác" -> listItems.filter { it.category == "Khác" }
        "Chưa mua" -> listItems.filter { !it.isBought }
        "Đã mua" -> listItems.filter { it.isBought }
        else -> listItems
    }

    val categories = listOf("Thịt, cá, hải sản", "Rau củ, trái cây", "Gia vị, đồ khô", "Khác")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
    ) {
        // ==========================================
        // 1. THANH TIÊU ĐỀ VÀ THAO TÁC (HEADER)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onNavigate(AppScreen.HOME) },
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("btn_back_home")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại Trang chủ",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Đi chợ",
                            fontSize = 20.sp,
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
                                text = displayedList?.periodLabel ?: "Tuần này",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        }
                    }
                    Text(
                        text = "Lên danh sách mua sắm, kiểm soát ngân sách",
                        fontSize = 11.5.sp,
                        color = TextSecondary
                    )
                }
            }

            // Action icons: History and Pantry
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showPantryDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE9FE))
                        .testTag("btn_open_pantry")
                ) {
                    Icon(
                        imageVector = Icons.Default.Kitchen,
                        contentDescription = "Kho nguyên liệu",
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = { showHistoryDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2FE))
                        .testTag("btn_open_history")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Lịch sử đi chợ",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ==========================================
        // 2. CHỌN DANH SÁCH & NÚT TẠO MỚI
        // ==========================================
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            color = SurfaceWhite,
            border = CardBorder
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = displayedList?.title ?: "Danh sách mua sắm",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Ngày: ${displayedList?.dateRange ?: "13/10/2026"} • ${listItems.size} món",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row {
                    Button(
                        onClick = { showCreateFromMenuDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("btn_create_from_menu")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tạo từ thực đơn", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ==========================================
        // MAIN SCROLLABLE BODY
        // ==========================================
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ==========================================
            // 3. TỔNG QUAN CHI PHÍ (3 THẺ THÔNG TIN)
            // ==========================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Thẻ 1: Tổng tiền dự kiến
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = CardBorder
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Dự kiến", fontSize = 11.sp, color = TextSecondary)
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "%,d đ".format(totalEstimated).replace(',', '.'),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${listItems.size} món đồ",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    // Thẻ 2: Thực tế đã mua
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = CardBorder
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Đã mua", fontSize = 11.sp, color = TextSecondary)
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "%,d đ".format(totalActual).replace(',', '.'),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${listItems.count { it.isBought }}/${listItems.size} món",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    // Thẻ 3: Số tiền còn lại trong ngân sách
                    val remainingCardBg = if (isOverBudget) Color(0xFFFEF2F2) else Color(0xFFFFFBEB)
                    val remainingTextColor = if (isOverBudget) Color(0xFFDC2626) else Color(0xFFD97706)

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = remainingCardBg),
                        border = CardBorder
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Còn lại", fontSize = 11.sp, color = TextSecondary)
                                Icon(
                                    imageVector = if (isOverBudget) Icons.Default.WarningAmber else Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = remainingTextColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "%,d đ".format(remainingBudget).replace(',', '.'),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = remainingTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (isOverBudget) "Vượt ngân sách!" else "Ngân sách: %,d đ".format(budget).replace(',', '.'),
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Thanh tiến độ ngân sách
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceWhite,
                    border = CardBorder
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tiến độ chi tiêu đợt đi chợ",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Text(
                                text = "%.0f%%".format(budgetProgress * 100),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isOverBudget) Color(0xFFDC2626) else PrimaryGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { budgetProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isOverBudget) Color(0xFFDC2626) else PrimaryGreen,
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                }
            }

            // ==========================================
            // 4. BỘ LỌC NGÀNH HÀNG / TRẠNG THÁI (CHIPS)
            // ==========================================
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val filters = listOf("Tất cả", "Thịt/Cá", "Rau củ", "Gia vị", "Khác", "Chưa mua", "Đã mua")
                    items(filters) { filter ->
                        val isSelected = selectedCategoryFilter == filter
                        val bg = if (isSelected) PrimaryGreen else SurfaceWhite
                        val textColor = if (isSelected) Color.White else TextSecondary
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(bg)
                                .border(1.dp, if (isSelected) PrimaryGreen else Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                                .clickable { selectedCategoryFilter = filter }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("filter_$filter")
                        ) {
                            Text(
                                text = filter,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = textColor
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 5. DANH SÁCH MÓN HÀNG THEO NHÓM
            // ==========================================
            if (filteredItems.isEmpty()) {
                item {
                    EmptyShoppingState(
                        onOpenCreateFromMenu = { showCreateFromMenuDialog = true },
                        onOpenAddItem = { showAddItemDialog = true }
                    )
                }
            } else if (selectedCategoryFilter != "Tất cả") {
                // Hiển thị phẳng danh sách lọc
                items(filteredItems, key = { it.id }) { item ->
                    ShoppingItemRow(
                        item = item,
                        onToggle = { isBought ->
                            if (isBought && item.actualPrice == 0L) {
                                itemForActualPriceInput = item
                            } else {
                                onToggleBought(item.id, isBought, item.actualPrice)
                            }
                        },
                        onEdit = { editingItem = item },
                        onDelete = { onDeleteItem(item.id) },
                        onPriceClick = { itemForActualPriceInput = item }
                    )
                }
            } else {
                // Hiển thị gom nhóm theo 4 ngành hàng
                categories.forEach { cat ->
                    val groupItems = listItems.filter { it.category == cat }
                    if (groupItems.isNotEmpty()) {
                        item {
                            CategoryHeader(
                                category = cat,
                                count = groupItems.size,
                                boughtCount = groupItems.count { it.isBought }
                            )
                        }
                        items(groupItems, key = { it.id }) { item ->
                            ShoppingItemRow(
                                item = item,
                                onToggle = { isBought ->
                                    if (isBought && item.actualPrice == 0L) {
                                        itemForActualPriceInput = item
                                    } else {
                                        onToggleBought(item.id, isBought, item.actualPrice)
                                    }
                                },
                                onEdit = { editingItem = item },
                                onDelete = { onDeleteItem(item.id) },
                                onPriceClick = { itemForActualPriceInput = item }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        // ==========================================
        // 6. THANH HÀNH ĐỘNG DƯỚI CÙNG (BOTTOM ACTION BAR)
        // ==========================================
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SurfaceWhite,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { showAddItemDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("btn_add_item_manual"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Thêm món", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { showCompleteTripDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier
                        .weight(1.4f)
                        .height(42.dp)
                        .testTag("btn_complete_shopping"),
                    shape = RoundedCornerShape(10.dp),
                    enabled = listItems.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hoàn thành đi chợ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==========================================
        // 7. THANH ĐIỀU HƯỚNG DƯỚI CÙNG (5 TABS CHUẨN)
        // ==========================================
        AppBottomNavBar(
            currentScreen = AppScreen.SHOPPING,
            onNavigate = onNavigate,
            onMicClick = onOpenQuickInput
        )
    }

    // ==========================================
    // 8. CÁC HỘP THOẠI & MODAL
    // ==========================================

    // A. Dialog: Tạo danh sách từ thực đơn
    if (showCreateFromMenuDialog) {
        CreateFromMenuModal(
            meals = meals,
            pantryItems = pantryItems,
            onDismiss = { showCreateFromMenuDialog = false },
            onConfirm = { selectedMealList, title, period, dateRange, subtractPantry, budgetVal ->
                onCreateFromMenu(selectedMealList, title, period, dateRange, subtractPantry, budgetVal)
                showCreateFromMenuDialog = false
            }
        )
    }

    // B. Dialog: Thêm món mới
    if (showAddItemDialog) {
        AddOrEditItemModal(
            item = null,
            onDismiss = { showAddItemDialog = false },
            onSave = { name, qty, unit, cat, estPrice, note ->
                onAddItem(activeListId, name, qty, unit, cat, estPrice, note)
                showAddItemDialog = false
            }
        )
    }

    // C. Dialog: Sửa món hiện có
    editingItem?.let { item ->
        AddOrEditItemModal(
            item = item,
            onDismiss = { editingItem = null },
            onSave = { name, qty, unit, cat, estPrice, note ->
                onUpdateItem(
                    item.copy(
                        name = name,
                        quantity = qty,
                        unit = unit,
                        category = cat,
                        estimatedPrice = estPrice,
                        note = note
                    )
                )
                editingItem = null
            }
        )
    }

    // D. Dialog: Nhập giá thực tế khi vừa mua
    itemForActualPriceInput?.let { item ->
        ActualPriceModal(
            item = item,
            onDismiss = { itemForActualPriceInput = null },
            onConfirm = { price ->
                onToggleBought(item.id, true, price)
                itemForActualPriceInput = null
            }
        )
    }

    // E. Dialog: Xác nhận hoàn thành đi chợ (Tích hợp kho & chi tiêu)
    if (showCompleteTripDialog && displayedList != null) {
        CompleteTripConfirmationModal(
            list = displayedList,
            items = listItems,
            onDismiss = { showCompleteTripDialog = false },
            onConfirm = { addToPantry, recordExpense ->
                onCompleteTrip(displayedList.id, addToPantry, recordExpense)
                showCompleteTripDialog = false
            }
        )
    }

    // F. Modal: Xem lịch sử các chuyến đi chợ
    if (showHistoryDialog) {
        ShoppingHistoryModal(
            lists = allLists,
            onDismiss = { showHistoryDialog = false },
            onSelectList = { listId ->
                onSelectList(listId)
                showHistoryDialog = false
            },
            onReuseList = { oldId ->
                onReuseList(oldId)
                showHistoryDialog = false
            },
            onDeleteList = { listToDelete ->
                onDeleteList(listToDelete)
            }
        )
    }

    // G. Modal: Kho nguyên liệu (Pantry)
    if (showPantryDialog) {
        PantryViewerModal(
            pantryItems = pantryItems,
            onDismiss = { showPantryDialog = false },
            onAddPantry = onAddPantryItem
        )
    }
}

// ==========================================
// THÀNH PHẦN HÀNG HÓA (SHOPPING ITEM ROW)
// ==========================================
@Composable
private fun ShoppingItemRow(
    item: ShoppingItemEntity,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPriceClick: () -> Unit
) {
    val cardBg by animateColorAsState(
        targetValue = if (item.isBought) Color(0xFFF8FAFC) else SurfaceWhite,
        label = "item_bg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("shopping_item_${item.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = item.isBought,
                onCheckedChange = { onToggle(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryGreen,
                    uncheckedColor = Color(0xFF94A3B8)
                ),
                modifier = Modifier
                    .size(24.dp)
                    .testTag("chk_item_${item.id}")
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Main Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (item.isBought) TextMuted else TextPrimary,
                        textDecoration = if (item.isBought) TextDecoration.LineThrough else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${if (item.quantity % 1.0 == 0.0) item.quantity.toInt() else item.quantity} ${item.unit}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }

                // Ghi chú hoặc thông tin món / kho
                if (item.note.isNotBlank() || item.sourceMealName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (item.note.isNotBlank()) {
                            Text(
                                text = item.note,
                                fontSize = 10.5.sp,
                                color = if (item.note.contains("Đã trừ") || item.note.contains("đủ")) SecondaryGreen else Color(0xFF64748B),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else if (item.sourceMealName.isNotBlank()) {
                            Text(
                                text = "Món: ${item.sourceMealName}",
                                fontSize = 10.5.sp,
                                color = TextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Giá dự kiến & Giá thực tế
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onPriceClick() }
                ) {
                    val actualText = if (item.actualPrice > 0) "Thực tế: %,d đ".format(item.actualPrice).replace(',', '.') else "Nhập giá thực tế"
                    Text(
                        text = "Dự kiến: %,d đ".format(item.estimatedPrice).replace(',', '.'),
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(text = " • ", fontSize = 11.sp, color = TextMuted)
                    Text(
                        text = actualText,
                        fontSize = 11.sp,
                        fontWeight = if (item.actualPrice > 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (item.actualPrice > 0) PrimaryGreen else Color(0xFF2563EB)
                    )
                }
            }

            // Quick actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Sửa",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xóa",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// TIÊU ĐỀ PHÂN NHÓM (CATEGORY HEADER)
// ==========================================
@Composable
private fun CategoryHeader(
    category: String,
    count: Int,
    boughtCount: Int
) {
    val icon = when (category) {
        "Thịt, cá, hải sản" -> "🥩"
        "Rau củ, trái cây" -> "🥬"
        "Gia vị, đồ khô" -> "🧂"
        else -> "📦"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = category,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Text(
            text = "$boughtCount/$count đã mua",
            fontSize = 11.sp,
            color = if (boughtCount == count) SecondaryGreen else TextMuted,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==========================================
// TRẠNG THÁI TRỐNG (EMPTY STATE)
// ==========================================
@Composable
private fun EmptyShoppingState(
    onOpenCreateFromMenu: () -> Unit,
    onOpenAddItem: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = CardBorder
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(LightMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Chưa có món hàng nào",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tạo danh sách từ Thực đơn đã lên lịch hoặc thêm từng món cần mua.",
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onOpenCreateFromMenu,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tạo từ thực đơn", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = onOpenAddItem,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Thêm thủ công", fontSize = 12.sp)
                }
            }
        }
    }
}

// ==========================================
// MODAL 1: TẠO DANH SÁCH TỪ THỰC ĐƠN
// ==========================================
@Composable
private fun CreateFromMenuModal(
    meals: List<MealEntity>,
    pantryItems: List<PantryItemEntity>,
    onDismiss: () -> Unit,
    onConfirm: (List<MealEntity>, String, String, String, Boolean, Long) -> Unit
) {
    var selectedPreset by remember { mutableStateOf("Tuần này") }
    var subtractPantry by remember { mutableStateOf(true) }
    var budgetInput by remember { mutableStateOf("800000") }

    val filteredMeals = when (selectedPreset) {
        "Hôm nay" -> meals.filter { it.date == "13/10/2026" }
        "3 ngày tới" -> meals.take(4)
        else -> meals // Tuần này
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tạo danh sách từ Thực đơn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Chọn khoảng thời gian:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Hôm nay", "Tuần này", "3 ngày tới").forEach { preset ->
                        val isSel = selectedPreset == preset
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) LightMint else Color(0xFFF1F5F9))
                                .border(1.dp, if (isSel) PrimaryGreen else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { selectedPreset = preset }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = preset,
                                fontSize = 11.5.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) PrimaryGreen else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = CardBorder
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Đã tìm thấy ${filteredMeals.size} món ăn đã lên lịch:",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = filteredMeals.joinToString(", ") { it.name }.ifBlank { "Chưa có món nào" },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Checkbox: Trừ kho nguyên liệu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { subtractPantry = !subtractPantry },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = subtractPantry,
                        onCheckedChange = { subtractPantry = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Trừ nguyên liệu đã có trong tủ lạnh",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tủ lạnh hiện có ${pantryItems.size} nguyên liệu sẵn",
                            fontSize = 10.5.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Ngân sách dự kiến
                OutlinedTextField(
                    value = budgetInput,
                    onValueChange = { budgetInput = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Ngân sách dự kiến (đ)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val budgetVal = budgetInput.toLongOrNull() ?: 800000L
                        val title = "Đi chợ $selectedPreset (${if (selectedPreset == "Hôm nay") "13/10/2026" else "12/10 - 18/10/2026"})"
                        val dateRange = if (selectedPreset == "Hôm nay") "13/10/2026" else "12/10 - 18/10/2026"
                        onConfirm(filteredMeals, title, selectedPreset, dateRange, subtractPantry, budgetVal)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text("Tạo danh sách & Tự động bóc tách", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================
// MODAL 2: THÊM / SỬA MÓN ĐỒ
// ==========================================
@Composable
private fun AddOrEditItemModal(
    item: ShoppingItemEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, qty: Double, unit: String, category: String, estPrice: Long, note: String) -> Unit
) {
    var name by remember { mutableStateOf(item?.name ?: "") }
    var qtyStr by remember { mutableStateOf(if (item != null) (if (item.quantity % 1.0 == 0.0) item.quantity.toInt().toString() else item.quantity.toString()) else "1") }
    var unit by remember { mutableStateOf(item?.unit ?: "g") }
    var category by remember { mutableStateOf(item?.category ?: "Rau củ, trái cây") }
    var estPriceStr by remember { mutableStateOf(item?.estimatedPrice?.toString() ?: "20000") }
    var note by remember { mutableStateOf(item?.note ?: "") }

    val categories = listOf("Thịt, cá, hải sản", "Rau củ, trái cây", "Gia vị, đồ khô", "Khác")
    val units = listOf("g", "kg", "quả", "củ", "mớ", "gói", "hộp", "chai", "thìa")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = if (item != null) "Sửa món hàng" else "Thêm món hàng mới",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên nguyên liệu / đồ cần mua") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = qtyStr,
                        onValueChange = { qtyStr = it },
                        label = { Text("Số lượng") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Đơn vị") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category chips
                Text("Ngành hàng:", fontSize = 11.5.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSel = category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSel) LightMint else Color(0xFFF1F5F9))
                                .border(1.dp, if (isSel) PrimaryGreen else Color.Transparent, RoundedCornerShape(14.dp))
                                .clickable { category = cat }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) PrimaryGreen else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = estPriceStr,
                    onValueChange = { estPriceStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Giá dự kiến (đ)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú (tùy chọn)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Hủy")
                    }
                    Button(
                        onClick = {
                            val qty = qtyStr.toDoubleOrNull() ?: 1.0
                            val est = estPriceStr.toLongOrNull() ?: 0L
                            if (name.isNotBlank()) {
                                onSave(name.trim(), qty, unit.trim(), category, est, note.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank()
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

// ==========================================
// MODAL 3: NHẬP GIÁ THỰC TẾ
// ==========================================
@Composable
private fun ActualPriceModal(
    item: ShoppingItemEntity,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var priceStr by remember {
        mutableStateOf(if (item.actualPrice > 0) item.actualPrice.toString() else item.estimatedPrice.toString())
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Xác nhận giá thực tế",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Món: ${item.name} (${if (item.quantity % 1.0 == 0.0) item.quantity.toInt() else item.quantity} ${item.unit})",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Số tiền thực tế đã trả (đ)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Bỏ qua")
                    }
                    Button(
                        onClick = {
                            val price = priceStr.toLongOrNull() ?: item.estimatedPrice
                            onConfirm(price)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Xác nhận")
                    }
                }
            }
        }
    }
}

// ==========================================
// MODAL 4: XÁC NHẬN HOÀN THÀNH ĐI CHỢ
// ==========================================
@Composable
private fun CompleteTripConfirmationModal(
    list: ShoppingListEntity,
    items: List<ShoppingItemEntity>,
    onDismiss: () -> Unit,
    onConfirm: (addToPantry: Boolean, recordExpense: Boolean) -> Unit
) {
    var addToPantry by remember { mutableStateOf(true) }
    var recordExpense by remember { mutableStateOf(true) }

    val boughtItems = items.filter { it.isBought }
    val totalSpent = boughtItems.sumOf { if (it.actualPrice > 0) it.actualPrice else it.estimatedPrice }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LightMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Hoàn thành chuyến đi chợ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = list.title,
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = CardBorder
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Số món đã mua:", fontSize = 12.sp, color = TextSecondary)
                            Text("${boughtItems.size}/${items.size} món", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng tiền thực tế:", fontSize = 12.sp, color = TextSecondary)
                            Text(
                                "%,d đ".format(totalSpent).replace(',', '.'),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tùy chọn 1: Thêm vào kho
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { addToPantry = !addToPantry },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = addToPantry,
                        onCheckedChange = { addToPantry = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Cập nhật các món đã mua vào Kho nguyên liệu", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("Tự động cộng dồn số lượng vào tủ lạnh", fontSize = 10.5.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Tùy chọn 2: Ghi nhận chi tiêu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { recordExpense = !recordExpense },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = recordExpense,
                        onCheckedChange = { recordExpense = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Ghi nhận vào sổ Chi tiêu (Khoản 'Đi chợ')", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("Tự động lưu khoản chi %,d đ vào mục Chi tiêu".format(totalSpent).replace(',', '.'), fontSize = 10.5.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Đóng")
                    }
                    Button(
                        onClick = { onConfirm(addToPantry, recordExpense) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Text("Lưu & Hoàn thành", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// MODAL 5: LỊCH SỬ CÁC CHUYẾN ĐI CHỢ
// ==========================================
@Composable
private fun ShoppingHistoryModal(
    lists: List<ShoppingListEntity>,
    onDismiss: () -> Unit,
    onSelectList: (Int) -> Unit,
    onReuseList: (Int) -> Unit,
    onDeleteList: (ShoppingListEntity) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lịch sử các đợt đi chợ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (lists.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Chưa có lịch sử đi chợ nào", color = TextSecondary, fontSize = 13.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lists) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectList(item.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (item.isCompleted) Color(0xFFF8FAFC) else Color(0xFFF0FDF4)
                                ),
                                border = CardBorder
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = item.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(if (item.isCompleted) Color(0xFFE2E8F0) else LightMint)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (item.isCompleted) "Đã hoàn thành" else "Đang mở",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (item.isCompleted) Color(0xFF64748B) else PrimaryGreen
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Khoảng ngày: ${item.dateRange} • Ngân sách: %,d đ".format(item.budget).replace(',', '.'),
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )

                                    if (item.note.isNotBlank()) {
                                        Text(text = item.note, fontSize = 10.5.sp, color = TextMuted)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        OutlinedButton(
                                            onClick = { onReuseList(item.id) },
                                            modifier = Modifier.height(30.dp),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text("Dùng lại danh sách", fontSize = 11.sp)
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        IconButton(
                                            onClick = { onDeleteList(item) },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Xóa",
                                                tint = Color(0xFFEF4444),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// MODAL 6: XEM VÀ QUẢN LÝ KHO NGUYÊN LIỆU (PANTRY)
// ==========================================
@Composable
private fun PantryViewerModal(
    pantryItems: List<PantryItemEntity>,
    onDismiss: () -> Unit,
    onAddPantry: (name: String, qty: Double, unit: String, category: String) -> Unit
) {
    var showAddForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var qtyStr by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("củ") }
    var cat by remember { mutableStateOf("Rau củ, trái cây") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Kitchen, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kho nguyên liệu (${pantryItems.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Nguyên liệu sẵn có trong tủ lạnh để AI tự động trừ khi tạo danh sách đi chợ.",
                    fontSize = 11.5.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (showAddForm) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = CardBorder
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Tên nguyên liệu") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedTextField(
                                    value = qtyStr,
                                    onValueChange = { qtyStr = it },
                                    label = { Text("Số lượng") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = unit,
                                    onValueChange = { unit = it },
                                    label = { Text("Đơn vị") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(onClick = { showAddForm = false }, modifier = Modifier.height(32.dp)) {
                                    Text("Hủy", fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = {
                                        val q = qtyStr.toDoubleOrNull() ?: 1.0
                                        if (name.isNotBlank()) {
                                            onAddPantry(name.trim(), q, unit.trim(), cat)
                                            name = ""
                                            showAddForm = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Thêm vào kho", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    OutlinedButton(
                        onClick = { showAddForm = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Thêm nguyên liệu vào tủ lạnh", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(pantryItems) { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8FAFC),
                            border = CardBorder
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = item.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                    Text(text = item.note.ifBlank { "Tủ bếp / Tủ lạnh" }, fontSize = 10.5.sp, color = TextMuted)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFEDE9FE))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "${if (item.quantity % 1.0 == 0.0) item.quantity.toInt() else item.quantity} ${item.unit}",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF7C3AED)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
