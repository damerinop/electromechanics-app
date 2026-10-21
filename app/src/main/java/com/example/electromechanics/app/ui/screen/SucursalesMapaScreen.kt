package com.electromechanics.app.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electromechanics.app.ui.components.Footer
import com.electromechanics.app.ui.components.Header
import com.electromechanics.app.ui.theme.*

data class SucursalModel(val nombre: String, val direccion: String, val lat: Double, val lng: Double)

@Composable
fun SucursalesMapaScreen(userName: String, onNavigateHome: () -> Unit) {
    val context = LocalContext.current
    val sucursales = listOf(
        SucursalModel("Planta Norte - Maipú", "Av. Central 450", -33.5123, -70.7561),
        SucursalModel("Bodega Central - Quilicura", "Los Industriales 1200", -33.3567, -70.7234),
        SucursalModel("Sucursal Sur - San Bernardo", "Camino a Lonquén 800", -33.5912, -70.7011)
    )

    Scaffold(
        topBar = { Header(title = "SUCURSALES DE CLIENTES", userName = userName) },
        bottomBar = { Footer(onNavigateHome = onNavigateHome, onLogout = null) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().background(IndustrialBlack).padding(innerPadding).padding(16.dp)) {
            Text("Instalaciones y Mapas de Clientes", color = IndustrialYellowAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sucursales) { suc ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = IndustrialCardGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = suc.nombre, color = IndustrialYellowAccent, fontWeight = FontWeight.Bold)
                            Text(text = suc.direccion, color = IndustrialTextWhite, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    // Intent para abrir la aplicación nativa de Google Maps
                                    val gmmIntentUri = Uri.parse("geo:${suc.lat},${suc.lng}?q=${Uri.encode(suc.nombre)}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    try {
                                        context.startActivity(mapIntent)
                                    } catch (e: Exception) {
                                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${suc.lat},${suc.lng}"))
                                        context.startActivity(browserIntent)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = IndustrialBluePrimary)
                            ) {
                                Text("Ver en mapa", color = IndustrialTextWhite, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}