package com.focuslens.presentation.feature.scanner

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

/**
 * Implementación Android del escáner de barras usando CameraX + ML Kit.
 * Mantiene la misma lógica que el ScannerScreen original.
 */
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
actual fun PlatformCameraScanner(
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier
) {
    val context        = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val barcodeScanner = remember { BarcodeScanning.getClient() }

    AndroidView(
        factory = { ctx ->
            val previewView          = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysis ->
                        analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )
                                barcodeScanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        barcodes.firstOrNull {
                                            it.format == Barcode.FORMAT_EAN_13 ||
                                            it.format == Barcode.FORMAT_EAN_8  ||
                                            it.format == Barcode.FORMAT_UPC_A  ||
                                            it.format == Barcode.FORMAT_UPC_E
                                        }?.rawValue?.let { barcode ->
                                            onBarcodeDetected(barcode)
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("PlatformCameraScanner", "Barcode scan failed", e)
                                    }
                                    .addOnCompleteListener { imageProxy.close() }
                            } else {
                                imageProxy.close()
                            }
                        }
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    Log.e("PlatformCameraScanner", "Camera binding failed", e)
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = modifier
    )
}
