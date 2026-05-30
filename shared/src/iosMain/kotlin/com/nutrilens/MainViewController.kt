package com.nutrilens

import androidx.compose.ui.window.ComposeUIViewController
import com.nutrilens.di.initKoin
import com.nutrilens.di.iosModule

/**
 * Punto de entrada para iOS.
 * Swift llama a MainKt.MainViewController() para obtener el UIViewController
 * que renderiza toda la UI de Compose Multiplatform.
 */
fun MainViewController() = ComposeUIViewController {
    NutriLensApp()
}

/**
 * Inicializa Koin desde Swift.
 * Debe llamarse en la función init() del @main App de Swift.
 */
fun initKoinIos() {
    initKoin(platformModule = iosModule)
}
