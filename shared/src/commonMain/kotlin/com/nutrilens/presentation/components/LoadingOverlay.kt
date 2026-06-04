package com.nutrilens.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrilens.presentation.theme.NutriBackground
import com.nutrilens.presentation.theme.NutriGreen

/**
 * Overlay de carga mejorado con animación de pulso.
 * Un ícono central pulsa suavemente mientras muestra el mensaje de carga.
 */
@Composable
fun LoadingOverlay(message: String = "Buscando producto...") {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingPulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue  = 1.1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier          = Modifier
            .fillMaxSize()
            .background(NutriBackground.copy(alpha = 0.88f)),
        contentAlignment  = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Ícono pulsante
            Text(
                text     = "🔍",
                fontSize = 56.sp,
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
            )

            // Texto
            Text(
                text       = message,
                style      = MaterialTheme.typography.bodyLarge,
                color      = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )

            // Indicador de progreso sutil
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(3.dp)
                    .background(
                        color = NutriGreen.copy(alpha = alpha),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
