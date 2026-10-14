package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LightMint
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SecondaryGreen

@Composable
fun ChefLogo(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    showBackgroundCircle: Boolean = false,
    outlineColor: Color = PrimaryGreen
) {
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showBackgroundCircle) Modifier.background(LightMint, CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.85f)) {
            val w = this.size.width
            val h = this.size.height

            val strokeW = (w * 0.045f).coerceAtLeast(2.5f)

            // 1. Hat Base (lower rectangle with rounded bottom)
            drawRoundRect(
                color = outlineColor,
                topLeft = Offset(w * 0.28f, h * 0.54f),
                size = Size(w * 0.44f, h * 0.26f),
                cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
                style = Stroke(width = strokeW)
            )

            // 2. Hat Puffs (triple clouds)
            // Left puff
            drawCircle(
                color = outlineColor,
                radius = w * 0.16f,
                center = Offset(w * 0.33f, h * 0.44f),
                style = Stroke(width = strokeW)
            )
            // Center puff (taller)
            drawCircle(
                color = outlineColor,
                radius = w * 0.19f,
                center = Offset(w * 0.50f, h * 0.35f),
                style = Stroke(width = strokeW)
            )
            // Right puff
            drawCircle(
                color = outlineColor,
                radius = w * 0.16f,
                center = Offset(w * 0.67f, h * 0.44f),
                style = Stroke(width = strokeW)
            )

            // Fill the inner puffs with white to mask overlapping circles
            drawCircle(
                color = Color.White,
                radius = w * 0.14f,
                center = Offset(w * 0.33f, h * 0.44f)
            )
            drawCircle(
                color = Color.White,
                radius = w * 0.17f,
                center = Offset(w * 0.50f, h * 0.35f)
            )
            drawCircle(
                color = Color.White,
                radius = w * 0.14f,
                center = Offset(w * 0.67f, h * 0.44f)
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.29f, h * 0.55f),
                size = Size(w * 0.42f, h * 0.24f),
                cornerRadius = CornerRadius(w * 0.07f, w * 0.07f)
            )

            // Redraw outer hat lines clean
            drawRoundRect(
                color = outlineColor,
                topLeft = Offset(w * 0.28f, h * 0.54f),
                size = Size(w * 0.44f, h * 0.26f),
                cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
                style = Stroke(width = strokeW)
            )

            // 3. Smiling face inside hat
            // Left Eye
            drawCircle(
                color = outlineColor,
                radius = w * 0.028f,
                center = Offset(w * 0.41f, h * 0.64f)
            )
            // Right Eye
            drawCircle(
                color = outlineColor,
                radius = w * 0.028f,
                center = Offset(w * 0.59f, h * 0.64f)
            )
            // Smile
            val smilePath = Path().apply {
                moveTo(w * 0.44f, h * 0.68f)
                quadraticTo(w * 0.50f, h * 0.74f, w * 0.56f, h * 0.68f)
            }
            drawPath(
                path = smilePath,
                color = outlineColor,
                style = Stroke(width = strokeW, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            // 4. Fresh green sprout leaves on the left
            val leafPath = Path().apply {
                moveTo(w * 0.22f, h * 0.50f)
                quadraticTo(w * 0.10f, h * 0.46f, w * 0.13f, h * 0.36f)
                quadraticTo(w * 0.22f, h * 0.38f, w * 0.24f, h * 0.46f)
                close()
            }
            drawPath(path = leafPath, color = SecondaryGreen)
        }
    }
}
