package com.algorithmx.medmate.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MedPrimaryDark,
    secondary = MedPrimaryLight, // Use teal accents in dark mode
    background = MedBackgroundDark,
    surface = MedSurfaceDark,
    surfaceVariant = Color(0xFF2C2C2C),
    onPrimary = Color.Black,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

private val LightColorScheme = lightColorScheme(
    primary = MedPrimary,
    secondary = MedSecondary,
    tertiary = MedWarning,
    background = MedBackground,
    surface = MedSurface,
    primaryContainer = MedPrimaryContainer,
    secondaryContainer = MedSecondaryContainer,
    onPrimary = Color.White,
    onBackground = Color(0xFF191C1C),
    onSurface = Color(0xFF191C1C)
)

@Composable
fun MedMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Keep your existing Type.kt
        content = content
    )
}