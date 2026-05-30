package com.nutrilens.presentation.feature.history

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nutrilens.presentation.components.FoodCard
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Historial", fontWeight = FontWeight.Bold, color = NutriOnBackground)
                        Text(
                            "${uiState.scans.size} productos escaneados",
                            style = MaterialTheme.typography.bodySmall,
                            color = NutriSubtext
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = NutriOnBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NutriBackground)
            )
        },
        containerColor = NutriBackground
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.scans.isEmpty() -> EmptyHistoryPlaceholder()
                else -> {
                    LazyColumn(
                        modifier            = Modifier.fillMaxSize(),
                        contentPadding      = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.scans,
                            key   = { it.id }
                        ) { scan ->
                            // Swipe to delete con DismissState
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        viewModel.deleteScan(scan.id)
                                        true
                                    } else false
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    val bgColor by animateColorAsState(
                                        targetValue = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                                            TrafficRed else Color.Transparent,
                                        label = "swipeBg"
                                    )
                                    Box(
                                        modifier          = Modifier
                                            .fillMaxSize()
                                            .background(bgColor, shape = RoundedCornerShape(16.dp))
                                            .padding(end = 20.dp),
                                        contentAlignment  = Alignment.CenterEnd
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                                            tint = Color.White)
                                    }
                                }
                            ) {
                                FoodCard(
                                    scan     = scan,
                                    onClick  = {
                                        navController.navigate(Screen.FoodDetail.createRoute(scan.food.barcode))
                                    },
                                    onDelete = { viewModel.deleteScan(scan.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryPlaceholder() {
    Column(
        modifier            = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📭", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(16.dp))
        Text("Sin historial todavía", style = MaterialTheme.typography.headlineMedium,
            color = NutriOnBackground, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Text("Escanea un alimento para empezar", style = MaterialTheme.typography.bodyMedium,
            color = NutriSubtext)
    }
}
