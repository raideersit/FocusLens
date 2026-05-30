package com.nutrilens.presentation.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrilens.domain.model.GoalType
import com.nutrilens.domain.model.User
import com.nutrilens.domain.model.UserGoal
import com.nutrilens.domain.usecase.user.GetCurrentUserUseCase
import com.nutrilens.domain.usecase.user.LogoutUseCase
import com.nutrilens.domain.usecase.user.UpdateUserGoalsUseCase
import com.nutrilens.presentation.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean  = true,
    val user: User?         = null,
    val isSaving: Boolean   = false,
    val saveSuccess: Boolean = false,
    val error: String?      = null,
    // Campos editables
    val selectedGoal: GoalType = GoalType.BALANCED,
    val caloriesTarget: String = "2000",
    val proteinTarget: String  = "50",
    val carbsTarget: String    = "250",
    val fatTarget: String      = "70"
)

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserGoalsUseCase: UpdateUserGoalsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    init { loadUser() }

    private fun loadUser() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            if (user != null) {
                _uiState.update {
                    it.copy(
                        isLoading       = false,
                        user            = user,
                        selectedGoal    = user.goal.type,
                        caloriesTarget  = user.goal.dailyCaloriesTarget.toString(),
                        proteinTarget   = user.goal.dailyProteinTarget.toString(),
                        carbsTarget     = user.goal.dailyCarbsTarget.toString(),
                        fatTarget       = user.goal.dailyFatTarget.toString()
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onGoalSelected(goal: GoalType) = _uiState.update { it.copy(selectedGoal = goal) }
    fun onCaloriesChanged(v: String)   = _uiState.update { it.copy(caloriesTarget = v) }
    fun onProteinChanged(v: String)    = _uiState.update { it.copy(proteinTarget = v) }
    fun onCarbsChanged(v: String)      = _uiState.update { it.copy(carbsTarget = v) }
    fun onFatChanged(v: String)        = _uiState.update { it.copy(fatTarget = v) }

    fun saveGoals() {
        val state  = _uiState.value
        val userId = state.user?.id ?: return

        val goals = UserGoal(
            type                = state.selectedGoal,
            dailyCaloriesTarget = state.caloriesTarget.toIntOrNull() ?: 2000,
            dailyProteinTarget  = state.proteinTarget.toIntOrNull()  ?: 50,
            dailyCarbsTarget    = state.carbsTarget.toIntOrNull()    ?: 250,
            dailyFatTarget      = state.fatTarget.toIntOrNull()      ?: 70
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveSuccess = false) }
            updateUserGoalsUseCase(userId, goals)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    _uiEvent.emit(UiEvent.ShowSnackbar("✅ Metas guardadas correctamente"))
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false) }
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error al guardar: ${e.message}"))
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiEvent.emit(UiEvent.NavigateTo("login"))
        }
    }
}
