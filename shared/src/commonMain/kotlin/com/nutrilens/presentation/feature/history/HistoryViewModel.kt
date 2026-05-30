package com.nutrilens.presentation.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrilens.domain.usecase.history.DeleteScanUseCase
import com.nutrilens.domain.usecase.history.GetScanHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getScanHistoryUseCase: GetScanHistoryUseCase,
    private val deleteScanUseCase: DeleteScanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
    }

    /**
     * Observa el historial como Flow reactivo.
     * La UI se actualiza automáticamente al agregar/eliminar escaneos.
     */
    private fun observeHistory() {
        getScanHistoryUseCase()
            .onEach { scans ->
                _uiState.update { it.copy(scans = scans, isLoading = false, error = null) }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun deleteScan(scanId: Long) {
        viewModelScope.launch {
            deleteScanUseCase(scanId)
        }
    }
}
