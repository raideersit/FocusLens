package com.focuslens.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focuslens.presentation.theme.*

/**
 * Barra de progreso nutricional animada.
 * Muestra el valor actual vs el objetivo con colores del semáforo.
 */
@Composable
fun NutrientBar(
    label: String,
    value: Double,
    maxValue: Double,
    unit: String = "g",
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    val fraction = if (maxValue > 0) (value / maxValue).coerceIn(0.0, 1.0).toFloat() else 0f

    var started by remember { mutableStateOf(false) }
    val animatedFraction by animateFloatAsState(
        targetValue   = if (started) fraction else 0f,
        animationSpec = tween(durationMillis = 900, easing = androidx.compose.animation.core.EaseOutCubic),
        label         = "nutrientBarAnim"
    )

    LaunchedEffect(Unit) { started = true }

    Column(modifier = modifier) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = label,
                style = MaterialTheme.typography.bodyMedium,
                color = NutriOnSurface
            )
            Text(
                text       = "${formatOneDecimal(value)} $unit",
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color      = color
            )
        }

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(NutriSurface2)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

/** KMP-compatible decimal formatting (replaces String.format) */
private fun formatOneDecimal(value: Double): String {
    val intPart = value.toLong()
    val decPart = ((value - intPart) * 10).toLong().let { kotlin.math.abs(it) }
    return "$intPart.$decPart"
}
