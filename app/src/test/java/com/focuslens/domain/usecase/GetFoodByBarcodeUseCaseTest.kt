package com.focuslens.domain.usecase.food

import com.focuslens.domain.model.Food
import com.focuslens.domain.model.NutritionFacts
import com.focuslens.domain.repository.FoodRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios de GetFoodByBarcodeUseCase.
 * PRINCIPIO L (LSP): FoodRepository está mockeado — sustituye la implementación real.
 */
class GetFoodByBarcodeUseCaseTest {

    private lateinit var repository: FoodRepository
    private lateinit var useCase: GetFoodByBarcodeUseCase

    private val sampleFood = Food(
        barcode   = "7501055362005",
        name      = "Galletas María",
        brand     = "Gamesa",
        imageUrl  = null,
        nutrition = NutritionFacts(
            calories      = 440.0,
            proteins      = 7.5,
            carbohydrates = 72.0,
            fats          = 14.0,
            sodium        = 0.45,
            fiber         = 2.0,
            sugars        = 18.0
        )
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase    = GetFoodByBarcodeUseCase(repository)
    }

    @Test
    fun `when barcode is valid, returns food successfully`() = runTest {
        // Arrange
        coEvery { repository.getFoodByBarcode("7501055362005") } returns Result.success(sampleFood)

        // Act
        val result = useCase("7501055362005")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Galletas María", result.getOrNull()?.name)
    }

    @Test
    fun `when barcode is blank, returns failure without calling repository`() = runTest {
        // Act
        val result = useCase("   ")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `when repository fails, propagates the error`() = runTest {
        // Arrange
        coEvery { repository.getFoodByBarcode(any()) } returns
            Result.failure(Exception("Producto no encontrado"))

        // Act
        val result = useCase("9999999999999")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Producto no encontrado", result.exceptionOrNull()?.message)
    }
}
