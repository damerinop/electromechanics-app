package com.electromechanics.app.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.electromechanics.app.data.database.AppDatabase
import com.electromechanics.app.data.local.entity.RegistroJornadaEntity
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ControlJornadaScreen(userName: String, userId: String = "default_user", onNavigateHome: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var estadoJornada by remember { mutableStateOf("Fuera de Turno") }
    var latActual by remember { mutableStateOf(-33.4489) }
    var lngActual by remember { mutableStateOf(-70.6693) }
    var infoGPS by remember { mutableStateOf("Buscando señal GPS...") }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latActual = location.latitude
            lngActual = location.longitude
            infoGPS = "Lat: %.4f, Lng: %.4f".format(latActual, lngActual)
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
            } catch (e: SecurityException) { }
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
                val lastLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastLoc != null) {
                    latActual = lastLoc.latitude
                    lngActual = lastLoc.longitude
                    infoGPS = "Lat: %.4f, Lng: %.4f".format(latActual, lngActual)
                }
            } catch (e: SecurityException) { }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        topBar = { Header(title = "CONTROL DE JORNADA", userName = userName) },
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
                    Text("Estado: $estadoJornada", color = IndustrialTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Coordenadas GPS: $infoGPS", color = IndustrialYellowAccent, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val time = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(Date())
                    estadoJornada = "EN JORNADA ($time)"
                    coroutineScope.launch(Dispatchers.IO) {
                        val db = AppDatabase.getDatabase(context)
                        db.registroJornadaDao().registrarMarcacion(
                            RegistroJornadaEntity(
                                idEmpleado = userId,
                                tipoMarcacion = "Entrada",
                                fechaHoraMarcacion = time,
                                latitudMarcacion = latActual,
                                longitudMarcacion = lngActual
                            )
                        )
                    }
                    Toast.makeText(context, "Entrada guardada en Auditoría (GPS)", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = IndustrialYellowAccent, contentColor = IndustrialBlack)
            ) {
                Text("MARCAR ENTRADA CON GPS", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val time = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(Date())
                    estadoJornada = "FINALIZADO ($time)"
                    coroutineScope.launch(Dispatchers.IO) {
                        val db = AppDatabase.getDatabase(context)
                        db.registroJornadaDao().registrarMarcacion(
                            RegistroJornadaEntity(
                                idEmpleado = userId,
                                tipoMarcacion = "Salida",
                                fechaHoraMarcacion = time,
                                latitudMarcacion = latActual,
                                longitudMarcacion = lngActual
                            )
                        )
                    }
                    Toast.makeText(context, "Salida guardada en Auditoría (GPS)", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = IndustrialErrorRed, contentColor = IndustrialTextWhite)
            ) {
                Text("MARCAR SALIDA CON GPS", fontWeight = FontWeight.Bold)
            }
        }
    }
}