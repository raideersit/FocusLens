package com.nutrilens.presentation.feature.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nutrilens.domain.model.GoalType
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import com.nutrilens.presentation.util.UiEvent
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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
            Card(
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NutriSurface)
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier         = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(NutriGreen),
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

            // ── Selector de objetivo ─────────────────────────────────────────
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
                                colors   = RadioButtonDefaults.colors(selectedColor = NutriGreen)
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

            // ── Metas diarias ────────────────────────────────────────────────
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

            // ── Botón guardar ────────────────────────────────────────────────
            Button(
                onClick  = { viewModel.saveGoals() },
                enabled  = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = NutriGreen)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color    = NutriBackground,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Guardar metas", fontWeight = FontWeight.SemiBold, color = NutriBackground)
                }
            }

            // ── Botón cerrar sesión ──────────────────────────────────────────
            OutlinedButton(
                onClick  = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = TrafficRed),
                border   = BorderStroke(1.dp, TrafficRed)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
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
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = NutriGreen,
            unfocusedBorderColor = NutriSurface2,
            focusedLabelColor    = NutriGreen,
            focusedTextColor     = NutriOnBackground,
            unfocusedTextColor   = NutriOnSurface
        )
    )
}
