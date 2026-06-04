package com.focuslens

import androidx.compose.ui.window.ComposeUIViewController
import com.focuslens.di.initKoin
import com.focuslens.di.iosModule

/**
 * Punto de entrada para iOS.
 * Swift llama a MainKt.MainViewController() para obtener el UIViewController
 * que renderiza toda la UI de Compose Multiplatform.
 */
fun MainViewController() = ComposeUIViewController {
    FocusLensApp()
}

/**
 * Inicializa Koin desde Swift.
 * Debe llamarse en la función init() del @main App de Swift.
 */
fun initKoinIos() {
    initKoin(platformModule = iosModule)
}
