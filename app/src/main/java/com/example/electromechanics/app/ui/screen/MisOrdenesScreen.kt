package com.electromechanics.app.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.components.camposColors
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel
import com.electromechanics.app.utils.FotoUtils
import java.io.File

@Composable
fun MisOrdenesScreen(
    usuario: Empleado,
    vm: OperacionesViewModel,
    onNavigateHome: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val miId = usuario.idEmpleado.orEmpty()
    val ordenes by vm.ordenes.observeAsState(emptyList())
    val misOrdenes = ordenes.filter { it.idTecnicoAsignado == miId }

    var seleccionadaId by remember { mutableStateOf<String?>(null) }
    var tempEstado by remember { mutableStateOf("") }
    var tempComentario by remember { mutableStateOf("") }
    var tempFoto by remember { mutableStateOf<Bitmap?>(null) }
    var fotoBase64 by remember { mutableStateOf<String?>(null) }
    var archivoFoto by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) { vm.cargarOrdenes() }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        val archivo = archivoFoto
        if (exito && archivo != null) {
            val bmp = FotoUtils.cargarEscalada(archivo)
            if (bmp != null) {
                tempFoto = bmp
                fotoBase64 = FotoUtils.aBase64(bmp)
                Toast.makeText(context, "Foto de evidencia adjuntada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No se pudo leer la foto", Toast.LENGTH_SHORT).show()
            }
        }
        archivo?.delete()
    }

    fun lanzarCamara() {
        val carpeta = File(context.cacheDir, "fotos").apply { mkdirs() }
        val archivo = File(carpeta, "evidencia_${System.currentTimeMillis()}.jpg")
        archivoFoto = archivo
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
        cameraLauncher.launch(uri)
    }

    val permisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { ok ->
        if (ok) lanzarCamara()
        else Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    fun abrirCamara() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            lanzarCamara()
        } else {
            permisoCamara.launch(Manifest.permission.CAMERA)
        }
    }

    val seleccionada = misOrdenes.firstOrNull { it.idOrden == seleccionadaId }

    Scaffold(
        topBar = { Header(title = "MIS ÓRDENES", userName = usuario.nombreCompleto) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = onLogout) }
    ) { innerPadding ->
        if (seleccionada == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(IndustrialBlack)
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text("Hoja de Ruta: seleccione una orden", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (misOrdenes.isEmpty()) {
                    Text("No tiene órdenes asignadas.", color = IndustrialTextWhite)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(misOrdenes) { orden ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    seleccionadaId = orden.idOrden
                                    tempEstado = orden.estado
                                    tempComentario = ""
                                    tempFoto = null
                                    fotoBase64 = null
                                }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Cliente: ${orden.cliente}", color = IndustrialTextWhite, fontWeight = FontWeight.Bold)
                                    Text("Labor: ${orden.tarea}", color = Color.LightGray, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        "Estado: [ ${orden.estado} ]",
                                        color = if (orden.estado == "Finalizado") Color.Green else IndustrialYellowAccent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
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
                Text("Gestión de Orden", color = IndustrialYellowAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cliente: ${seleccionada.cliente}", color = IndustrialTextWhite)
                Text("Tarea: ${seleccionada.tarea}", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Cambiar Estado:", color = IndustrialTextWhite, fontSize = 14.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Pendiente", "En Proceso", "Finalizado").forEach { est ->
                        Button(
                            onClick = { tempEstado = est },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (tempEstado == est) IndustrialYellowAccent else IndustrialCardGray,
                                contentColor = if (tempEstado == est) IndustrialBlack else IndustrialTextWhite
                            ),
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                        ) {
                            Text(est, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tempComentario,
                    onValueChange = { tempComentario = it.take(250) },
                    label = { Text("Actividad realizada / comentarios") },
                    colors = camposColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { abrirCamara() },
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialBluePrimary),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                ) {
                    Text("Tomar evidencia con cámara", color = IndustrialTextWhite)
                }

                tempFoto?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(bitmap = it.asImageBitmap(), contentDescription = "Foto", modifier = Modifier.size(140.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { seleccionadaId = null },
                        colors = ButtonDefaults.buttonColors(containerColor = IndustrialErrorRed),
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val idOrden = seleccionada.idOrden.orEmpty()
                            if (tempEstado != seleccionada.estado) vm.cambiarEstado(idOrden, tempEstado)
                            if (tempComentario.isNotBlank() || fotoBase64 != null) {
                                vm.registrarActividad(idOrden, miId, tempComentario, fotoBase64) {}
                            }
                            seleccionadaId = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndustrialYellowAccent, contentColor = IndustrialBlack),
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                    ) {
                        Text("Guardar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
