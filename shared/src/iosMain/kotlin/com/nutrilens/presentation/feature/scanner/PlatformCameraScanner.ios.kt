package com.nutrilens.presentation.feature.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nutrilens.presentation.theme.NutriBackground
import com.nutrilens.presentation.theme.NutriOnBackground

/**
 * Implementación iOS del escáner de barras.
 * 
 * TODO: Implementar con AVCaptureSession + AVCaptureMetadataOutput
 * usando UIViewControllerRepresentable cuando se compile en una Mac con Xcode.
 * 
 * Esta es una implementación placeholder que se debe completar en una Mac.
 * La implementación real usará:
 * - AVCaptureSession para captura de video
 * - AVCaptureMetadataOutput con tipos: .ean13, .ean8, .upce
 * - UIViewControllerRepresentable para integrar con Compose
 */
@Composable
actual fun PlatformCameraScanner(
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier
) {
    // Placeholder — la implementación real con AVFoundation
    // requiere compilar en una Mac con Xcode.
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "📷 Escáner iOS\n(Requiere implementación con AVFoundation)",
            style = MaterialTheme.typography.bodyLarge,
            color = NutriOnBackground
        )
    }
}
