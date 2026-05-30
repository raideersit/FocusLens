package com.nutrilens.presentation.feature.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrilens.domain.model.FoodScan
import com.nutrilens.domain.usecase.food.GetFoodByBarcodeUseCase
import com.nutrilens.domain.usecase.history.AddScanToHistoryUseCase
import com.nutrilens.presentation.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel del escáner.
 * PATRÓN Observer: expone StateFlow (estado) y SharedFlow (eventos únicos).
 * PRINCIPIO S (SRP): solo coordina el escaneo y la persistencia del resultado.
 * PRINCIPIO D (DIP): depende de los casos de uso (abstracciones), no de repos directamente.
 */
class ScannerViewModel(
    private val getFoodByBarcodeUseCase: GetFoodByBarcodeUseCase,
    private val addScanToHistoryUseCase: AddScanToHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Idle)
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    /** Evita disparar múltiples búsquedas por el mismo código mientras se procesa */
    private var lastScannedBarcode: String? = null

    fun onBarcodeDetected(barcode: String) {
        if (barcode == lastScannedBarcode) return
        if (_uiState.value is ScannerUiState.Loading) return
        lastScannedBarcode = barcode

        viewModelScope.launch {
            _uiState.value = ScannerUiState.Loading

            getFoodByBarcodeUseCase(barcode)
                .onSuccess { food ->
                    _uiState.value = ScannerUiState.Success(food)
                    // Guardamos en historial local automáticamente
                    addScanToHistoryUseCase(FoodScan(food = food))
                }
                .onFailure { error ->
                    val msg = error.message ?: "Error desconocido al buscar el producto"
                    _uiState.value = ScannerUiState.Error(msg)
                    _uiEvent.emit(UiEvent.ShowSnackbar(msg))
                }
        }
    }

    fun resetScanner() {
        lastScannedBarcode = null
        _uiState.value = ScannerUiState.Idle
    }
}
