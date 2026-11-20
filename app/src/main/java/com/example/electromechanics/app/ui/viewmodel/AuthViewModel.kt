package com.electromechanics.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.electromechanics.app.data.api.RetrofitElectromechanics
import com.electromechanics.app.data.database.AppDatabase
import com.electromechanics.app.data.local.entity.toEntity
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.utils.RutUtils
import com.electromechanics.app.utils.Seguridad
import kotlinx.coroutines.launch
import java.io.IOException

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val empleadoDao = AppDatabase.getDatabase(application).empleadoDao()

    private val _usuario = MutableLiveData<Empleado?>(null)
    val usuario: LiveData<Empleado?> = _usuario

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _sesionCargada = MutableLiveData(false)
    val sesionCargada: LiveData<Boolean> = _sesionCargada

    init {
        viewModelScope.launch {
            try {
                _usuario.value = empleadoDao.obtenerSesion()?.toEmpleado()
            } catch (e: Exception) {
                _usuario.value = null
            }
            _sesionCargada.value = true
        }
    }

    fun login(credencial: String, clave: String, onExito: () -> Unit) {
        if (credencial.isBlank() || clave.isBlank()) {
            _error.value = "Ingrese RUT/correo y contraseña"
            return
        }
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            val rutIngresado = RutUtils.limpiar(credencial)
            val correoIngresado = credencial.trim().lowercase()
            try {
                val emp = RetrofitElectromechanics.empleados.listar().firstOrNull {
                    (RutUtils.limpiar(it.rutTrabajador) == rutIngresado ||
                        it.correoCorporativo.lowercase() == correoIngresado) &&
                        it.contrasena == clave
                }
                if (emp == null) {
                    _error.value = "Credenciales incorrectas"
                } else if (!emp.esSupervisor && !emp.esTecnico) {
                    _error.value = "Rol no habilitado en la app"
                } else {
                    val hash = Seguridad.hash(clave, RutUtils.limpiar(emp.rutTrabajador))
                    empleadoDao.cerrarSesiones()
                    empleadoDao.guardar(emp.toEntity(hash))
                    _usuario.value = emp.copy(contrasena = "")
                    onExito()
                }
            } catch (e: IOException) {
                val local = empleadoDao.obtenerTodos().firstOrNull {
                    (RutUtils.limpiar(it.rut) == rutIngresado || it.correo.lowercase() == correoIngresado) &&
                        it.claveHash == Seguridad.hash(clave, RutUtils.limpiar(it.rut))
                }
                if (local == null) {
                    _error.value = "Sin conexión y sin credenciales guardadas para este usuario"
                } else {
                    empleadoDao.cerrarSesiones()
                    empleadoDao.guardar(local.copy(sesionActiva = true))
                    _usuario.value = local.toEmpleado()
                    onExito()
                }
            } catch (e: Exception) {
                _error.value = "Error al iniciar sesión: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun logout() {
        _usuario.value = null
        _error.value = null
        viewModelScope.launch {
            try {
                empleadoDao.cerrarSesiones()
            } catch (e: Exception) {
            }
        }
    }
}
