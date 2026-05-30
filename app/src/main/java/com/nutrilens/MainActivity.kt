package com.nutrilens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Única Activity del proyecto (patrón Single Activity con Navigation Compose).
 * Ahora usa la UI compartida de Compose Multiplatform desde el módulo :shared.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Llama al punto de entrada compartido de Compose Multiplatform
            NutriLensApp()
        }
    }
}
