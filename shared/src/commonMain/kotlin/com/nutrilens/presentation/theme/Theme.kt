package com.nutrilens.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val NutriDarkColorScheme = darkColorScheme(
    primary            = NutriGreen,
    onPrimary          = NutriBackground,
    primaryContainer   = NutriGreenDark,
    secondary          = NutriTeal,
    onSecondary        = NutriBackground,
    secondaryContainer = NutriTealDark,
    tertiary           = AccentAmber,
    onTertiary         = NutriBackground,
    background         = NutriBackground,
    onBackground       = NutriOnBackground,
    surface            = NutriSurface,
    onSurface          = NutriOnSurface,
    surfaceVariant     = NutriSurface2,
    error              = TrafficRed,
    onError            = NutriOnBackground
)

private val NutriShapes = Shapes(
    small       = RoundedCornerShape(10.dp),
    medium      = RoundedCornerShape(16.dp),
    large       = RoundedCornerShape(22.dp),
    extraLarge  = RoundedCornerShape(28.dp)
)

@Composable
fun NutriLensTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NutriDarkColorScheme,
        typography  = NutriTypography,
        shapes      = NutriShapes,
        content     = content
    )
}
