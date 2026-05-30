package com.nutrilens.presentation.navigation

/**
 * Rutas de navegación definidas como sealed class.
 * Evita strings sueltos en el NavGraph.
 */
sealed class Screen(val route: String) {
    data object Login      : Screen("login")
    data object Register   : Screen("register")
    data object Scanner    : Screen("scanner")
    data object History    : Screen("history")
    data object Profile    : Screen("profile")

    data object FoodDetail : Screen("food_detail/{barcode}") {
        fun createRoute(barcode: String) = "food_detail/$barcode"
    }
}
