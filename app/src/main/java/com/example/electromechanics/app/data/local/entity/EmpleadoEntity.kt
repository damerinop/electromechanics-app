package com.electromechanics.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.electromechanics.app.model.Empleado

// Usuarios que han iniciado sesión en este dispositivo (permite login sin conexión y sesión persistente)
@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey val id: String, // idEmpleado (UUID del microservicio)
    val rut: String,
    val nombreCompleto: String,
    val rol: String,
    val especialidad: String?,
    val correo: String,
    val claveHash: String,
    val sesionActiva: Boolean = false
) {
    fun toEmpleado() = Empleado(
        idEmpleado = id,
        rutTrabajador = rut,
        nombreCompleto = nombreCompleto,
        rol = rol,
        especialidadTecnica = especialidad,
        correoCorporativo = correo,
        contrasena = ""
    )
}

fun Empleado.toEntity(claveHash: String) = EmpleadoEntity(
    id = idEmpleado.orEmpty(),
    rut = rutTrabajador,
    nombreCompleto = nombreCompleto,
    rol = rol,
    especialidad = especialidadTecnica,
    correo = correoCorporativo,
    claveHash = claveHash,
    sesionActiva = true
)
