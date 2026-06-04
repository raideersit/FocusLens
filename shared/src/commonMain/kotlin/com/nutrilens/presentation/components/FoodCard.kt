package com.nutrilens.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nutrilens.domain.model.FoodScan
import com.nutrilens.presentation.theme.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Tarjeta de historial de escaneo.
 * Muestra nombre, marca, calorías y fecha del escaneo.
 * Efecto de escala al presionar para feedback táctil premium.
 */
@Composable
fun FoodCard(
    scan: FoodScan,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.97f else 1f,
        animationSpec = tween(150),
        label         = "cardScale"
    )

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                onClick = {
                    onClick()
                }
            ),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = NutriSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier  = Modifier.padding(12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Placeholder de imagen (KMP-compatible, sin Coil)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NutriSurface2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "🥫",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Info del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = scan.food.name,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = NutriOnBackground,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                scan.food.brand?.let {
                    Text(
                        text     = it,
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = NutriSubtext,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "${scan.food.nutrition.calories.toInt()} kcal",
                        style      = MaterialTheme.typography.labelLarge,
                        color      = NutriGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text  = formatTimestamp(scan.scannedAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = NutriSubtext
                    )
                }
            }

            // NutriScore badge
            scan.food.nutriScore?.let { score ->
                NutriScoreBadge(score = score)
            }
        }
    }
}

@Composable
fun NutriScoreBadge(score: String, modifier: Modifier = Modifier) {
    val color = when (score.uppercase()) {
        "A" -> NutriScoreA
        "B" -> NutriScoreB
        "C" -> NutriScoreC
        "D" -> NutriScoreD
        "E" -> NutriScoreE
        else -> NutriSubtext
    }
    Box(
        modifier          = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color),
        contentAlignment  = Alignment.Center
    ) {
        Text(
            text       = score.uppercase(),
            style      = MaterialTheme.typography.titleMedium,
            color      = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/** KMP-compatible timestamp formatting using kotlinx-datetime */
private fun formatTimestamp(millis: Long): String {
    return try {
        val instant = Instant.fromEpochMilliseconds(millis)
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = local.dayOfMonth.toString().padStart(2, '0')
        val month = local.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        val hour = local.hour.toString().padStart(2, '0')
        val minute = local.minute.toString().padStart(2, '0')
        "$day $month, $hour:$minute"
    } catch (_: Exception) {
        ""
    }
}
