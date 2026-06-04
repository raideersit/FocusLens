package com.nutrilens.presentation.feature.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nutrilens.domain.model.GoalType
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import com.nutrilens.presentation.util.UiEvent
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // ── Animaciones de entrada escalonadas ──────────────────────────────────
    var avatarVisible  by remember { mutableStateOf(false) }
    var goalVisible    by remember { mutableStateOf(false) }
    var targetsVisible by remember { mutableStateOf(false) }
    var actionsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            avatarVisible = true
            delay(120)
            goalVisible = true
            delay(120)
            targetsVisible = true
            delay(120)
            actionsVisible = true
        }
    }

    // Observar eventos
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.NavigateTo   -> navController.navigate(event.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text("Mi perfil", color = NutriOnBackground, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = NutriOnBackground)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint               = TrafficRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NutriBackground)
            )
        },
        containerColor = NutriBackground
    ) { padding ->

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NutriGreen)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Avatar y datos reales del usuario ────────────────────────────
            AnimatedVisibility(
                visible = avatarVisible,
                enter   = fadeIn(tween(500)) +
                          slideInVertically(tween(500, easing = EaseOutCubic)) { -40 }
            ) {
                Card(
                    shape  = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NutriSurface)
                ) {
                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Avatar con gradiente
                        Box(
                            modifier         = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(NutriGreen, NutriTeal)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = (uiState.user?.name?.firstOrNull()?.uppercase() ?: "U"),
                                fontSize   = 24.sp,
                                color      = NutriBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                text       = uiState.user?.name ?: "Usuario",
                                style      = MaterialTheme.typography.titleLarge,
                                color      = NutriOnBackground,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text  = uiState.user?.email ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NutriSubtext
                            )
                        }
                    }
                }
            }

            // ── Selector de objetivo ─────────────────────────────────────────
            AnimatedVisibility(
                visible = goalVisible,
                enter   = fadeIn(tween(500)) +
                          slideInVertically(tween(500, easing = EaseOutCubic)) { 40 }
            ) {
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NutriSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Objetivo nutricional",
                            style      = MaterialTheme.typography.titleMedium,
                            color      = NutriOnBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(12.dp))
                        GoalType.entries.forEach { goal ->
                            Row(
                                modifier          = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = uiState.selectedGoal == goal,
                                    onClick  = { viewModel.onGoalSelected(goal) },
                                    colors   = RadioButtonDefaults.colors(
                                        selectedColor   = NutriGreen,
                                        unselectedColor = NutriSubtext
                                    )
                                )
                                Text(
                                    text  = when (goal) {
                                        GoalType.LOSE_WEIGHT -> "🏃 Perder peso"
                                        GoalType.MAINTAIN    -> "⚖️ Mantener peso"
                                        GoalType.GAIN_MUSCLE -> "💪 Ganar músculo"
                                        GoalType.BALANCED    -> "🥗 Alimentación balanceada"
                                    },
                                    color = if (uiState.selectedGoal == goal) NutriGreen else NutriOnSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // ── Metas diarias ────────────────────────────────────────────────
            AnimatedVisibility(
                visible = targetsVisible,
                enter   = fadeIn(tween(500)) +
                          slideInVertically(tween(500, easing = EaseOutCubic)) { 40 }
            ) {
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NutriSurface)
                ) {
                    Column(
                        modifier            = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Metas diarias",
                            style      = MaterialTheme.typography.titleMedium,
                            color      = NutriOnBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                        GoalTextField("Calorías (kcal)", uiState.caloriesTarget, viewModel::onCaloriesChanged)
                        GoalTextField("Proteínas (g)",   uiState.proteinTarget,  viewModel::onProteinChanged)
                        GoalTextField("Carbohidratos (g)", uiState.carbsTarget,  viewModel::onCarbsChanged)
                        GoalTextField("Grasas (g)",      uiState.fatTarget,      viewModel::onFatChanged)
                    }
                }
            }

            // ── Botones animados ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = actionsVisible,
                enter   = fadeIn(tween(500)) +
                          slideInVertically(tween(500, easing = EaseOutCubic)) { 40 }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Botón guardar
                    Button(
                        onClick  = { viewModel.saveGoals() },
                        enabled  = !uiState.isSaving,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = NutriGreen,
                            disabledContainerColor = NutriGreen.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                color    = NutriBackground,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text("Guardar metas", fontWeight = FontWeight.SemiBold, color = NutriBackground)
                        }
                    }

                    // Botón cerrar sesión
                    OutlinedButton(
                        onClick  = { viewModel.logout() },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = TrafficRed),
                        border   = BorderStroke(1.dp, TrafficRed.copy(alpha = 0.7f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label) },
        modifier      = Modifier.fillMaxWidth(),
        shape         = RoundedCornerShape(12.dp),
        singleLine    = true,
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor     = NutriGreen,
            unfocusedBorderColor   = NutriSurface2,
            focusedLabelColor      = NutriGreen,
            unfocusedLabelColor    = NutriSubtext,
            focusedTextColor       = NutriOnBackground,
            unfocusedTextColor     = NutriOnSurface
        )
    )
}
