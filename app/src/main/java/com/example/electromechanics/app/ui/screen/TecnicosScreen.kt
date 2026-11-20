package com.electromechanics.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.components.camposColors
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel
import com.electromechanics.app.utils.RutUtils

private const val CLAVE_MIN_LARGO = 10

@Composable
fun TecnicosScreen(
    usuario: Empleado,
    vm: OperacionesViewModel,
    onNavigateHome: () -> Unit,
    onLogout: () -> Unit
) {
    val empleados by vm.empleados.observeAsState(emptyList())
    val tecnicos = empleados.filter { it.esTecnico }

    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var errorForm by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { vm.cargarEmpleados() }

    Scaffold(
        topBar = { Header(title = "GESTIÓN DE TÉCNICOS", userName = usuario.nombreCompleto) },
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
                Text("Crear Técnico (rol: TECNICO)", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = rut, onValueChange = { rut = it.take(12) }, label = { Text("RUT") },
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = nombre, onValueChange = { nombre = it.take(100) }, label = { Text("Nombre completo") },
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = correo, onValueChange = { correo = it.take(100) }, label = { Text("Correo corporativo") },
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = especialidad, onValueChange = { especialidad = it.take(100) }, label = { Text("Especialidad técnica") },
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = clave, onValueChange = { clave = it.take(255) }, label = { Text("Contraseña (mínimo $CLAVE_MIN_LARGO caracteres)") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = camposColors(), modifier = Modifier.fillMaxWidth(), singleLine = true)

                errorForm?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(it, color = IndustrialErrorRed)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        when {
                            !RutUtils.esValido(rut) -> errorForm = "RUT inválido (dígito verificador incorrecto)"
                            nombre.isBlank() -> errorForm = "Ingrese el nombre"
                            !correo.contains("@") -> errorForm = "Correo inválido"
                            clave.length < CLAVE_MIN_LARGO -> errorForm = "La contraseña debe tener al menos $CLAVE_MIN_LARGO caracteres"
                            else -> {
                                errorForm = null
                                vm.crearTecnico(RutUtils.formatear(rut), nombre, correo, especialidad, clave) {
                                    rut = ""; nombre = ""; correo = ""; especialidad = ""; clave = ""
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialYellowAccent, contentColor = IndustrialBlack),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                ) {
                    Text("CREAR TÉCNICO", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Técnicos registrados (${tecnicos.size})", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(tecnicos) { t ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(t.nombreCompleto, color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                        Text("RUT: ${t.rutTrabajador}", color = IndustrialTextWhite, fontSize = 13.sp)
                        Text("Correo: ${t.correoCorporativo}", color = IndustrialTextWhite, fontSize = 13.sp)
                        Text("Especialidad: ${t.especialidadTecnica ?: "No registrada"}", color = IndustrialTextWhite, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
