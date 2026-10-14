package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = SurfaceWhite,
    primaryContainer = LightMint,
    onPrimaryContainer = PrimaryGreenDark,
    secondary = SecondaryGreen,
    onSecondary = SurfaceWhite,
    secondaryContainer = LightMintSurface,
    onSecondaryContainer = PrimaryGreenDark,
    background = BackgroundApp,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = LightMintSurface,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Forced clean white/green theme as per reference design specifications
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = SurfaceWhite.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
