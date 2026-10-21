package com.electromechanics.app.ui.screen

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*

data class ServicioModel(
    val id: String,
    val cliente: String,
    val tarea: String,
    var estado: String,
    var descripcionTecnica: String = "",
    var fotoEvidencia: Bitmap? = null
)

@Composable
fun AgendaServiciosScreen(userName: String, onNavigateHome: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    var selectedServicio by remember { mutableStateOf<ServicioModel?>(null) }
    var tempEstado by remember { mutableStateOf("") }
    var tempDescripcion by remember { mutableStateOf("") }
    var tempFoto by remember { mutableStateOf<Bitmap?>(null) }

    // Lanzador simple de cámara nativa
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            tempFoto = bitmap
            Toast.makeText(context, "Foto de evidencia adjuntada", Toast.LENGTH_SHORT).show()
        }
    }

    val serviciosList = remember {
        mutableStateListOf(
            ServicioModel("SRV-001", "Industria Manufacturera S.A.", "Mantención Preventiva Tablero Eléctrico", "En Proceso"),
            ServicioModel("SRV-002", "Metalúrgica del Maipo", "Reparación Electromecánica de Motor 5HP", "Pendiente")
        )
    }

    Scaffold(
        topBar = { Header(title = "HOJA DE RUTA - ÓRDENES", userName = userName) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = onLogout) }
    ) { innerPadding ->
        if (selectedServicio == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IndustrialBlack)
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(text = "Seleccione una Orden para Gestionar", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(serviciosList) { srv ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                selectedServicio = srv
                                tempEstado = srv.estado
                                tempDescripcion = srv.descripcionTecnica
                                tempFoto = srv.fotoEvidencia
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = srv.id, color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Cliente: ${srv.cliente}", color = IndustrialTextWhite)
                                Text(text = "Labor: ${srv.tarea}", color = Color.LightGray, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Estado: [ ${srv.estado} ]", color = if (srv.estado == "Finalizado") Color.Green else IndustrialYellowAccent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IndustrialBlack)
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Text(text = "Gestión de Orden: ${selectedServicio!!.id}", color = IndustrialYellowAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Cliente: ${selectedServicio!!.cliente}", color = IndustrialTextWhite)
                Text(text = "Tarea: ${selectedServicio!!.tarea}", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Cambiar Estado:", color = IndustrialTextWhite, fontSize = 14.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Pendiente", "En Proceso", "Finalizado").forEach { est ->
                        Button(
                            onClick = { tempEstado = est },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (tempEstado == est) IndustrialYellowAccent else IndustrialCardGray,
                                contentColor = if (tempEstado == est) IndustrialBlack else IndustrialTextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(est, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tempDescripcion,
                    onValueChange = { tempDescripcion = it },
                    label = { Text("Comentarios / Descripción técnica") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = IndustrialTextWhite,
                        unfocusedTextColor = IndustrialTextWhite,
                        focusedBorderColor = IndustrialYellowAccent,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = IndustrialYellowAccent,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { cameraLauncher.launch(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialBluePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tomar Foto Evidencia (Opcional)", color = IndustrialTextWhite)
                }

                if (tempFoto != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        bitmap = tempFoto!!.asImageBitmap(),
                        contentDescription = "Foto",
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { selectedServicio = null },
                        colors = ButtonDefaults.buttonColors(containerColor = IndustrialErrorRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            selectedServicio!!.estado = tempEstado
                            selectedServicio!!.descripcionTecnica = tempDescripcion
                            selectedServicio!!.fotoEvidencia = tempFoto
                            Toast.makeText(context, "Orden actualizada con éxito", Toast.LENGTH_SHORT).show()
                            selectedServicio = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndustrialYellowAccent, contentColor = IndustrialBlack),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}