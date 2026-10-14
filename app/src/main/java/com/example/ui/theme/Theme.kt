package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AeroOptimaColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = Color(0xFF00363D),
    primaryContainer = ElectricBlue,
    onPrimaryContainer = Color.White,
    secondary = SkyBlue,
    onSecondary = Color(0xFF00344D),
    secondaryContainer = NavySurfaceVariant,
    onSecondaryContainer = SkyBlue,
    tertiary = StatusPurple,
    onTertiary = Color.White,
    background = NavyBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = NavyCardBorder,
    error = StatusDanger,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to high-contrast dark aerospace SaaS theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AeroOptimaColorScheme,
        typography = Typography,
        content = content
    )
}
