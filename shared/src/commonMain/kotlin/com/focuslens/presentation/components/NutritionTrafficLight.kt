package com.focuslens.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focuslens.domain.model.NutritionFeedback
import com.focuslens.presentation.theme.*
import kotlinx.coroutines.delay

/** Muestra el análisis nutricional con íconos y colores según el tipo de feedback. */
@Composable
fun NutritionTrafficLight(
    feedbacks: List<NutritionFeedback>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier  = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        feedbacks.forEachIndexed { index, feedback ->
            // ── Animación staggered para cada item ──────────────────────
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(feedback) {
                delay(index * 80L)
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(400)) +
                          slideInHorizontally(tween(400, easing = EaseOutCubic)) { -40 }
            ) {
                FeedbackItem(feedback)
            }
        }
    }
}

@Composable
private fun FeedbackItem(feedback: NutritionFeedback) {
    val (bgColor, icon, textColor) = when (feedback) {
        is NutritionFeedback.Good    -> Triple(TrafficGreen.copy(alpha = 0.12f),  Icons.Default.CheckCircle, TrafficGreen)
        is NutritionFeedback.Warning -> Triple(TrafficRed.copy(alpha = 0.12f),    Icons.Default.Warning,     TrafficRed)
        is NutritionFeedback.Info    -> Triple(AccentAmber.copy(alpha = 0.12f),   Icons.Default.Info,        AccentAmber)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint     = textColor,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text       = feedback.nutrient,
                style      = MaterialTheme.typography.labelLarge,
                color      = textColor,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text  = feedback.message,
                style = MaterialTheme.typography.bodyMedium,
                color = NutriOnSurface
            )
        }
    }
}
