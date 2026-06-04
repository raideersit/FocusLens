package com.focuslens.presentation.feature.fooddetail

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.focuslens.domain.model.Food
import com.focuslens.domain.model.NutritionFeedback
import com.focuslens.presentation.components.LoadingOverlay
import com.focuslens.presentation.components.NutrientBar
import com.focuslens.presentation.components.NutritionTrafficLight
import com.focuslens.presentation.components.NutriScoreBadge
import com.focuslens.presentation.theme.*
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    barcode: String,
    navController: NavController,
    viewModel: FoodDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(barcode) {
        viewModel.loadFood(barcode)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle nutricional") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = NutriBackground,
                    titleContentColor = NutriOnBackground,
                    navigationIconContentColor = NutriOnBackground
                )
            )
        },
        containerColor = NutriBackground
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is FoodDetailUiState.Loading -> LoadingOverlay("Cargando producto...")
                is FoodDetailUiState.Error   -> ErrorContent(state.message)
                is FoodDetailUiState.Success -> FoodDetailContent(state.food, state.feedbacks)
            }
        }
    }
}

@Composable
private fun FoodDetailContent(food: Food, feedbacks: List<NutritionFeedback>) {
    // ── Animaciones de entrada escalonadas ───────────────────────────────────
    var headerVisible    by remember { mutableStateOf(false) }
    var caloriesVisible  by remember { mutableStateOf(false) }
    var barsVisible      by remember { mutableStateOf(false) }
    var feedbackVisible  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
        delay(150)
        caloriesVisible = true
        delay(150)
        barsVisible = true
        delay(150)
        feedbackVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Header: Imagen + info básica ────────────────────────────────────
        AnimatedVisibility(
            visible = headerVisible,
            enter   = fadeIn(tween(500)) +
                      slideInVertically(tween(500, easing = EaseOutCubic)) { -30 }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment     = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NutriSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🥫", style = MaterialTheme.typography.displayLarge)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = food.name,
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = NutriOnBackground,
                        fontWeight = FontWeight.Bold
                    )
                    food.brand?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium, color = NutriSubtext)
                    }
                    food.quantity?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium, color = NutriSubtext)
                    }
                    Spacer(Modifier.height(8.dp))
                    food.nutriScore?.let { NutriScoreBadge(it) }
                }
            }
        }

        // ── Calorías destacadas ─────────────────────────────────────────────
        AnimatedVisibility(
            visible = caloriesVisible,
            enter   = fadeIn(tween(500)) +
                      slideInVertically(tween(500, easing = EaseOutCubic)) { 40 }
        ) {
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NutriSurface)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MacroChip("Calorías",  "${food.nutrition.calories.toInt()}", "kcal", NutriGreen)
                    MacroChip("Proteínas", "${food.nutrition.proteins}",         "g",    AccentBlue)
                    MacroChip("Carbos",    "${food.nutrition.carbohydrates}",    "g",    AccentAmber)
                    MacroChip("Grasas",    "${food.nutrition.fats}",            "g",    AccentCoral)
                }
            }
        }

        // ── Barras nutricionales ────────────────────────────────────────────
        AnimatedVisibility(
            visible = barsVisible,
            enter   = fadeIn(tween(500)) +
                      slideInVertically(tween(500, easing = EaseOutCubic)) { 40 }
        ) {
            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NutriSurface)
            ) {
                Column(
                    modifier            = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Valores por 100g",
                        style      = MaterialTheme.typography.titleMedium,
                        color      = NutriOnBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                    with(food.nutrition) {
                        NutrientBar("Proteínas",       proteins,       30.0,  "g", AccentBlue)
                        NutrientBar("Carbohidratos",   carbohydrates,  100.0, "g", AccentAmber)
                        NutrientBar("Azúcares",        sugars,         50.0,  "g", AccentPurple)
                        NutrientBar("Grasas totales",  fats,           35.0,  "g", AccentCoral)
                        NutrientBar("Grasas saturadas", saturatedFats, 20.0,  "g", NutriScoreD)
                        NutrientBar("Fibra",           fiber,          10.0,  "g", NutriGreen)
                        NutrientBar("Sodio",           sodium,         2.0,   "g", NutriTeal)
                    }
                }
            }
        }

        // ── Semáforo nutricional (feedback personalizado) ────────────────────
        if (feedbacks.isNotEmpty()) {
            AnimatedVisibility(
                visible = feedbackVisible,
                enter   = fadeIn(tween(500)) +
                          slideInVertically(tween(500, easing = EaseOutCubic)) { 40 }
            ) {
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NutriSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Análisis según tus metas",
                            style      = MaterialTheme.typography.titleMedium,
                            color      = NutriOnBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(12.dp))
                        NutritionTrafficLight(feedbacks = feedbacks)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun MacroChip(label: String, value: String, unit: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style      = MaterialTheme.typography.titleLarge,
            color      = color,
            fontWeight = FontWeight.Bold
        )
        Text(unit,  style = MaterialTheme.typography.labelSmall, color = NutriSubtext)
        Text(label, style = MaterialTheme.typography.labelSmall, color = NutriSubtext)
    }
}

@Composable
private fun ErrorContent(message: String) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.85f)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("😕", style = MaterialTheme.typography.displayLarge)
                Spacer(Modifier.height(12.dp))
                Text(message, style = MaterialTheme.typography.bodyLarge, color = NutriSubtext)
            }
        }
    }
}
