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
    primaryContainer = Color(0xFFE7F1E6), onPrimaryContainer = Color(0xFF2E6B45),
    inversePrimary = Color(0xFFA4D5A6),
    secondary = Color(0xFF536457), onSecondary = Color.White,
    secondaryContainer = Color(0xFFE7F1E6), onSecondaryContainer = Color(0xFF1D2B20),
    tertiary = Color(0xFF8A5B16), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFF1D5), onTertiaryContainer = Color(0xFF8A5B16),
    background = Color(0xFFF6F8F4), onBackground = Color(0xFF1D2B20),
    surface = Color.White, onSurface = Color(0xFF1D2B20),
    surfaceVariant = Color(0xFFF6F8F4), onSurfaceVariant = Color(0xFF536457),
    surfaceTint = Color.Transparent,
    inverseSurface = Color(0xFF1F2922), inverseOnSurface = Color(0xFFE4EDE3),
    surfaceBright = Color.White, surfaceDim = Color(0xFFF6F8F4),
    surfaceContainerLowest = Color.White, surfaceContainerLow = Color.White,
    surfaceContainer = Color.White, surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color(0xFFE7F1E6),
    outline = Color(0xFFD2DCD1), outlineVariant = Color(0xFFD2DCD1),
    error = Color(0xFF9D3933), onError = Color.White,
    errorContainer = Color(0xFFFCE9E7), onErrorContainer = Color(0xFF9D3933),
    scrim = Color.Black,
)

private val darkColors = darkColorScheme(
    primary = Color(0xFFA4D5A6), onPrimary = Color(0xFF143821),
    primaryContainer = Color(0xFF253E2C), onPrimaryContainer = Color(0xFFA4D5A6),
    inversePrimary = Color(0xFF2E6B45),
    secondary = Color(0xFFB4C3B4), onSecondary = Color(0xFF143821),
    secondaryContainer = Color(0xFF253E2C), onSecondaryContainer = Color(0xFFE4EDE3),
    tertiary = Color(0xFFF0C77B), onTertiary = Color(0xFF45371F),
    tertiaryContainer = Color(0xFF45371F), onTertiaryContainer = Color(0xFFF0C77B),
    background = Color(0xFF151C17), onBackground = Color(0xFFE4EDE3),
    surface = Color(0xFF1F2922), onSurface = Color(0xFFE4EDE3),
    surfaceVariant = Color(0xFF253E2C), onSurfaceVariant = Color(0xFFB4C3B4),
    surfaceTint = Color.Transparent,
    inverseSurface = Color(0xFFE4EDE3), inverseOnSurface = Color(0xFF1F2922),
    surfaceBright = Color(0xFF253E2C), surfaceDim = Color(0xFF151C17),
    surfaceContainerLowest = Color(0xFF151C17), surfaceContainerLow = Color(0xFF1F2922),
    surfaceContainer = Color(0xFF1F2922), surfaceContainerHigh = Color(0xFF1F2922),
    surfaceContainerHighest = Color(0xFF253E2C),
    outline = Color(0xFF465448), outlineVariant = Color(0xFF465448),
    error = Color(0xFFFFB4AB), onError = Color(0xFF4B2624),
    errorContainer = Color(0xFF4B2624), onErrorContainer = Color(0xFFFFB4AB),
    scrim = Color.Black,
)

private val plantTypography = Typography(
    titleLarge = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Medium),
    headlineSmall = TextStyle(fontSize = 26.sp, lineHeight = 32.sp, fontWeight = FontWeight.Medium),
    titleMedium = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
)

@Composable
fun PlantTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors,
        typography = plantTypography,
        content = content,
    )
}
