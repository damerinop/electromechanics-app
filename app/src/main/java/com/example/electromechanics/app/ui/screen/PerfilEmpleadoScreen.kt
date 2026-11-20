package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*

@Composable
fun PerfilEmpleadoScreen(usuario: Empleado, onNavigateHome: () -> Unit) {
    Scaffold(
        topBar = { Header(title = "MI PERFIL", userName = usuario.nombreCompleto) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(20.dp)
        ) {
            Text("Información del Usuario", color = IndustrialYellowAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nombre: ${usuario.nombreCompleto}", color = IndustrialTextWhite, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("RUT: ${usuario.rutTrabajador}", color = IndustrialTextWhite, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Correo: ${usuario.correoCorporativo}", color = IndustrialTextWhite, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Especialidad: ${usuario.especialidadTecnica ?: "No registrada"}", color = IndustrialTextWhite, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Rol: ${usuario.rol}", color = IndustrialYellowAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
