package com.nutrilens.presentation.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import com.nutrilens.presentation.util.UiEvent
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ── Animaciones de entrada escalonadas ─────────────────────────────────
    var logoVisible   by remember { mutableStateOf(false) }
    var fieldsVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        logoVisible = true
        delay(150)
        fieldsVisible = true
        delay(150)
        buttonVisible = true
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.NavigateTo) {
                navController.navigate(event.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }

    // Gradiente sutil de fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        NutriBackground,
                        NutriSurface.copy(alpha = 0.5f),
                        NutriBackground
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1500f, 2000f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {
            // ── Logo / Título animado ──────────────────────────────────────
            AnimatedVisibility(
                visible = logoVisible,
                enter   = fadeIn(tween(600)) + slideInVertically(tween(600)) { -60 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔍", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text       = "FocusLens",
                        style      = MaterialTheme.typography.displayLarge,
                        color      = NutriGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = "Escanea. Aprende. Come mejor.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NutriSubtext
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Campos animados ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = fieldsVisible,
                enter   = fadeIn(tween(500, delayMillis = 100)) +
                          slideInVertically(tween(500, delayMillis = 100)) { 40 }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = { Text("Email") },
                        leadingIcon   = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(14.dp),
                        colors        = authFieldColors(),
                        singleLine    = true
                    )

                    OutlinedTextField(
                        value               = password,
                        onValueChange       = { password = it },
                        label               = { Text("Contraseña") },
                        leadingIcon         = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions     = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier            = Modifier.fillMaxWidth(),
                        shape               = RoundedCornerShape(14.dp),
                        colors              = authFieldColors(),
                        singleLine          = true
                    )
                }
            }

            // ── Error animado ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = uiState.error != null,
                enter   = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit    = fadeOut(tween(200)) + shrinkVertically(tween(200))
            ) {
                Card(
                    shape  = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = TrafficRed.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text     = uiState.error ?: "",
                        color    = TrafficRed,
                        style    = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            // ── Botón principal animado ──────────────────────────────────────
            AnimatedVisibility(
                visible = buttonVisible,
                enter   = fadeIn(tween(500, delayMillis = 200)) +
                          slideInVertically(tween(500, delayMillis = 200)) { 40 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick  = { viewModel.login(email, password) },
                        enabled  = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = NutriGreen,
                            disabledContainerColor = NutriGreen.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color    = NutriBackground,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                "Iniciar sesión",
                                fontWeight = FontWeight.SemiBold,
                                color      = NutriBackground
                            )
                        }
                    }

                    // Ir a registro
                    TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                        Text("¿No tienes cuenta? ", color = NutriSubtext)
                        Text("Regístrate", color = NutriGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor     = NutriGreen,
    unfocusedBorderColor   = NutriSurface2,
    focusedLabelColor      = NutriGreen,
    unfocusedLabelColor    = NutriSubtext,
    cursorColor            = NutriGreen,
    focusedTextColor       = NutriOnBackground,
    unfocusedTextColor     = NutriOnSurface,
    focusedLeadingIconColor   = NutriGreen,
    unfocusedLeadingIconColor = NutriSubtext
)
