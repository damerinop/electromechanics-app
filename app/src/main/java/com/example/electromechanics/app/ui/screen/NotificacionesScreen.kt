package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*

data class NotificacionModel(val titulo: String, val detalle: String, val fecha: String)

@Composable
fun NotificacionesScreen(userName: String, onNavigateHome: () -> Unit) {
    val notificaciones = listOf(
        NotificacionModel("Urgencia en Planta Norte", "Se solicita mantención técnica preventiva en tablero principal.", "Hoy 08:30"),
        NotificacionModel("Actualización de Hoja de Ruta", "Se asignó nuevo servicio SRV-002.", "Ayer 16:15")
    )

    Scaffold(
        topBar = { Header(title = "NOTIFICACIONES", userName = userName) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(16.dp)) {
            Text("Avisos y Despachos Operativos", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notificaciones) { notif ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = notif.titulo, color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = notif.detalle, color = IndustrialTextWhite, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = notif.fecha, color = androidx.compose.ui.graphics.Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}