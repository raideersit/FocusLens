package com.focuslens.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.focuslens.presentation.feature.auth.LoginScreen
import com.focuslens.presentation.feature.auth.RegisterScreen
import com.focuslens.presentation.feature.fooddetail.FoodDetailScreen
import com.focuslens.presentation.feature.history.HistoryScreen
import com.focuslens.presentation.feature.profile.ProfileScreen
import com.focuslens.presentation.feature.scanner.ScannerScreen

private const val TRANSITION_DURATION = 400

private fun enterAnim(): EnterTransition =
    fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) +
    slideIn(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) { fullSize ->
        IntOffset(fullSize.width / 4, 0)
    }

private fun exitAnim(): ExitTransition =
    fadeOut(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) +
    slideOut(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) { fullSize ->
        IntOffset(-fullSize.width / 4, 0)
    }

private fun popEnterAnim(): EnterTransition =
    fadeIn(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) +
    slideIn(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) { fullSize ->
        IntOffset(-fullSize.width / 4, 0)
    }

private fun popExitAnim(): ExitTransition =
    fadeOut(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) +
    slideOut(animationSpec = tween(TRANSITION_DURATION, easing = EaseInOutCubic)) { fullSize ->
        IntOffset(fullSize.width / 4, 0)
    }

/**
 * Grafo de navegación principal de FocusLens.
 * Single source of truth de todas las rutas.
 * PATRÓN Facade: expone una API simple para toda la navegación.
 * Transiciones suaves con slide + fade para una experiencia premium.
 */
@Composable
fun FocusLensNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Login.route,
        enterTransition  = { enterAnim() },
        exitTransition   = { exitAnim() },
        popEnterTransition = { popEnterAnim() },
        popExitTransition  = { popExitAnim() }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Screen.Scanner.route) {
            ScannerScreen(navController = navController)
        }

        composable(Screen.History.route) {
            HistoryScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(
            route     = Screen.FoodDetail.route,
            arguments = listOf(
                navArgument("barcode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            FoodDetailScreen(
                barcode       = backStackEntry.arguments?.getString("barcode") ?: "",
                navController = navController
            )
        }
    }
}
