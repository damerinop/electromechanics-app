package com.electromechanics.app.ui.components

import android.widget.Toast
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.electromechanics.app.ui.theme.IndustrialTextWhite
import com.electromechanics.app.ui.theme.IndustrialYellowAccent
import com.electromechanics.app.ui.viewmodel.OperacionesViewModel

@Composable
fun camposColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = IndustrialTextWhite,
    unfocusedTextColor = IndustrialTextWhite,
    focusedBorderColor = IndustrialYellowAccent,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = IndustrialYellowAccent,
    unfocusedLabelColor = Color.Gray
)

@Composable
fun MensajeToast(vm: OperacionesViewModel) {
    val context = LocalContext.current
    val mensaje by vm.mensaje.observeAsState()
    LaunchedEffect(mensaje) {
        mensaje?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.limpiarMensaje()
        }
    }
}
