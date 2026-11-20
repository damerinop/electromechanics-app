package com.electromechanics.app.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel
import com.electromechanics.app.utils.fechaCorta

@Composable
fun ControlJornadaScreen(
    usuario: Empleado,
    vm: OperacionesViewModel,
    onNavigateHome: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val miId = usuario.idEmpleado.orEmpty()
    val jornadas by vm.jornadas.observeAsState(emptyList())

    var lat by remember { mutableStateOf<Double?>(null) }
    var lng by remember { mutableStateOf<Double?>(null) }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener = remember {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                lat = location.latitude
                lng = location.longitude
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
    }

    fun iniciarGps() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0f, locationListener)
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                lat = it.latitude
                lng = it.longitude
            }
        } catch (e: SecurityException) {
        } catch (e: IllegalArgumentException) {
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted -> if (isGranted) iniciarGps() }

    LaunchedEffect(Unit) {
        vm.cargarJornadas()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            iniciarGps()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    DisposableEffect(Unit) {
        onDispose { locationManager.removeUpdates(locationListener) }
    }

    // Estado según la última marcación guardada en el servidor
    val ultima = jornadas
        .filter { it.idEmpleado == miId }
        .maxByOrNull { it.fechaHoraMarcacion ?: "" }
    val enJornada = ultima?.tipoMarcacion == "ENTRADA"

    val la = lat
    val lo = lng
    val tieneGps = la != null && lo != null

    Scaffold(
        topBar = { Header(title = "CONTROL DE JORNADA", userName = usuario.nombreCompleto) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = onLogout) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IndustrialBlack)
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Marcación con GPS en Terreno",
                color = IndustrialYellowAccent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (enJornada) "Estado: EN JORNADA (desde ${ultima?.fechaHoraMarcacion.fechaCorta()})" else "Estado: FUERA DE TURNO",
                        color = IndustrialTextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (tieneGps) "GPS: Lat %.5f, Lng %.5f".format(la, lo) else "Buscando señal GPS...",
                        color = IndustrialYellowAccent,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (la != null && lo != null) vm.marcarJornada(miId, "ENTRADA", la, lo) },
                enabled = tieneGps && !enJornada,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = IndustrialYellowAccent, contentColor = IndustrialBlack)
            ) {
                Text("MARCAR ENTRADA CON GPS", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { if (la != null && lo != null) vm.marcarJornada(miId, "SALIDA", la, lo) },
                enabled = tieneGps && enJornada,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = IndustrialErrorRed, contentColor = IndustrialTextWhite)
            ) {
                Text("MARCAR SALIDA CON GPS", fontWeight = FontWeight.Bold)
            }
        }
    }
}
