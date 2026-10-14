package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.MealEntity
import com.example.ui.theme.BorderLight
import com.example.ui.theme.LightMint
import com.example.ui.theme.LightMintSurface
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DishDetailScalingDialog(
    meal: MealEntity,
    onScaleIngredients: (String, Int, Int) -> String,
    onCopyMeal: (MealEntity, String, String) -> Unit,
    onDeleteMeal: (MealEntity) -> Unit,
    onAskAi: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var currentPortion by remember { mutableIntStateOf(meal.portionBase) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showCopyDialog by remember { mutableStateOf(false) }

    val scaledIngredients = remember(currentPortion, meal.ingredients) {
        onScaleIngredients(meal.ingredients, meal.portionBase, currentPortion)
    }

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
                // Header: Thumbnail + Name + Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        DishThumbnail(dishName = meal.name, size = 52.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = meal.name,
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(LightMint)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = meal.category,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryGreen
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Bữa ${meal.mealTime} • ${meal.date}",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 12.dp))

                // Portions Scaling Control
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Khẩu phần ăn (Tự co giãn định lượng):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                        Text(
                            text = "$currentPortion người ăn",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (currentPortion > 1) LightMint else Color(0xFFE2E8F0))
                                .clickable(enabled = currentPortion > 1) {
                                    if (currentPortion > 1) currentPortion--
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Giảm",
                                tint = if (currentPortion > 1) PrimaryGreen else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(LightMint)
                                .clickable {
                                    currentPortion++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tăng",
                                tint = PrimaryGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Ingredients List
                Text(
                    text = "Nguyên liệu cần chuẩn bị:",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = LightMintSurface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        scaledIngredients.split(",").map { it.trim() }.forEach { ing ->
                            if (ing.isNotBlank()) {
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(SecondaryGreen)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ing,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Cooking Steps
                Text(
                    text = "Các bước chế biến:",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (meal.recipe.isNotBlank()) meal.recipe else "Sơ chế nguyên liệu tươi sạch, ướp gia vị đều tay và nấu lửa vừa theo thời gian hướng dẫn.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Time and Cost summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Nấu: ${meal.cookTimeMinutes} phút", fontSize = 12.sp, color = TextSecondary)
                    }
                    Text(
                        text = "Chi phí ước tính: %,d đ".format(meal.estimatedCost).replace(',', '.'),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen
                    )
                }

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Copy to another day
                    OutlinedButton(
                        onClick = { showCopyDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sao chép", fontSize = 12.sp)
                    }

                    // Delete meal
                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Xóa", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Ask AI Button
                Button(
                    onClick = {
                        onAskAi("Hãy cho tôi mẹo nấu món ${meal.name} cho $currentPortion người ăn ngon nhất và cách nêm chuẩn vị.")
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Hỏi AI mẹo nấu cho $currentPortion người", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa món ăn", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn xóa món '${meal.name}' khỏi thực đơn không?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteMeal(meal)
                        showDeleteConfirm = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Xóa món", color = SurfaceWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }

    // Copy to Date Dialog
    if (showCopyDialog) {
        var targetDate by remember { mutableStateOf("14/10/2026") }
        var targetTime by remember { mutableStateOf("Tối") }

        AlertDialog(
            onDismissRequest = { showCopyDialog = false },
            title = { Text("Sao chép sang ngày khác", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Chọn ngày và bữa ăn bạn muốn sao chép món '${meal.name}' sang:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("14/10/2026", "15/10/2026", "16/10/2026").forEach { d ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (targetDate == d) PrimaryGreen else Color(0xFFF1F5F9))
                                    .clickable { targetDate = d }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = d,
                                    fontSize = 11.5.sp,
                                    color = if (targetDate == d) SurfaceWhite else TextSecondary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Sáng", "Trưa", "Tối").forEach { t ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (targetTime == t) SecondaryGreen else Color(0xFFF1F5F9))
                                    .clickable { targetTime = t }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Bữa $t",
                                    fontSize = 11.5.sp,
                                    color = if (targetTime == t) SurfaceWhite else TextSecondary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCopyMeal(meal, targetDate, targetTime)
                        showCopyDialog = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Sao chép")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCopyDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }
}
