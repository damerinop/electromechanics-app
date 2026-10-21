package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*

@Composable
fun HomeScreen(
    userName: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { Header(title = "PANEL PRINCIPAL", userName = userName) },
        bottomBar = { Footer(onNavigateHome = null, onLogout = onLogout) }
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
                text = "Módulos de Terreno",
                color = IndustrialYellowAccent,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            MenuCard("Control de Jornada", "Marcación de entrada y salida con GPS", Icons.Default.AccessTime) { onNavigate("CONTROL_JORNADA") }
            MenuCard("Hoja de Ruta / Órdenes", "Trabajos asignados y gestión técnica", Icons.Default.Assignment) { onNavigate("AGENDA_SERVICIOS") }
            MenuCard("Historial de Jornadas", "Auditoría de asistencia y geolocalización", Icons.Default.History) { onNavigate("HISTORIAL_JORNADAS") }
            MenuCard("Sucursales de Clientes", "Repositorio y mapas de instalaciones", Icons.Default.Business) { onNavigate("SUCURSALES_MAPA") }
            MenuCard("Notificaciones Operativas", "Avisos de urgencia y despachos", Icons.Default.Notifications) { onNavigate("NOTIFICACIONES") }
            MenuCard("Mi Perfil Técnico", "Datos personales, especialidad y rol", Icons.Default.Person) { onNavigate("PERFIL_EMPLEADO") }
        }
    }
}

@Composable
fun MenuCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = IndustrialYellowAccent,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = IndustrialYellowAccent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, color = IndustrialTextWhite, fontSize = 12.sp)
            }
        }
    }
}