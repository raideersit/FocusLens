package com.nutrilens.presentation.feature.scanner

import com.nutrilens.domain.model.Food

/** Estado de la pantalla del escáner. PATRÓN Observer con StateFlow. */
sealed class ScannerUiState {
    data object Idle    : ScannerUiState()
    data object Loading : ScannerUiState()
    data class Success(val food: Food) : ScannerUiState()
    data class Error(val message: String) : ScannerUiState()
}
