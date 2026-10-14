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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBottomNavBar
import com.example.ui.theme.BackgroundApp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

data class GridFeatureItem(
    val number: Int,
    val title: String,
    val icon: ImageVector,
    val bgTint: Color,
    val iconTint: Color,
    val targetScreen: AppScreen
)

@Composable
fun FeaturesGridScreen(
    onNavigate: (AppScreen) -> Unit,
    onOpenQuickInput: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        GridFeatureItem(1, "Thực đơn", Icons.Default.RestaurantMenu, Color(0xFFDCFCE7), Color(0xFF16A34A), AppScreen.MENU),
        GridFeatureItem(2, "Đi chợ", Icons.Default.ShoppingCart, Color(0xFFF3E8FF), Color(0xFF9333EA), AppScreen.SHOPPING),
        GridFeatureItem(3, "Chi tiêu", Icons.Default.AccountBalanceWallet, Color(0xFFFFEDD5), Color(0xFFEA580C), AppScreen.EXPENSE),
        GridFeatureItem(4, "Thu nhập", Icons.Default.Paid, Color(0xFFCCFBF1), Color(0xFF0D9488), AppScreen.EXPENSE),
        GridFeatureItem(5, "Báo cáo", Icons.Default.Assessment, Color(0xFFEDE9FE), Color(0xFF7C3AED), AppScreen.EXPENSE),
        GridFeatureItem(6, "Nhập nhanh", Icons.Default.Mic, Color(0xFFE0F2FE), Color(0xFF0284C7), AppScreen.HOME),
        GridFeatureItem(7, "Lịch sử", Icons.Default.History, Color(0xFFE0E7FF), Color(0xFF4F46E5), AppScreen.EXPENSE),
        GridFeatureItem(8, "Cài đặt", Icons.Default.Settings, Color(0xFFF1F5F9), Color(0xFF475569), AppScreen.SETTINGS)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chức năng chính",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // 8 Grid Cards (2 columns, numbers 1 to 8)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .clickable {
                            if (item.number == 6) {
                                onOpenQuickInput()
                            } else {
                                onNavigate(item.targetScreen)
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = item.bgTint)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icon in center
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.iconTint,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        // Number & Title row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${item.number}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                            Text(
                                text = item.title,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Bottom Navigation Bar
        AppBottomNavBar(
            currentScreen = AppScreen.FEATURES_GRID,
            onNavigate = onNavigate,
            onMicClick = onOpenQuickInput
        )
    }
}
