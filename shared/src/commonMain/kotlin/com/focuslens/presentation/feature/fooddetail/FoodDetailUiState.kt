package com.focuslens.presentation.feature.fooddetail

import com.focuslens.domain.model.Food
import com.focuslens.domain.model.NutritionFeedback

sealed class FoodDetailUiState {
    data object Loading : FoodDetailUiState()
    data class Success(
        val food: Food,
        val feedbacks: List<NutritionFeedback>
    ) : FoodDetailUiState()
    data class Error(val message: String) : FoodDetailUiState()
}
