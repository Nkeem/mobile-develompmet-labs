package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val lightColors = lightColorScheme(
    primary = Color(0xFF2E6B45), onPrimary = Color.White,
    background = Color(0xFFF6F8F4), onBackground = Color(0xFF1D2B20),
    surface = Color.White, onSurface = Color(0xFF1D2B20),
    onSurfaceVariant = Color(0xFF536457), outline = Color(0xFFD2DCD1),
    primaryContainer = Color(0xFFE7F1E6), onPrimaryContainer = Color(0xFF2E6B45),
    error = Color(0xFF9D3933), errorContainer = Color(0xFFFCE9E7),
)

private val darkColors = darkColorScheme(
    primary = Color(0xFFA4D5A6), onPrimary = Color(0xFF143821),
    background = Color(0xFF151C17), onBackground = Color(0xFFE4EDE3),
    surface = Color(0xFF1F2922), onSurface = Color(0xFFE4EDE3),
    onSurfaceVariant = Color(0xFFB4C3B4), outline = Color(0xFF465448),
    primaryContainer = Color(0xFF253E2C), onPrimaryContainer = Color(0xFFA4D5A6),
    error = Color(0xFFFFB4AB), errorContainer = Color(0xFF4B2624),
)

private val plantTypography = Typography(
    titleLarge = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
    headlineSmall = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    bodySmall = TextStyle(fontSize = 12.sp),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontSize = 12.sp),
)

@Composable
fun PlantTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors,
        typography = plantTypography,
        content = content,
    )
}
