package com.nutrilens.presentation.feature.history

import com.nutrilens.domain.model.FoodScan

data class HistoryUiState(
    val scans: List<FoodScan> = emptyList(),
    val isLoading: Boolean    = false,
    val error: String?        = null
)
