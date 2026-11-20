package com.electromechanics.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel
import com.electromechanics.app.utils.FotoUtils
import com.electromechanics.app.utils.fechaCorta

@Composable
fun ActividadesScreen(
    usuario: Empleado,
    vm: OperacionesViewModel,
    onNavigateHome: () -> Unit
) {
    val actividades by vm.actividades.observeAsState(emptyList())
    val ordenes by vm.ordenes.observeAsState(emptyList())
    val empleados by vm.empleados.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        vm.cargarActividades()
        vm.cargarOrdenes()
        vm.cargarEmpleados()
    }

    Scaffold(
        topBar = { Header(title = "BITÁCORA DE ACTIVIDADES", userName = usuario.nombreCompleto) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(16.dp)
        ) {
            Text("Reportes de los técnicos", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (actividades.isEmpty()) {
                Text("No hay actividades registradas.", color = IndustrialTextWhite, fontSize = 14.sp)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(actividades.sortedByDescending { it.fechaHoraRegistro ?: "" }) { a ->
                        val tecnico = empleados.firstOrNull { it.idEmpleado == a.idTecnico }?.nombreCompleto ?: a.idTecnico
                        val orden = ordenes.firstOrNull { it.idOrden == a.idOrden }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Técnico: $tecnico", color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                                Text("Orden: ${orden?.let { "${it.cliente} - ${it.tarea}" } ?: a.idOrden}", color = IndustrialTextWhite, fontSize = 13.sp)
                                Text("Comentario: ${a.comentario ?: "(sin comentario)"}", color = IndustrialTextWhite, fontSize = 13.sp)
                                val foto = remember(a.evidenciaFotografica) { FotoUtils.desdeBase64(a.evidenciaFotografica) }
                                if (foto != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Image(
                                        bitmap = foto.asImageBitmap(),
                                        contentDescription = "Evidencia fotográfica",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxWidth().heightIn(max = 220.dp)
                                    )
                                } else if (a.evidenciaFotografica != null) {
                                    Text("Evidencia (no visible)", color = Color.Gray, fontSize = 11.sp)
                                }
                                Text(a.fechaHoraRegistro.fechaCorta(), color = Color.Gray, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
