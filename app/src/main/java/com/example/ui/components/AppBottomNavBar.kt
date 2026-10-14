package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LightMint
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.AppScreen

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val screen: AppScreen
)

@Composable
fun AppBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSettingsVariant: Boolean = false
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        color = SurfaceWhite,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(68.dp)
        ) {
            if (!isSettingsVariant) {
                // Standard 5-item bar with floating center Mic button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Trang chủ
                    NavButton(
                        label = "Trang chủ",
                        icon = Icons.Default.Home,
                        isSelected = currentScreen == AppScreen.HOME,
                        onClick = { onNavigate(AppScreen.HOME) }
                    )

                    // 2. Thực đơn
                    NavButton(
                        label = "Thực đơn",
                        icon = Icons.Default.RestaurantMenu,
                        isSelected = currentScreen == AppScreen.MENU,
                        onClick = { onNavigate(AppScreen.MENU) }
                    )

                    // Space for center Mic
                    Box(modifier = Modifier.size(52.dp))

                    // 4. Chi tiêu
                    NavButton(
                        label = "Chi tiêu",
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = currentScreen == AppScreen.EXPENSE,
                        onClick = { onNavigate(AppScreen.EXPENSE) }
                    )

                    // 5. Cài đặt
                    NavButton(
                        label = "Cài đặt",
                        icon = Icons.Default.Settings,
                        isSelected = currentScreen == AppScreen.SETTINGS,
                        onClick = { onNavigate(AppScreen.SETTINGS) }
                    )
                }

                // Center Mic FAB
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-10).dp)
                        .size(54.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                        .clickable { onMicClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Nhập nhanh",
                        tint = SurfaceWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                // Settings Variant (Screen 13): Trang chủ, Thực đơn, Mua sắm, Kế hoạch, Cài đặt (pill)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavButton(
                        label = "Trang chủ",
                        icon = Icons.Default.Home,
                        isSelected = false,
                        onClick = { onNavigate(AppScreen.HOME) }
                    )
                    NavButton(
                        label = "Thực đơn",
                        icon = Icons.Default.RestaurantMenu,
                        isSelected = false,
                        onClick = { onNavigate(AppScreen.MENU) }
                    )
                    NavButton(
                        label = "Mua sắm",
                        icon = Icons.Default.ShoppingCart,
                        isSelected = false,
                        onClick = { onNavigate(AppScreen.FEATURES_GRID) }
                    )
                    NavButton(
                        label = "Kế hoạch",
                        icon = Icons.Default.CalendarMonth,
                        isSelected = false,
                        onClick = { onNavigate(AppScreen.EXPENSE) }
                    )
                    // Active Pill for Cài đặt
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(LightMint)
                            .clickable { onNavigate(AppScreen.SETTINGS) }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Cài đặt",
                                tint = PrimaryGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Cài đặt",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) PrimaryGreen else TextMuted,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) PrimaryGreen else TextMuted,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
