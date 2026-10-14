package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserSettings
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.ChefLogo
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

@Composable
fun SettingsScreen(
    userSettings: UserSettings,
    onSaveSettings: (UserSettings) -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var showGeminiDialog by remember { mutableStateOf(false) }
    var showDietDialog by remember { mutableStateOf(false) }
    var showFamilyDialog by remember { mutableStateOf(false) }

    var tempApiKey by remember(userSettings) { mutableStateOf(userSettings.geminiApiKey) }
    var tempFamilyMembers by remember(userSettings) { mutableStateOf(userSettings.familyMembers.toString()) }
    var tempDietNote by remember(userSettings) { mutableStateOf(userSettings.dietaryNote) }

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
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            // Top App Brand Header with Decorative Bowl Illustration
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChefLogo(size = 46.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI BẾP NHÀ",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Text(
                            text = "Nấu ngon • Sống khỏe • Cùng AI",
                            fontSize = 11.5.sp,
                            color = SecondaryGreen,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }
                }

                // Decorative Soup Bowl & Green Leaves
                DecorativeSoupBowl(size = 48.dp)
            }

            // Section Header: Gear icon + "Cài đặt"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cài đặt",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }
            Text(
                text = "Tùy chỉnh ứng dụng theo nhu cầu của bạn",
                fontSize = 12.5.sp,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 18.dp)
            )

            // GROUP 1: TÀI KHOẢN
            SettingsGroupCard(
                groupTitle = "Tài khoản",
                groupIcon = Icons.Default.Person
            ) {
                SettingsItemRow(
                    title = "Thông tin cá nhân",
                    subtitle = "Quản lý thông tin và hồ sơ của bạn",
                    icon = Icons.Default.Person,
                    onClick = { showFamilyDialog = true }
                )
                Divider(color = BorderLight, modifier = Modifier.padding(vertical = 4.dp))
                SettingsItemRow(
                    title = "Bảo mật tài khoản",
                    subtitle = "Đổi mật khẩu, xác thực 2 lớp",
                    icon = Icons.Default.Security,
                    onClick = { onNavigate(AppScreen.AUTH_RESET_PASSWORD) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // GROUP 2: TÙY CHỈNH THỰC ĐƠN
            SettingsGroupCard(
                groupTitle = "Tùy chỉnh thực đơn",
                groupIcon = Icons.Default.RestaurantMenu
            ) {
                SettingsItemRow(
                    title = "Số người ăn",
                    subtitle = "Cài đặt số thành viên trong gia đình: ${userSettings.familyMembers} người",
                    icon = Icons.Default.Group,
                    onClick = { showFamilyDialog = true }
                )
                Divider(color = BorderLight, modifier = Modifier.padding(vertical = 4.dp))
                SettingsItemRow(
                    title = "Ngân sách thực phẩm",
                    subtitle = "Quản lý chi tiêu và ngân sách hàng ngày",
                    icon = Icons.Default.Wallet,
                    onClick = { onNavigate(AppScreen.EXPENSE) }
                )
                Divider(color = BorderLight, modifier = Modifier.padding(vertical = 4.dp))
                SettingsItemRow(
                    title = "Khẩu vị và chế độ ăn",
                    subtitle = userSettings.dietaryNote,
                    icon = Icons.Default.RoomService,
                    onClick = { showDietDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // GROUP 3: TRỢ LÝ AI VÀ DỮ LIỆU
            SettingsGroupCard(
                groupTitle = "Trợ lý AI và dữ liệu",
                groupIcon = Icons.Default.SmartToy
            ) {
                SettingsItemRow(
                    title = "Cấu hình Gemini API",
                    subtitle = if (userSettings.geminiApiKey.isBlank()) "Sử dụng chế độ trợ lý AI tích hợp" else "Đã thiết lập API Key riêng",
                    icon = Icons.Default.AutoAwesome,
                    onClick = { showGeminiDialog = true }
                )
                Divider(color = BorderLight, modifier = Modifier.padding(vertical = 4.dp))
                SettingsItemRow(
                    title = "Thông báo",
                    subtitle = "Cài đặt thông báo ứng dụng",
                    icon = Icons.Default.Notifications,
                    onClick = { /* Notification setting */ }
                )
                Divider(color = BorderLight, modifier = Modifier.padding(vertical = 4.dp))
                SettingsItemRow(
                    title = "Sao lưu và khôi phục",
                    subtitle = "Lưu trữ và khôi phục dữ liệu của bạn",
                    icon = Icons.Default.CloudUpload,
                    onClick = { /* Backup */ }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // GROUP 4: GIAO DIỆN VÀ HỖ TRỢ
            SettingsGroupCard(
                groupTitle = "Giao diện và hỗ trợ",
                groupIcon = Icons.Default.Palette
            ) {
                SettingsItemRow(
                    title = "Giao diện",
                    subtitle = "Tùy chỉnh giao diện ứng dụng",
                    icon = Icons.Default.Palette,
                    onClick = { /* Theme */ }
                )
                Divider(color = BorderLight, modifier = Modifier.padding(vertical = 4.dp))
                SettingsItemRow(
                    title = "Thông tin ứng dụng",
                    subtitle = "Phiên bản 1.0, điều khoản và hỗ trợ",
                    icon = Icons.Default.Info,
                    onClick = { /* Info */ }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Navigation Bar (Screen 13 variant with green pill for Cài đặt)
        AppBottomNavBar(
            currentScreen = AppScreen.SETTINGS,
            onNavigate = onNavigate,
            onMicClick = { onNavigate(AppScreen.HOME) },
            isSettingsVariant = true
        )
    }

    // Gemini API Dialog
    if (showGeminiDialog) {
        AlertDialog(
            onDismissRequest = { showGeminiDialog = false },
            title = { Text("Cấu hình Gemini API Key", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Nhập API key từ Google AI Studio nếu bạn muốn sử dụng hạn ngạch cá nhân:",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    OutlinedTextField(
                        value = tempApiKey,
                        onValueChange = { tempApiKey = it },
                        placeholder = { Text("AIzaSy...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveSettings(userSettings.copy(geminiApiKey = tempApiKey))
                        showGeminiDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Lưu lại")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGeminiDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }

    // Family Members Dialog
    if (showFamilyDialog) {
        AlertDialog(
            onDismissRequest = { showFamilyDialog = false },
            title = { Text("Số người ăn trong gia đình", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = tempFamilyMembers,
                    onValueChange = { tempFamilyMembers = it },
                    label = { Text("Số thành viên") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val num = tempFamilyMembers.toIntOrNull() ?: 4
                        onSaveSettings(userSettings.copy(familyMembers = num))
                        showFamilyDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Lưu lại")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFamilyDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }

    // Dietary Preferences Dialog
    if (showDietDialog) {
        AlertDialog(
            onDismissRequest = { showDietDialog = false },
            title = { Text("Khẩu vị & Chế độ ăn", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = tempDietNote,
                    onValueChange = { tempDietNote = it },
                    label = { Text("Ghi chú khẩu vị") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveSettings(userSettings.copy(dietaryNote = tempDietNote))
                        showDietDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Lưu lại")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDietDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun SettingsGroupCard(
    groupTitle: String,
    groupIcon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Group Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Icon(
                    imageVector = groupIcon,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = groupTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }

            content()
        }
    }
}

@Composable
private fun SettingsItemRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(LightMint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.5.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp),
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Chi tiết",
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DecorativeSoupBowl(
    size: androidx.compose.ui.unit.Dp = 48.dp
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Green leaf in top background
            val leaf = Path().apply {
                moveTo(w * 0.15f, h * 0.35f)
                quadraticTo(w * 0.35f, h * 0.10f, w * 0.55f, h * 0.30f)
                quadraticTo(w * 0.35f, h * 0.45f, w * 0.15f, h * 0.35f)
                close()
            }
            drawPath(leaf, color = Color(0xFF86EFAC))

            // Ceramic Bowl (green rim)
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF047857))),
                topLeft = Offset(w * 0.28f, h * 0.36f),
                size = androidx.compose.ui.geometry.Size(w * 0.68f, h * 0.52f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
            )

            // Soup surface (creamy white broth with herbs & tofu)
            drawCircle(color = Color(0xFFFEF3C7), radius = w * 0.26f, center = Offset(w * 0.62f, h * 0.48f))
            drawCircle(color = Color.White, radius = 5f, center = Offset(w * 0.55f, h * 0.44f))
            drawCircle(color = Color(0xFFF97316), radius = 5f, center = Offset(w * 0.68f, h * 0.42f))
            drawCircle(color = Color(0xFF15803D), radius = 4f, center = Offset(w * 0.62f, h * 0.54f))
        }
    }
}
