package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.navigation.Routes
import com.electromechanics.app.ui.theme.*

@Composable
fun HomeScreen(
    usuario: Empleado,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { Header(title = "PANEL PRINCIPAL", userName = "${usuario.nombreCompleto} (${usuario.rol})") },
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
            if (usuario.esSupervisor) {
                Text("Panel del Supervisor", color = IndustrialYellowAccent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                MenuCard("Gestión de Técnicos", "Crear y listar técnicos", Icons.Default.Group) { onNavigate(Routes.TECNICOS) }
                MenuCard("Órdenes de Trabajo", "Crear, asignar y revisar estados", Icons.Default.Assignment) { onNavigate(Routes.ORDENES_SUP) }
                MenuCard("Bitácora de Actividades", "Reportes y evidencias de los técnicos", Icons.Default.Build) { onNavigate(Routes.ACTIVIDADES) }
                MenuCard("Auditoría de Jornadas", "Asistencia y GPS de todos los técnicos", Icons.Default.History) { onNavigate(Routes.HISTORIAL_JORNADAS) }
            } else {
                Text("Módulos de Terreno", color = IndustrialYellowAccent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                MenuCard("Control de Jornada", "Marcación de entrada y salida con GPS", Icons.Default.AccessTime) { onNavigate(Routes.CONTROL_JORNADA) }
                MenuCard("Mis Órdenes", "Hoja de ruta, estados y evidencia", Icons.Default.Assignment) { onNavigate(Routes.MIS_ORDENES) }
                MenuCard("Mis Jornadas", "Historial de mis marcaciones", Icons.Default.History) { onNavigate(Routes.HISTORIAL_JORNADAS) }
            }
            MenuCard("Mi Perfil", "Datos personales y rol", Icons.Default.Person) { onNavigate(Routes.PERFIL) }
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
