package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.data.local.entity.EmpleadoEntity
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*

@Composable
fun PerfilEmpleadoScreen(user: EmpleadoEntity?, onNavigateHome: () -> Unit) {
    Scaffold(
        topBar = { Header(title = "MI PERFIL TÉCNICO", userName = user?.nombreCompleto ?: "Técnico") },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(20.dp)
        ) {
            Text("Información del Operario", color = IndustrialYellowAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nombre: ${user?.nombreCompleto ?: "Operario Terreno"}", color = IndustrialTextWhite, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("RUT: ${user?.rut ?: "Sin registro"}", color = IndustrialTextWhite, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Correo: ${user?.correo ?: "contacto@electromechanics.cl"}", color = IndustrialTextWhite, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Especialidad: ${user?.especialidad ?: "Electromecánica General"}", color = IndustrialYellowAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}