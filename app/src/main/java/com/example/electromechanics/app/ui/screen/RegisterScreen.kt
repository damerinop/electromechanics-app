package com.electromechanics.app.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = IndustrialTextWhite,
        unfocusedTextColor = IndustrialTextWhite,
        focusedBorderColor = IndustrialYellowAccent,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = IndustrialYellowAccent,
        unfocusedLabelColor = Color.Gray
    )

    Scaffold(
        topBar = { Header(title = "REGISTRO DE PERSONAL", userName = "Nuevo") },
        bottomBar = { Footer(onNavigateHome = null, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IndustrialBlack)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta de Operario",
                color = IndustrialYellowAccent,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.rut,
                onValueChange = { viewModel.onRutChanged(it) },
                label = { Text("RUT Trabajador (ej: 12345678-K)") },
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChanged(it) },
                label = { Text("Nombre Completo") },
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.correo,
                onValueChange = { viewModel.onCorreoChanged(it) },
                label = { Text("Correo Corporativo") },
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.especialidad,
                onValueChange = { viewModel.onEspecialidadChanged(it) },
                label = { Text("Especialidad Técnica (ej: Electromecánica)") },
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.clave,
                onValueChange = { viewModel.onClaveChanged(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.confirmarClave,
                onValueChange = { viewModel.onConfirmarClaveChanged(it) },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = state.error, color = IndustrialErrorRed)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.registrar(context) {
                        Toast.makeText(context, "Registro Exitoso. Inicie sesión.", Toast.LENGTH_SHORT).show()
                        onNavigateToLogin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IndustrialYellowAccent,
                    contentColor = IndustrialBlack
                )
            ) {
                Text("REGISTRAR EMPLEADO", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia Sesión aquí", color = IndustrialYellowAccent)
            }
        }
    }
}