package com.nutrilens.presentation.feature.auth

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import com.nutrilens.presentation.util.UiEvent
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

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.NavigateTo) {
                navController.navigate(event.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier         = Modifier.fillMaxSize().background(NutriBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {
            // Logo / Título
            Text("🥗", style = MaterialTheme.typography.displayLarge)
            Text(
                text       = "NutriLens",
                style      = MaterialTheme.typography.displayLarge,
                color      = NutriGreen,
                fontWeight = FontWeight.Bold
            )
            Text(
                text  = "Escanea. Aprende. Come mejor.",
                style = MaterialTheme.typography.bodyMedium,
                color = NutriSubtext
            )

            Spacer(Modifier.height(8.dp))

            // Campos
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                label         = { Text("Email") },
                leadingIcon   = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(14.dp),
                colors        = authFieldColors()
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
                colors              = authFieldColors()
            )

            // Error
            AnimatedVisibility(uiState.error != null) {
                Text(
                    text  = uiState.error ?: "",
                    color = TrafficRed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Botón principal
            Button(
                onClick  = { viewModel.login(email, password) },
                enabled  = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = NutriGreen)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = NutriBackground, modifier = Modifier.size(22.dp))
                } else {
                    Text("Iniciar sesión", fontWeight = FontWeight.SemiBold, color = NutriBackground)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = NutriGreen,
    unfocusedBorderColor = NutriSurface2,
    focusedLabelColor    = NutriGreen,
    cursorColor          = NutriGreen,
    focusedTextColor     = NutriOnBackground,
    unfocusedTextColor   = NutriOnSurface
)
