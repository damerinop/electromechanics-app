package com.electromechanics.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electromechanics.app.data.database.AppDatabase
import com.electromechanics.app.data.local.entity.EmpleadoEntity
import com.electromechanics.app.utils.RutUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AuthUiState(
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val especialidad: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val error: String? = null,
    val usuarioLogueado: EmpleadoEntity? = null
)

class AuthViewModel : ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    fun onRutChanged(v: String) { uiState = uiState.copy(rut = v, error = null) }
    fun onNombreChanged(v: String) { uiState = uiState.copy(nombre = v, error = null) }
    fun onCorreoChanged(v: String) { uiState = uiState.copy(correo = v, error = null) }
    fun onEspecialidadChanged(v: String) { uiState = uiState.copy(especialidad = v, error = null) }
    fun onClaveChanged(v: String) { uiState = uiState.copy(clave = v, error = null) }
    fun onConfirmarClaveChanged(v: String) { uiState = uiState.copy(confirmarClave = v, error = null) }

    fun login(context: Context, onSuccess: () -> Unit) {
        if (!RutUtils.esValido(uiState.rut)) {
            uiState = uiState.copy(error = "RUT / Correo inválido")
            return
        }
        if (uiState.clave.isBlank()) {
            uiState = uiState.copy(error = "Ingrese su contraseña")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            val emp = db.empleadoDao().autenticar(uiState.rut, uiState.clave)
            withContext(Dispatchers.Main) {
                if (emp != null) {
                    uiState = uiState.copy(usuarioLogueado = emp, error = null)
                    onSuccess()
                } else {
                    // Usuario demo si no se ha registrado aún
                    val demoUser = EmpleadoEntity(uiState.rut, uiState.rut, "Operario Terreno", "demo@electromechanics.cl", "Mantenimiento", uiState.clave)
                    uiState = uiState.copy(usuarioLogueado = demoUser, error = null)
                    onSuccess()
                }
            }
        }
    }

    fun registrar(context: Context, onSuccess: () -> Unit) {
        if (!RutUtils.esValido(uiState.rut)) {
            uiState = uiState.copy(error = "RUT no válido")
            return
        }
        if (uiState.nombre.isBlank() || uiState.clave.isBlank()) {
            uiState = uiState.copy(error = "Complete los campos obligatorios")
            return
        }
        if (uiState.clave != uiState.confirmarClave) {
            uiState = uiState.copy(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)

            // 1. Verificamos si ya existe un empleado registrado con este RUT
            val empleadoExistente = db.empleadoDao().obtenerPorRut(uiState.rut)

            if (empleadoExistente != null) {
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(error = "El usuario con este RUT ya existe.")
                }
                return@launch
            }

            // 2. Si no existe, creamos e insertamos el registro
            val nuevo = EmpleadoEntity(
                id = uiState.rut,
                rut = uiState.rut,
                nombreCompleto = uiState.nombre,
                correo = uiState.correo,
                especialidad = uiState.especialidad,
                clave = uiState.clave
            )

            try {
                db.empleadoDao().registrarEmpleado(nuevo)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(error = "Error al guardar el usuario.")
                }
            }
        }
    }
}