package com.nutrilens.presentation.feature.scanner

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nutrilens.presentation.components.LoadingOverlay
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import com.nutrilens.presentation.util.UiEvent
import org.koin.compose.viewmodel.koinViewModel

/**
 * Pantalla principal del escáner de códigos de barras.
 * Usa PlatformCameraScanner (expect/actual) para la vista de cámara.
 * PATRÓN Observer: observa uiState y uiEvent del ViewModel.
 */
@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ScannerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // ── Animación pulsante para el marco de escaneo ──────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "scanPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = 1.03f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // ── Observar eventos únicos ──────────────────────────────────────────────
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.NavigateTo  -> navController.navigate(event.route)
                is UiEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    // ── Navegar al detalle cuando se escanea con éxito ───────────────────────
    LaunchedEffect(uiState) {
        if (uiState is ScannerUiState.Success) {
            val food = (uiState as ScannerUiState.Success).food
            navController.navigate(Screen.FoodDetail.createRoute(food.barcode))
            viewModel.resetScanner()
        }
    }

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        containerColor = NutriBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Vista de cámara multiplataforma ──────────────────────────────
            PlatformCameraScanner(
                onBarcodeDetected = viewModel::onBarcodeDetected,
                modifier          = Modifier.fillMaxSize()
            )

            // ── Header overlay con gradiente ─────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                NutriBackground.copy(alpha = 0.85f),
                                NutriBackground.copy(alpha = 0.4f),
                                NutriBackground.copy(alpha = 0f)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "FocusLens",
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = NutriGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { navController.navigate(Screen.History.route) }) {
                            Icon(Icons.Default.History, contentDescription = "Historial", tint = NutriOnBackground)
                        }
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = NutriOnBackground)
                        }
                    }
                }
            }

            // ── Marco de escaneo pulsante en el centro ───────────────────────
            Box(
                modifier         = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size((240 * pulseScale).dp)
                        .border(
                            width = 3.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    NutriGreen.copy(alpha = pulseAlpha),
                                    NutriTeal.copy(alpha = pulseAlpha)
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                )
                Text(
                    text     = "Apunta al código de barras",
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = NutriOnBackground,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NutriBackground.copy(alpha = 0.8f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            // ── Loading overlay encima de todo ────────────────────────────────
            AnimatedVisibility(
                visible = uiState is ScannerUiState.Loading,
                enter   = fadeIn(tween(300)),
                exit    = fadeOut(tween(300))
            ) {
                LoadingOverlay()
            }
        }
    }
}
