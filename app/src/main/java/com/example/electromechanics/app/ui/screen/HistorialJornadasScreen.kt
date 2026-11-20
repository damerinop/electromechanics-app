package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel
import com.electromechanics.app.utils.fechaCorta

@Composable
fun HistorialJornadasScreen(
    usuario: Empleado,
    vm: OperacionesViewModel,
    onNavigateHome: () -> Unit
) {
    val jornadas by vm.jornadas.observeAsState(emptyList())
    val empleados by vm.empleados.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        vm.cargarJornadas()
        if (usuario.esSupervisor) vm.cargarEmpleados()
    }

    val lista = jornadas
        .filter { usuario.esSupervisor || it.idEmpleado == usuario.idEmpleado }
        .sortedByDescending { it.fechaHoraMarcacion ?: "" }

    Scaffold(
        topBar = {
            Header(
                title = if (usuario.esSupervisor) "AUDITORÍA DE JORNADAS" else "MIS JORNADAS",
                userName = usuario.nombreCompleto
            )
        },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(16.dp)
        ) {
            Text("Registros de Asistencia y GPS", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (lista.isEmpty()) {
                Text("No hay marcaciones registradas.", color = IndustrialTextWhite, fontSize = 14.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(lista) { item ->
                        val nombre = empleados.firstOrNull { it.idEmpleado == item.idEmpleado }?.nombreCompleto
                        Card(
                            colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                if (usuario.esSupervisor) {
                                    Text("Técnico: ${nombre ?: item.idEmpleado}", color = IndustrialTextWhite, fontWeight = FontWeight.Bold)
                                }
                                Text("Tipo: ${item.tipoMarcacion}", color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                                Text("Fecha/Hora: ${item.fechaHoraMarcacion.fechaCorta()}", color = IndustrialTextWhite, fontSize = 14.sp)
                                Text("GPS: Lat ${item.latitudMarcacion}, Lng ${item.longitudMarcacion}", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
