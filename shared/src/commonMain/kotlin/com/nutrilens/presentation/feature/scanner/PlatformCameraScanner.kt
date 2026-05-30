package com.nutrilens.presentation.feature.scanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Composable multiplataforma para el escáner de códigos de barras.
 * 
 * - Android: usa CameraX + ML Kit Barcode Scanning
 * - iOS: usa AVCaptureSession + AVCaptureMetadataOutput
 *
 * Cada plataforma implementa su propia versión con expect/actual.
 */
@Composable
expect fun PlatformCameraScanner(
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
)
