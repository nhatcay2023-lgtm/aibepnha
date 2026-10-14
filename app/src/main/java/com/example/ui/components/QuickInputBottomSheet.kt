package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuickInputBottomSheet(
    onDismiss: () -> Unit,
    onVoiceSelect: () -> Unit,
    onTextQuickAdd: (String) -> Unit,
    onChatAiSelect: () -> Unit,
    onManualAddSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quickTextInput by remember { mutableStateOf("") }
    var isTextInputMode by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main Modal Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                color = SurfaceWhite,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nhập nhanh",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    if (!isTextInputMode) {
                        // Option 1: Giọng nói (Purple)
                        QuickInputOptionRow(
                            icon = Icons.Default.Mic,
                            iconBg = Color(0xFFF3E8FF),
                            iconTint = Color(0xFF7C3AED),
                            title = "Nhập nhanh (Giọng nói)",
                            subtitle = "Nói để ghi nhận thực đơn, chi tiêu, đi chợ...",
                            onClick = onVoiceSelect
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Option 2: Văn bản (Orange)
                        QuickInputOptionRow(
                            icon = Icons.Default.Keyboard,
                            iconBg = Color(0xFFFFEDD5),
                            iconTint = Color(0xFFEA580C),
                            title = "Nhập nhanh (Văn bản)",
                            subtitle = "Gõ nhanh để thêm dữ liệu",
                            onClick = { isTextInputMode = true }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Option 3: Chat AI (Blue)
                        QuickInputOptionRow(
                            icon = Icons.Default.SmartToy,
                            iconBg = Color(0xFFDBEAFE),
                            iconTint = Color(0xFF2563EB),
                            title = "Chat AI",
                            subtitle = "Trò chuyện với AI trợ lý",
                            onClick = onChatAiSelect
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Option 4: Nhập thủ công (Green)
                        QuickInputOptionRow(
                            icon = Icons.Default.Edit,
                            iconBg = Color(0xFFDCFCE7),
                            iconTint = Color(0xFF16A34A),
                            title = "Nhập thủ công",
                            subtitle = "Nhập dữ liệu thủ công",
                            onClick = onManualAddSelect
                        )
                    } else {
                        // Quick Text Entry Box
                        OutlinedTextField(
                            value = quickTextInput,
                            onValueChange = { quickTextInput = it },
                            placeholder = { Text("Ví dụ: Bữa trưa ăn bún chả 50.000 đ...", fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { isTextInputMode = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Quay lại", color = TextSecondary)
                            }
                            Button(
                                onClick = {
                                    if (quickTextInput.isNotBlank()) {
                                        onTextQuickAdd(quickTextInput)
                                        quickTextInput = ""
                                        isTextInputMode = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Thêm ngay", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Circular Close Button (X) matching Screen 11
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF374151))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Đóng",
                    tint = SurfaceWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickInputOptionRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF9FAFB),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
