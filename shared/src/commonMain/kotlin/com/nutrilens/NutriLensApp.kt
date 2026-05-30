package com.nutrilens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.nutrilens.presentation.navigation.NutriLensNavGraph
import com.nutrilens.presentation.theme.NutriLensTheme

/**
 * Punto de entrada compartido de la UI de NutriLens.
 * Usado tanto por Android (MainActivity) como por iOS (MainViewController).
 */
@Composable
fun NutriLensApp() {
    NutriLensTheme {
        val navController = rememberNavController()
        NutriLensNavGraph(navController = navController)
    }
}
