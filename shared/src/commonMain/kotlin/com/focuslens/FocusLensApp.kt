package com.focuslens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.focuslens.presentation.navigation.FocusLensNavGraph
import com.focuslens.presentation.theme.FocusLensTheme

/**
 * Punto de entrada compartido de la UI de FocusLens.
 * Usado tanto por Android (MainActivity) como por iOS (MainViewController).
 */
@Composable
fun FocusLensApp() {
    FocusLensTheme {
        val navController = rememberNavController()
        FocusLensNavGraph(navController = navController)
    }
}
