package com.focuslens.domain.repository

import com.focuslens.domain.model.Food

/**
 * PRINCIPIO I (ISP): Interfaz de solo búsqueda de alimentos por código de barras.
 * PRINCIPIO D (DIP): Los UseCases dependen de esta abstracción, nunca de FoodRepositoryImpl.
 * PATRÓN Repository: oculta si el dato viene de API o caché local.
 */
interface FoodRepository {
    /**
     * Busca un alimento por su código de barras en Open Food Facts.
     * @param barcode Código EAN/UPC del producto.
     * @return [Result] con [Food] si se encontró, o una excepción de dominio.
     */
    suspend fun getFoodByBarcode(barcode: String): Result<Food>
}
