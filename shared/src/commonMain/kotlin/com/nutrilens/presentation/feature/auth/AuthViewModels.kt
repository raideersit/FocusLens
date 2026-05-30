package com.nutrilens.presentation.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrilens.domain.usecase.user.LoginUseCase
import com.nutrilens.domain.usecase.user.RegisterUseCase
import com.nutrilens.presentation.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── Estado compartido de formularios ─────────────────────────────────────────

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String?     = null,
    val isSuccess: Boolean = false
)

// ── LoginViewModel ────────────────────────────────────────────────────────────

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUseCase(email, password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _uiEvent.emit(UiEvent.NavigateTo("scanner"))
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}

// ── RegisterViewModel ─────────────────────────────────────────────────────────

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            registerUseCase(name, email, password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _uiEvent.emit(UiEvent.NavigateTo("scanner"))
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}
