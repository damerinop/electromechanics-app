package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.data.database.AppDatabase
import com.electromechanics.app.data.local.entity.RegistroJornadaEntity
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HistorialJornadasScreen(userName: String, userId: String = "default_user", onNavigateHome: () -> Unit) {
    val context = LocalContext.current
    var listaJornadas by remember { mutableStateOf<List<RegistroJornadaEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            listaJornadas = db.registroJornadaDao().obtenerJornadasPorEmpleado(userId)
        }
    }

    Scaffold(
        topBar = { Header(title = "AUDITORÍA DE ASISTENCIA", userName = userName) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(16.dp)
        ) {
            Text("Registros de Asistencia y GPS Acumulados", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (listaJornadas.isEmpty()) {
                Text("No hay marcaciones registradas todavía.", color = IndustrialTextWhite, fontSize = 14.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listaJornadas) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Tipo: ${item.tipoMarcacion}", color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                                Text(text = "Fecha/Hora: ${item.fechaHoraMarcacion}", color = IndustrialTextWhite, fontSize = 14.sp)
                                Text(text = "GPS: Lat ${item.latitudMarcacion}, Lng ${item.longitudMarcacion}", color = androidx.compose.ui.graphics.Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}