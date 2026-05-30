package com.nutrilens.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NutriDarkColorScheme = darkColorScheme(
    primary          = NutriGreen,
    onPrimary        = NutriBackground,
    primaryContainer = NutriGreenDark,
    secondary        = NutriTeal,
    onSecondary      = NutriBackground,
    background       = NutriBackground,
    onBackground     = NutriOnBackground,
    surface          = NutriSurface,
    onSurface        = NutriOnSurface,
    surfaceVariant   = NutriSurface2,
    error            = TrafficRed,
    onError          = NutriOnBackground
)

@Composable
fun NutriLensTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NutriDarkColorScheme,
        typography  = NutriTypography,
        content     = content
    )
}
