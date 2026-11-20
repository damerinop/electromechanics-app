package com.electromechanics.app.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.electromechanics.app.ui.components.camposColors
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val error by viewModel.error.observeAsState()
    val cargando by viewModel.cargando.observeAsState(false)
    var credencial by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

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
                value = credencial,
                onValueChange = { credencial = it },
                label = { Text("RUT / Correo Corporativo") },
                colors = camposColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                colors = camposColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = IndustrialErrorRed)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.login(credencial, clave) {
                        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IndustrialYellowAccent,
                    contentColor = IndustrialBlack
                )
            ) {
                Text(if (cargando) "CONECTANDO..." else "INGRESAR", fontWeight = FontWeight.Bold)
            }
        }
    }
}
