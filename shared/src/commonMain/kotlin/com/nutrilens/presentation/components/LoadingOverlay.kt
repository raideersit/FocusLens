package com.nutrilens.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutrilens.presentation.theme.NutriBackground

/**
 * Overlay de carga que bloquea la UI mientras se procesa el escaneo.
 */
@Composable
fun LoadingOverlay(message: String = "Buscando producto...") {
    Box(
        modifier          = Modifier
            .fillMaxSize()
            .background(NutriBackground.copy(alpha = 0.85f)),
        contentAlignment  = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color    = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp),
                strokeWidth = 4.dp
            )
            Text(
                text  = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
