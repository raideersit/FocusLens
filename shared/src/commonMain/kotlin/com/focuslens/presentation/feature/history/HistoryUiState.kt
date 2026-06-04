package com.focuslens.presentation.feature.history

import com.focuslens.domain.model.FoodScan

data class HistoryUiState(
    val scans: List<FoodScan> = emptyList(),
    val isLoading: Boolean    = false,
    val error: String?        = null
)
