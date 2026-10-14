package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ChartFood
import com.example.ui.theme.ChartLiving
import com.example.ui.theme.ChartMarket
import com.example.ui.theme.LightMint
import com.example.ui.theme.LightMintSurface
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen

@Composable
fun FeastIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 200.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.radialGradient(listOf(Color(0xFFFFFBEB), Color(0xFFF3F4F6)))),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Wooden table background
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFFDE68A), Color(0xFFD97706))),
                topLeft = Offset(w * 0.08f, h * 0.35f),
                size = Size(w * 0.84f, h * 0.58f),
                cornerRadius = CornerRadius(16f, 16f)
            )

            // Main braised fish dish (large plate)
            drawCircle(
                color = Color.White,
                radius = w * 0.22f,
                center = Offset(w * 0.45f, h * 0.52f)
            )
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFFB45309), Color(0xFF78350F))),
                radius = w * 0.18f,
                center = Offset(w * 0.45f, h * 0.52f)
            )
            // Golden fish pieces & ginger garnish
            drawRoundRect(
                color = Color(0xFFD97706),
                topLeft = Offset(w * 0.35f, h * 0.46f),
                size = Size(w * 0.18f, h * 0.10f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            // White sesame / green herbs
            drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(w * 0.45f, h * 0.48f))
            drawCircle(color = Color(0xFF10B981), radius = 5f, center = Offset(w * 0.48f, h * 0.54f))

            // Bowl of white rice on left
            drawCircle(color = Color.White, radius = w * 0.14f, center = Offset(w * 0.22f, h * 0.72f))
            drawCircle(color = Color(0xFFF3F4F6), radius = w * 0.11f, center = Offset(w * 0.22f, h * 0.71f))

            // Vegetable / boiled greens plate on right
            drawCircle(color = Color.White, radius = w * 0.16f, center = Offset(w * 0.74f, h * 0.68f))
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFF34D399), Color(0xFF059669))),
                radius = w * 0.13f,
                center = Offset(w * 0.74f, h * 0.68f)
            )
            // Tomato accent on greens
            drawCircle(color = Color(0xFFEF4444), radius = w * 0.04f, center = Offset(w * 0.72f, h * 0.65f))

            // Fresh tomato on the side
            drawCircle(color = Color(0xFFEF4444), radius = w * 0.07f, center = Offset(w * 0.14f, h * 0.54f))
            drawCircle(color = Color(0xFF15803D), radius = 5f, center = Offset(w * 0.14f, h * 0.48f))

            // Olive oil bottle top left
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFFBBF24), Color(0xFFD97706))),
                topLeft = Offset(w * 0.10f, h * 0.20f),
                size = Size(w * 0.10f, h * 0.25f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = Color(0xFF78350F),
                topLeft = Offset(w * 0.12f, h * 0.16f),
                size = Size(w * 0.06f, h * 0.05f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

@Composable
fun AiRobotIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 190.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(20.dp))
            .background(LightMint),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Smartphone frame on left
            drawRoundRect(
                color = PrimaryGreen,
                topLeft = Offset(w * 0.14f, h * 0.20f),
                size = Size(w * 0.34f, h * 0.62f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.16f, h * 0.24f),
                size = Size(w * 0.30f, h * 0.54f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            // App UI cards inside smartphone
            drawRoundRect(
                color = Color(0xFFDCFCE7),
                topLeft = Offset(w * 0.19f, h * 0.28f),
                size = Size(w * 0.24f, h * 0.08f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = Color(0xFFF1F5F9),
                topLeft = Offset(w * 0.19f, h * 0.38f),
                size = Size(w * 0.24f, h * 0.06f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = Color(0xFFFEF3C7),
                topLeft = Offset(w * 0.19f, h * 0.46f),
                size = Size(w * 0.24f, h * 0.06f),
                cornerRadius = CornerRadius(6f, 6f)
            )

            // Robot Chef on right
            // Chef hat
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.60f, h * 0.18f),
                size = Size(w * 0.24f, h * 0.15f),
                cornerRadius = CornerRadius(14f, 14f)
            )
            drawCircle(color = Color.White, radius = w * 0.09f, center = Offset(w * 0.72f, h * 0.16f))
            drawCircle(color = Color.White, radius = w * 0.07f, center = Offset(w * 0.64f, h * 0.19f))
            drawCircle(color = Color.White, radius = w * 0.07f, center = Offset(w * 0.80f, h * 0.19f))

            // Robot Head
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.58f, h * 0.32f),
                size = Size(w * 0.28f, h * 0.26f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            // Screen Face (Dark green/teal screen)
            drawRoundRect(
                color = Color(0xFF064E3B),
                topLeft = Offset(w * 0.61f, h * 0.35f),
                size = Size(w * 0.22f, h * 0.18f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            // Glowing Robot Eyes (green arc)
            drawCircle(color = Color(0xFF34D399), radius = 6f, center = Offset(w * 0.67f, h * 0.43f))
            drawCircle(color = Color(0xFF34D399), radius = 6f, center = Offset(w * 0.77f, h * 0.43f))

            // Smile on Robot
            val smile = Path().apply {
                moveTo(w * 0.69f, h * 0.47f)
                quadraticTo(w * 0.72f, h * 0.50f, w * 0.75f, h * 0.47f)
            }
            drawPath(smile, color = Color(0xFF34D399), style = Stroke(width = 3f))

            // Robot Body & Veggie Bowl
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.60f, h * 0.58f),
                size = Size(w * 0.24f, h * 0.24f),
                cornerRadius = CornerRadius(14f, 14f)
            )
            // Bowl of vegetables held by robot
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFF97316), Color(0xFFC2410C))),
                topLeft = Offset(w * 0.54f, h * 0.68f),
                size = Size(w * 0.34f, h * 0.15f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            drawCircle(color = Color(0xFF10B981), radius = 10f, center = Offset(w * 0.62f, h * 0.66f))
            drawCircle(color = Color(0xFFEF4444), radius = 9f, center = Offset(w * 0.72f, h * 0.67f))
            drawCircle(color = Color(0xFFFBBF24), radius = 8f, center = Offset(w * 0.80f, h * 0.67f))
        }
    }
}

@Composable
fun FastInputIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 190.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(20.dp))
            .background(LightMint),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Floating Shopping Cart on Top Left (Cyan circle)
            drawCircle(color = Color(0xFFE0F2FE), radius = w * 0.14f, center = Offset(w * 0.25f, h * 0.26f))
            drawRoundRect(
                color = Color(0xFF0284C7),
                topLeft = Offset(w * 0.19f, h * 0.24f),
                size = Size(w * 0.12f, h * 0.08f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(color = Color(0xFF0284C7), radius = 4f, center = Offset(w * 0.21f, h * 0.33f))
            drawCircle(color = Color(0xFF0284C7), radius = 4f, center = Offset(w * 0.28f, h * 0.33f))

            // Floating Microphone on Top Right (Orange circle)
            drawCircle(color = Color(0xFFFEF3C7), radius = w * 0.14f, center = Offset(w * 0.75f, h * 0.26f))
            drawRoundRect(
                color = Color(0xFFD97706),
                topLeft = Offset(w * 0.72f, h * 0.20f),
                size = Size(w * 0.06f, h * 0.10f),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = Color(0xFFD97706),
                topLeft = Offset(w * 0.74f, h * 0.30f),
                size = Size(w * 0.02f, h * 0.04f),
                cornerRadius = CornerRadius(2f, 2f)
            )

            // Center chart badge
            drawCircle(color = Color(0xFFDCFCE7), radius = w * 0.16f, center = Offset(w * 0.50f, h * 0.34f))
            // Colorful bar charts inside badge
            drawRoundRect(color = Color(0xFFEF4444), topLeft = Offset(w * 0.43f, h * 0.32f), size = Size(w * 0.03f, h * 0.08f), cornerRadius = CornerRadius(3f, 3f))
            drawRoundRect(color = Color(0xFF10B981), topLeft = Offset(w * 0.48f, h * 0.26f), size = Size(w * 0.03f, h * 0.14f), cornerRadius = CornerRadius(3f, 3f))
            drawRoundRect(color = Color(0xFF3B82F6), topLeft = Offset(w * 0.53f, h * 0.29f), size = Size(w * 0.03f, h * 0.11f), cornerRadius = CornerRadius(3f, 3f))

            // Bottom large UI card
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.12f, h * 0.48f),
                size = Size(w * 0.76f, h * 0.42f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            // Top card header with 3 dots
            drawRoundRect(
                color = Color(0xFFF1F5F9),
                topLeft = Offset(w * 0.12f, h * 0.48f),
                size = Size(w * 0.76f, h * 0.09f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawCircle(color = Color(0xFFCBD5E1), radius = 4f, center = Offset(w * 0.20f, h * 0.525f))
            drawCircle(color = Color(0xFFCBD5E1), radius = 4f, center = Offset(w * 0.25f, h * 0.525f))
            drawCircle(color = Color(0xFFCBD5E1), radius = 4f, center = Offset(w * 0.30f, h * 0.525f))

            // Item row 1
            drawCircle(color = Color(0xFFF97316), radius = 12f, center = Offset(w * 0.22f, h * 0.65f))
            drawRoundRect(color = Color(0xFFE2E8F0), topLeft = Offset(w * 0.32f, h * 0.63f), size = Size(w * 0.24f, h * 0.025f), cornerRadius = CornerRadius(4f, 4f))
            drawRoundRect(color = Color(0xFF86EFAC), topLeft = Offset(w * 0.68f, h * 0.62f), size = Size(w * 0.14f, h * 0.035f), cornerRadius = CornerRadius(6f, 6f))

            // Item row 2
            drawCircle(color = Color(0xFF10B981), radius = 12f, center = Offset(w * 0.22f, h * 0.78f))
            drawRoundRect(color = Color(0xFFE2E8F0), topLeft = Offset(w * 0.32f, h * 0.76f), size = Size(w * 0.24f, h * 0.025f), cornerRadius = CornerRadius(4f, 4f))
            drawRoundRect(color = Color(0xFF86EFAC), topLeft = Offset(w * 0.68f, h * 0.75f), size = Size(w * 0.14f, h * 0.035f), cornerRadius = CornerRadius(6f, 6f))
        }
    }
}

@Composable
fun ForgotPasswordIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Yellow open letter envelope
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFFBBF24), Color(0xFFD97706))),
                topLeft = Offset(w * 0.25f, h * 0.40f),
                size = Size(w * 0.50f, h * 0.36f),
                cornerRadius = CornerRadius(10f, 10f)
            )

            // Letter flap open
            val flap = Path().apply {
                moveTo(w * 0.25f, h * 0.40f)
                lineTo(w * 0.50f, h * 0.24f)
                lineTo(w * 0.75f, h * 0.40f)
                close()
            }
            drawPath(flap, color = Color(0xFFFEF3C7))

            // Gold Lock in center
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(w * 0.44f, h * 0.32f),
                size = Size(w * 0.16f, h * 0.16f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawCircle(
                color = Color(0xFF1E293B),
                radius = w * 0.07f,
                center = Offset(w * 0.52f, h * 0.32f),
                style = Stroke(width = 6f)
            )
            // Lock keyhole
            drawCircle(color = Color(0xFFFBBF24), radius = 4f, center = Offset(w * 0.52f, h * 0.39f))

            // Green Key on left
            drawCircle(color = SecondaryGreen, radius = 10f, center = Offset(w * 0.24f, h * 0.54f), style = Stroke(width = 6f))
            drawRoundRect(
                color = SecondaryGreen,
                topLeft = Offset(w * 0.28f, h * 0.52f),
                size = Size(w * 0.15f, h * 0.04f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = SecondaryGreen,
                topLeft = Offset(w * 0.38f, h * 0.55f),
                size = Size(w * 0.03f, h * 0.05f),
                cornerRadius = CornerRadius(2f, 2f)
            )

            // Sparkles
            drawCircle(color = Color(0xFFFCD34D), radius = 3f, center = Offset(w * 0.18f, h * 0.36f))
            drawCircle(color = Color(0xFF86EFAC), radius = 4f, center = Offset(w * 0.80f, h * 0.32f))
        }
    }
}

@Composable
fun SuccessCircleIllustration(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Big Green Circle
            drawCircle(
                brush = Brush.linearGradient(listOf(SecondaryGreen, PrimaryGreen)),
                radius = w * 0.38f,
                center = Offset(w * 0.50f, h * 0.50f)
            )

            // Crisp White Checkmark
            val checkPath = Path().apply {
                moveTo(w * 0.34f, h * 0.50f)
                lineTo(w * 0.46f, h * 0.62f)
                lineTo(w * 0.66f, h * 0.40f)
            }
            drawPath(
                path = checkPath,
                color = Color.White,
                style = Stroke(width = 12f, cap = androidx.compose.ui.graphics.StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
            )

            // Colorful Confetti dots
            drawCircle(color = Color(0xFFF59E0B), radius = 5f, center = Offset(w * 0.18f, h * 0.22f))
            drawCircle(color = Color(0xFFEC4899), radius = 6f, center = Offset(w * 0.82f, h * 0.28f))
            drawCircle(color = Color(0xFF3B82F6), radius = 4f, center = Offset(w * 0.22f, h * 0.78f))
            drawCircle(color = Color(0xFF10B981), radius = 5f, center = Offset(w * 0.85f, h * 0.72f))
            drawCircle(color = Color(0xFF8B5CF6), radius = 4f, center = Offset(w * 0.50f, h * 0.08f))
        }
    }
}

@Composable
fun DishThumbnail(
    dishName: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(LightMintSurface),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            val nameLower = dishName.lowercase()
            if (nameLower.contains("cá kho") || nameLower.contains("cá")) {
                // Clay pot dark brown with caramelized fish & ginger
                drawCircle(color = Color(0xFF78350F), radius = w * 0.44f, center = Offset(w * 0.50f, h * 0.50f))
                drawCircle(color = Color(0xFF92400E), radius = w * 0.38f, center = Offset(w * 0.50f, h * 0.50f))
                drawRoundRect(
                    color = Color(0xFFD97706),
                    topLeft = Offset(w * 0.26f, h * 0.36f),
                    size = Size(w * 0.48f, h * 0.26f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawCircle(color = Color(0xFF10B981), radius = 3.5f, center = Offset(w * 0.50f, h * 0.45f))
                drawCircle(color = Color(0xFFEF4444), radius = 3.5f, center = Offset(w * 0.58f, h * 0.52f))
            } else if (nameLower.contains("thịt") || nameLower.contains("sườn") || nameLower.contains("gà")) {
                // Braised pork with quail eggs & golden ribs
                drawCircle(color = Color(0xFFFEF3C7), radius = w * 0.44f, center = Offset(w * 0.50f, h * 0.50f))
                drawCircle(color = Color(0xFFFDE68A), radius = w * 0.38f, center = Offset(w * 0.50f, h * 0.50f))
                // Braised pork chunks
                drawRoundRect(
                    color = Color(0xFFB45309),
                    topLeft = Offset(w * 0.28f, h * 0.34f),
                    size = Size(w * 0.26f, h * 0.26f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0xFF92400E),
                    topLeft = Offset(w * 0.52f, h * 0.42f),
                    size = Size(w * 0.24f, h * 0.24f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // Quail egg
                drawCircle(color = Color.White, radius = w * 0.10f, center = Offset(w * 0.40f, h * 0.65f))
                drawCircle(color = Color(0xFFFBBF24), radius = w * 0.05f, center = Offset(w * 0.40f, h * 0.65f))
            } else if (nameLower.contains("canh")) {
                // Sweet and sour fish soup bowl with tomatoes and pineapple
                drawCircle(color = Color(0xFFE0F2FE), radius = w * 0.44f, center = Offset(w * 0.50f, h * 0.50f))
                drawCircle(color = Color.White, radius = w * 0.38f, center = Offset(w * 0.50f, h * 0.50f))
                // Soup broth
                drawCircle(color = Color(0xFFFEF9C3), radius = w * 0.32f, center = Offset(w * 0.50f, h * 0.50f))
                // Red tomato slice
                drawCircle(color = Color(0xFFEF4444), radius = w * 0.11f, center = Offset(w * 0.38f, h * 0.44f))
                // Yellow pineapple chunk
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(w * 0.52f, h * 0.48f),
                    size = Size(w * 0.20f, h * 0.16f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                // Herbs
                drawCircle(color = Color(0xFF10B981), radius = 3.5f, center = Offset(w * 0.48f, h * 0.38f))
            } else {
                // White bowl with boiled cabbage or fresh vegetables
                drawCircle(color = Color(0xFFE2E8F0), radius = w * 0.44f, center = Offset(w * 0.50f, h * 0.50f))
                drawCircle(color = Color.White, radius = w * 0.38f, center = Offset(w * 0.50f, h * 0.50f))
                // Fresh green cabbage leaves
                drawCircle(color = Color(0xFF86EFAC), radius = w * 0.24f, center = Offset(w * 0.44f, h * 0.48f))
                drawCircle(color = Color(0xFF4ADE80), radius = w * 0.20f, center = Offset(w * 0.58f, h * 0.52f))
                drawCircle(color = Color(0xFF22C55E), radius = w * 0.16f, center = Offset(w * 0.48f, h * 0.44f))
            }
        }
    }
}
