package com.electromechanics.app.ui.screen

import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.R
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
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
        topBar = { Header(title = "INICIAR SESIÓN", userName = "Sin Sesión") },
        bottomBar = { Footer(onNavigateHome = null, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IndustrialBlack)
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logotipo institucional
            Image(
                painter = painterResource(id = R.drawable.logo_electromechanics),
                contentDescription = "Logo Electromechanics",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .shadow(elevation = 12.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .border(width = 3.dp, color = IndustrialYellowAccent, shape = CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ELECTROMECHANICS",
                color = IndustrialYellowAccent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sistema Móvil de Asistencia y Mantención",
                color = IndustrialTextWhite,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.rut,
                onValueChange = { viewModel.onRutChanged(it) },
                label = { Text("RUT / Correo Corporativo") },
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = state.clave,
                onValueChange = { viewModel.onClaveChanged(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = state.error, color = IndustrialErrorRed)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.login(context) {
                        // Vibración suave al ingresar
                        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        v.vibrate(300)
                        Toast.makeText(context, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IndustrialYellowAccent,
                    contentColor = IndustrialBlack
                )
            ) {
                Text("INGRESAR", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IndustrialBluePrimary,
                    contentColor = IndustrialTextWhite
                )
            ) {
                Text("REGISTRAR NUEVO OPERARIO")
            }
        }
    }
}