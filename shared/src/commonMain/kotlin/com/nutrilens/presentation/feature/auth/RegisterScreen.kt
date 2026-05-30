package com.nutrilens.presentation.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
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
import com.nutrilens.presentation.navigation.Screen
import com.nutrilens.presentation.theme.*
import com.nutrilens.presentation.util.UiEvent
import org.koin.compose.viewmodel.koinViewModel

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

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.NavigateTo) {
                navController.navigate(event.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta") },
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
            Spacer(Modifier.height(16.dp))

            Text("Bienvenido a NutriLens 🥗",
                style = MaterialTheme.typography.headlineMedium,
                color = NutriOnBackground, fontWeight = FontWeight.Bold)
            Text("Crea tu cuenta y comienza a comer con inteligencia",
                style = MaterialTheme.typography.bodyMedium, color = NutriSubtext)

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value         = name,
                onValueChange = { name = it },
                label         = { Text("Nombre completo") },
                leadingIcon   = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(14.dp),
                colors        = authFieldColors()
            )

            OutlinedTextField(
                value           = email,
                onValueChange   = { email = it },
                label           = { Text("Email") },
                leadingIcon     = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier        = Modifier.fillMaxWidth(),
                shape           = RoundedCornerShape(14.dp),
                colors          = authFieldColors()
            )

            OutlinedTextField(
                value                = password,
                onValueChange        = { password = it },
                label                = { Text("Contraseña (mín. 6 caracteres)") },
                leadingIcon          = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier             = Modifier.fillMaxWidth(),
                shape                = RoundedCornerShape(14.dp),
                colors               = authFieldColors()
            )

            AnimatedVisibility(uiState.error != null) {
                Text(uiState.error ?: "", color = TrafficRed,
                    style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick  = { viewModel.register(name, email, password) },
                enabled  = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = NutriGreen)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = NutriBackground, modifier = Modifier.size(22.dp))
                } else {
                    Text("Registrarme", fontWeight = FontWeight.SemiBold, color = NutriBackground)
                }
            }
        }
    }
}
