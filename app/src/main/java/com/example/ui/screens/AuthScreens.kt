package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChefLogo
import com.example.ui.components.ForgotPasswordIllustration
import com.example.ui.components.SuccessCircleIllustration
import com.example.ui.theme.BackgroundApp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

// SCREEN 4: ĐĂNG NHẬP
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailOrPhone by remember { mutableStateOf("anh.nguyen@email.com") }
    var password by remember { mutableStateOf("••••••••") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Chef Hat Logo
        ChefLogo(size = 72.dp)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Chào mừng bạn đến với",
            fontSize = 15.sp,
            color = TextSecondary
        )
        Text(
            text = "AI BẾP NHÀ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tabs: Đăng nhập (Active) / Đăng ký
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* Already on login */ }
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Đăng nhập",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(2.5.dp)
                        .width(70.dp)
                        .background(PrimaryGreen)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToRegister() }
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Đăng ký",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted
                )
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(2.5.dp)
                        .width(70.dp)
                        .background(Color.Transparent)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input: Email hoặc SĐT
        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            placeholder = { Text("Email hoặc số điện thoại", fontSize = 14.sp, color = TextMuted) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = BorderLight,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Input: Mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Mật khẩu", fontSize = 14.sp, color = TextMuted) },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Ẩn hiện mật khẩu",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = BorderLight,
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Row: Ghi nhớ đăng nhập + Quên mật khẩu?
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
                )
                Text(text = "Ghi nhớ đăng nhập", fontSize = 13.sp, color = TextPrimary)
            }

            Text(
                text = "Quên mật khẩu?",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen,
                modifier = Modifier.clickable { onForgotPassword() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text(text = "Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurfaceWhite)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divider: Hoặc đăng nhập với
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = BorderLight)
            Text(
                text = "  Hoặc đăng nhập với  ",
                fontSize = 12.sp,
                color = TextMuted
            )
            Divider(modifier = Modifier.weight(1f), color = BorderLight)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Social Buttons: Google & Facebook
        SocialButtonRow(onGoogleClick = onLoginSuccess, onFacebookClick = onLoginSuccess)

        Spacer(modifier = Modifier.height(24.dp))

        // Footer: Chưa có tài khoản? Đăng ký ngay
        Row(
            modifier = Modifier.clickable { onNavigateToRegister() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Chưa có tài khoản? ", fontSize = 13.sp, color = TextSecondary)
            Text(text = "Đăng ký ngay", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        }
    }
}

// SCREEN 5: TẠO TÀI KHOẢN MỚI
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Back Button & Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToLogin) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Tạo tài khoản mới",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input 1: Họ và tên
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = { Text("Họ và tên", fontSize = 14.sp, color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Input 2: Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", fontSize = 14.sp, color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Input 3: Mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Mật khẩu", fontSize = 14.sp, color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
            trailingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Input 4: Xác nhận mật khẩu
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Xác nhận mật khẩu", fontSize = 14.sp, color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
            trailingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Terms Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = agreeTerms,
                onCheckedChange = { agreeTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
            )
            Text(
                text = "Tôi đồng ý với Điều khoản sử dụng và Chính sách bảo mật",
                fontSize = 12.5.sp,
                color = TextSecondary,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onRegisterSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text(text = "Đăng ký", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurfaceWhite)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Social options
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = BorderLight)
            Text(text = "  Hoặc đăng ký với  ", fontSize = 12.sp, color = TextMuted)
            Divider(modifier = Modifier.weight(1f), color = BorderLight)
        }

        Spacer(modifier = Modifier.height(16.dp))

        SocialButtonRow(onGoogleClick = onRegisterSuccess, onFacebookClick = onRegisterSuccess)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBackToLogin() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Đã có tài khoản? ", fontSize = 13.sp, color = TextSecondary)
            Text(text = "Đăng nhập", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        }
    }
}

// SCREEN 6: QUÊN MẬT KHẨU
@Composable
fun ForgotPasswordScreen(
    onSubmitEmail: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Quên mật khẩu",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(top = 10.dp, bottom = 24.dp)
            )

            ForgotPasswordIllustration(size = 180.dp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Nhập email của bạn để nhận\nliên kết đặt lại mật khẩu.",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email của bạn", fontSize = 14.sp, color = TextMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onSubmitEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(text = "Gửi liên kết", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurfaceWhite)
            }
        }

        Text(
            text = "Quay lại đăng nhập",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            modifier = Modifier
                .clickable { onBackToLogin() }
                .padding(bottom = 16.dp)
        )
    }
}

// SCREEN 7: ĐẶT LẠI MẬT KHẨU
@Composable
fun ResetPasswordScreen(
    onResetSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Đặt lại mật khẩu",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text("Mật khẩu mới", fontSize = 14.sp, color = TextMuted) },
                trailingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmNewPassword,
                onValueChange = { confirmNewPassword = it },
                placeholder = { Text("Xác nhận mật khẩu mới", fontSize = 14.sp, color = TextMuted) },
                trailingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, unfocusedBorderColor = BorderLight)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "• Tối thiểu 6 ký tự", fontSize = 12.5.sp, color = TextSecondary)
            Text(text = "• Bao gồm chữ và số", fontSize = 12.5.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = onResetSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(text = "Đặt lại mật khẩu", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurfaceWhite)
            }
        }

        Text(
            text = "Quay lại đăng nhập",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            modifier = Modifier
                .clickable { onBackToLogin() }
                .padding(bottom = 16.dp)
        )
    }
}

// SCREEN 8: ĐĂNG NHẬP THÀNH CÔNG!
@Composable
fun AuthSuccessScreen(
    onEnterApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SuccessCircleIllustration(size = 180.dp)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Đăng nhập thành công!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Chào bạn trở lại",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        Button(
            onClick = onEnterApp,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) {
            Text(text = "Vào ứng dụng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurfaceWhite)
        }
    }
}

@Composable
private fun SocialButtonRow(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Google button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                .background(SurfaceWhite)
                .clickable { onGoogleClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Colored G circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEA4335)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("G", color = SurfaceWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Google", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            }
        }

        // Facebook button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                .background(SurfaceWhite)
                .clickable { onFacebookClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // FB Blue circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1877F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("f", color = SurfaceWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Facebook", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            }
        }
    }
}
