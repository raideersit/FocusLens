package com.focuslens.presentation.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.focuslens.presentation.navigation.Screen
import com.focuslens.presentation.theme.*
import com.focuslens.presentation.util.UiEvent
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private const val EMAIL_DUPLICATE_ERROR = "El email ya está registrado"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()
    var name     by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ── Animaciones de entrada ──────────────────────────────────────────────
    var headerVisible by remember { mutableStateOf(false) }
    var fieldsVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible = true
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

    // ── Detectar si el error es de email duplicado ───────────────────────────
    val isEmailDuplicate = uiState.error?.contains("email ya está registrado", ignoreCase = true) == true
            || uiState.error?.contains("email already", ignoreCase = true) == true

    // ── Animación de shake para campo de email con error ────────────────────
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(isEmailDuplicate) {
        if (isEmailDuplicate) {
            // Shake animation: oscila rápidamente 3 veces
            repeat(3) {
                shakeOffset.animateTo(
                    targetValue   = 12f,
                    animationSpec = tween(durationMillis = 50, easing = LinearEasing)
                )
                shakeOffset.animateTo(
                    targetValue   = -12f,
                    animationSpec = tween(durationMillis = 50, easing = LinearEasing)
                )
            }
            shakeOffset.animateTo(0f, animationSpec = tween(50))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = NutriOnBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = NutriBackground,
                    titleContentColor = NutriOnBackground
                )
            )
        },
        containerColor = NutriBackground
    ) { padding ->
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Header animado ──────────────────────────────────────────────
            AnimatedVisibility(
                visible = headerVisible,
                enter   = fadeIn(tween(600)) + slideInVertically(tween(600)) { -40 }
            ) {
                Column {
                    Text(
                        "Bienvenido a FocusLens 🔍",
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = NutriOnBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Crea tu cuenta y comienza a comer con inteligencia",
                        style = MaterialTheme.typography.bodyMedium,
                        color = NutriSubtext
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── Campos animados ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = fieldsVisible,
                enter   = fadeIn(tween(500, delayMillis = 100)) +
                          slideInVertically(tween(500, delayMillis = 100)) { 40 }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Nombre
                    OutlinedTextField(
                        value         = name,
                        onValueChange = { name = it },
                        label         = { Text("Nombre completo") },
                        leadingIcon   = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(14.dp),
                        colors        = authFieldColors(),
                        singleLine    = true
                    )

                    // Email — con indicación visual de error de duplicado
                    OutlinedTextField(
                        value           = email,
                        onValueChange   = { email = it },
                        label           = { Text("Email") },
                        leadingIcon     = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = if (isEmailDuplicate) TrafficRed else LocalContentColor.current
                            )
                        },
                        trailingIcon = if (isEmailDuplicate) {
                            {
                                Icon(
                                    Icons.Default.ErrorOutline,
                                    contentDescription = "Email ya registrado",
                                    tint = TrafficRed
                                )
                            }
                        } else null,
                        isError         = isEmailDuplicate,
                        supportingText = if (isEmailDuplicate) {
                            {
                                Text(
                                    "Este correo ya tiene una cuenta asociada. Intenta iniciar sesión.",
                                    color = TrafficRed,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier        = Modifier
                            .fillMaxWidth()
                            .offset(x = shakeOffset.value.dp),
                        shape           = RoundedCornerShape(14.dp),
                        colors          = if (isEmailDuplicate) {
                            OutlinedTextFieldDefaults.colors(
                                focusedBorderColor     = TrafficRed,
                                unfocusedBorderColor   = TrafficRed.copy(alpha = 0.7f),
                                focusedLabelColor      = TrafficRed,
                                unfocusedLabelColor    = TrafficRed.copy(alpha = 0.7f),
                                cursorColor            = TrafficRed,
                                focusedTextColor       = NutriOnBackground,
                                unfocusedTextColor     = NutriOnSurface
                            )
                        } else authFieldColors(),
                        singleLine      = true
                    )

                    // Contraseña
                    OutlinedTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = { Text("Contraseña (mín. 6 caracteres)") },
                        leadingIcon          = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier             = Modifier.fillMaxWidth(),
                        shape                = RoundedCornerShape(14.dp),
                        colors               = authFieldColors(),
                        singleLine           = true
                    )
                }
            }

            // ── Error general (no email duplicado) ──────────────────────────
            AnimatedVisibility(
                visible = uiState.error != null && !isEmailDuplicate,
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

            Spacer(Modifier.height(4.dp))

            // ── Botón animado ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = buttonVisible,
                enter   = fadeIn(tween(500, delayMillis = 200)) +
                          slideInVertically(tween(500, delayMillis = 200)) { 40 }
            ) {
                Button(
                    onClick  = { viewModel.register(name, email, password) },
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
                            "Registrarme",
                            fontWeight = FontWeight.SemiBold,
                            color      = NutriBackground
                        )
                    }
                }
            }
        }
    }
}
