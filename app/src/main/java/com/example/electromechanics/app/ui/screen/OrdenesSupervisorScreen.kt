package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.components.camposColors
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel
import com.electromechanics.app.utils.fechaCorta

@Composable
fun OrdenesSupervisorScreen(
    usuario: Empleado,
    vm: OperacionesViewModel,
    onNavigateHome: () -> Unit,
    onLogout: () -> Unit
) {
    val ordenes by vm.ordenes.observeAsState(emptyList())
    val empleados by vm.empleados.observeAsState(emptyList())
    val tecnicos = empleados.filter { it.esTecnico }

    var cliente by remember { mutableStateOf("") }
    var tarea by remember { mutableStateOf("") }
    var tecnicoId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        vm.cargarOrdenes()
        vm.cargarEmpleados()
    }

    Scaffold(
        topBar = { Header(title = "ÓRDENES DE TRABAJO", userName = usuario.nombreCompleto) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = onLogout) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(IndustrialBlack)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Nueva Orden de Trabajo", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = cliente, onValueChange = { cliente = it.take(100) }, label = { Text("Cliente / Sucursal") },
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = tarea, onValueChange = { tarea = it.take(100) }, label = { Text("Tarea a realizar") },
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Asignar a:", color = IndustrialTextWhite, fontSize = 14.sp)
                if (tecnicos.isEmpty()) {
                    Text("No hay técnicos creados.", color = Color.Gray, fontSize = 13.sp)
                }
                tecnicos.forEach { t ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable { tecnicoId = t.idEmpleado },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tecnicoId == t.idEmpleado,
                            onClick = { tecnicoId = t.idEmpleado },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = IndustrialYellowAccent,
                                unselectedColor = Color.Gray
                            )
                        )
                        Text("${t.nombreCompleto} · ${t.especialidadTecnica ?: "General"}", color = IndustrialTextWhite, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        tecnicoId?.let { id ->
                            vm.crearOrden(usuario.idEmpleado.orEmpty(), id, cliente, tarea) {
                                cliente = ""; tarea = ""; tecnicoId = null
                            }
                        }
                    },
                    enabled = cliente.isNotBlank() && tarea.isNotBlank() && tecnicoId != null,
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialYellowAccent, contentColor = IndustrialBlack),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                ) {
                    Text("CREAR Y ASIGNAR", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Órdenes (${ordenes.size})", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(ordenes.sortedByDescending { it.fechaCreacion ?: "" }) { o ->
                val tecnico = empleados.firstOrNull { it.idEmpleado == o.idTecnicoAsignado }?.nombreCompleto ?: "Sin asignar"
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Cliente: ${o.cliente}", color = IndustrialTextWhite, fontWeight = FontWeight.Bold)
                        Text("Tarea: ${o.tarea}", color = Color.LightGray, fontSize = 13.sp)
                        Text("Técnico: $tecnico", color = IndustrialTextWhite, fontSize = 13.sp)
                        Text("Creada: ${o.fechaCreacion.fechaCorta()}", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "Estado: [ ${o.estado} ]",
                            color = if (o.estado == "Finalizado") Color.Green else IndustrialYellowAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
