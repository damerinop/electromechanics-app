package com.electromechanics.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electromechanics.app.data.api.RetrofitElectromechanics
import com.electromechanics.app.model.Empleado
import com.electromechanics.app.model.EstadoRequest
import com.electromechanics.app.model.Jornada
import com.electromechanics.app.model.OrdenTrabajo
import com.electromechanics.app.model.RegistroActividad
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OperacionesViewModel : ViewModel() {

    private val _empleados = MutableLiveData<List<Empleado>>(emptyList())
    val empleados: LiveData<List<Empleado>> = _empleados

    private val _ordenes = MutableLiveData<List<OrdenTrabajo>>(emptyList())
    val ordenes: LiveData<List<OrdenTrabajo>> = _ordenes

    private val _actividades = MutableLiveData<List<RegistroActividad>>(emptyList())
    val actividades: LiveData<List<RegistroActividad>> = _actividades

    private val _jornadas = MutableLiveData<List<Jornada>>(emptyList())
    val jornadas: LiveData<List<Jornada>> = _jornadas

    private val _mensaje = MutableLiveData<String?>(null)
    val mensaje: LiveData<String?> = _mensaje

    fun limpiarMensaje() { _mensaje.value = null }

    private fun ejecutar(bloque: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                bloque()
            } catch (e: HttpException) {
                _mensaje.value = "Error del servidor (${e.code()}). Revise datos duplicados o inválidos."
            } catch (e: Exception) {
                _mensaje.value = "Sin conexión: ${e.message}"
            }
        }
    }

    fun cargarEmpleados() = ejecutar {
        _empleados.value = RetrofitElectromechanics.empleados.listar()
    }

    fun crearTecnico(
        rut: String, nombre: String, correo: String,
        especialidad: String, clave: String, onExito: () -> Unit
    ) = ejecutar {
        RetrofitElectromechanics.empleados.crear(
            Empleado(
                rutTrabajador = rut.trim(),
                nombreCompleto = nombre.trim(),
                rol = "TECNICO",
                especialidadTecnica = especialidad.trim().ifBlank { null },
                correoCorporativo = correo.trim(),
                contrasena = clave
            )
        )
        _empleados.value = RetrofitElectromechanics.empleados.listar()
        _mensaje.value = "Técnico creado"
        onExito()
    }

    fun cargarOrdenes() = ejecutar {
        _ordenes.value = RetrofitElectromechanics.ordenes.listar()
    }

    fun crearOrden(
        idSupervisor: String, idTecnico: String,
        cliente: String, tarea: String, onExito: () -> Unit
    ) = ejecutar {
        RetrofitElectromechanics.ordenes.crear(
            OrdenTrabajo(
                idSupervisor = idSupervisor,
                idTecnicoAsignado = idTecnico,
                cliente = cliente.trim().take(100),
                tarea = tarea.trim().take(100)
            )
        )
        _ordenes.value = RetrofitElectromechanics.ordenes.listar()
        _mensaje.value = "Orden creada y asignada"
        onExito()
    }

    fun cambiarEstado(idOrden: String, estado: String) = ejecutar {
        RetrofitElectromechanics.ordenes.actualizarEstado(idOrden, EstadoRequest(estado))
        _ordenes.value = RetrofitElectromechanics.ordenes.listar()
        _mensaje.value = "Estado actualizado: $estado"
    }

    fun cargarActividades() = ejecutar {
        _actividades.value = RetrofitElectromechanics.actividades.listar()
    }

    fun registrarActividad(
        idOrden: String, idTecnico: String,
        comentario: String, fotoBase64: String?, onExito: () -> Unit
    ) = ejecutar {
        RetrofitElectromechanics.actividades.crear(
            RegistroActividad(
                idOrden = idOrden,
                idTecnico = idTecnico,
                comentario = comentario.trim().take(250).ifBlank { null },
                evidenciaFotografica = fotoBase64
            )
        )
        _mensaje.value = "Actividad registrada"
        onExito()
    }

    fun cargarJornadas() = ejecutar {
        _jornadas.value = RetrofitElectromechanics.jornadas.listar()
    }

    fun marcarJornada(idEmpleado: String, tipo: String, lat: Double, lng: Double) = ejecutar {
        RetrofitElectromechanics.jornadas.crear(
            Jornada(
                idEmpleado = idEmpleado,
                tipoMarcacion = tipo,
                latitudMarcacion = lat,
                longitudMarcacion = lng
            )
        )
        _jornadas.value = RetrofitElectromechanics.jornadas.listar()
        _mensaje.value = "$tipo registrada con GPS"
    }
}
