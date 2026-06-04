package com.focuslens.presentation.feature.fooddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focuslens.domain.model.UserGoal
import com.focuslens.domain.usecase.food.GetFoodByBarcodeUseCase
import com.focuslens.domain.usecase.nutrition.AnalyzeNutritionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodDetailViewModel(
    private val getFoodByBarcodeUseCase: GetFoodByBarcodeUseCase,
    private val analyzeNutritionUseCase: AnalyzeNutritionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FoodDetailUiState>(FoodDetailUiState.Loading)
    val uiState: StateFlow<FoodDetailUiState> = _uiState.asStateFlow()

    /** Carga el detalle del alimento y genera el análisis nutricional personalizado. */
    fun loadFood(barcode: String, userGoal: UserGoal = UserGoal()) {
        viewModelScope.launch {
            _uiState.value = FoodDetailUiState.Loading
            getFoodByBarcodeUseCase(barcode)
                .onSuccess { food ->
                    val feedbacks = analyzeNutritionUseCase(food, userGoal)
                    _uiState.value = FoodDetailUiState.Success(food, feedbacks)
                }
                .onFailure { error ->
                    _uiState.value = FoodDetailUiState.Error(
                        error.message ?: "No se pudo cargar la información del producto"
                    )
                }
        }
    }
}
