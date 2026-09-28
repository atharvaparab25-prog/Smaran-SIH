package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SmaranGreenPrimaryDark,
    onPrimary = Color.Black,
    primaryContainer = SmaranGreenContainerDark,
    onPrimaryContainer = Color.White,
    secondary = SmaranLavender,
    onSecondary = Color.White,
    tertiary = SmaranAmber,
    onTertiary = Color.White,
    background = SmaranBackgroundDark,
    onBackground = SmaranTextPrimaryDark,
    surface = SmaranSurfaceDark,
    onSurface = SmaranTextPrimaryDark,
    surfaceVariant = Color(0xFF23312A),
    onSurfaceVariant = SmaranTextSecondaryDark,
    error = Color(0xFFEF5350),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = SmaranGreenPrimary,
    onPrimary = SmaranGreenOnPrimary,
    primaryContainer = SmaranGreenContainer,
    onPrimaryContainer = SmaranGreenOnContainer,
    secondary = SmaranLavender,
    onSecondary = Color.White,
    secondaryContainer = SmaranLavenderContainer,
    onSecondaryContainer = Color(0xFF321959),
    tertiary = SmaranAmber,
    onTertiary = Color.White,
    tertiaryContainer = SmaranAmberContainer,
    onTertiaryContainer = Color(0xFF5A2A00),
    background = SmaranBackground,
    onBackground = SmaranTextPrimary,
    surface = SmaranSurface,
    onSurface = SmaranTextPrimary,
    surfaceVariant = SmaranSurfaceVariant,
    onSurfaceVariant = SmaranTextSecondary,
    error = SmaranAccentRed,
    onError = Color.White,
    errorContainer = SmaranAccentRedContainer,
    onErrorContainer = SmaranAccentRedOnContainer
)

private val HighContrastColorScheme = lightColorScheme(
    primary = Color(0xFF00381D),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC7F2DB),
    onPrimaryContainer = Color(0xFF00210E),
    secondary = Color(0xFF2D1B69),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFF2F5F3),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFE2EBE5),
    onSurfaceVariant = Color(0xFF000000),
    error = Color(0xFFB71C1C),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun SmaranTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast -> HighContrastColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getSmaranTypography(fontScale),
        content = content
    )
}
