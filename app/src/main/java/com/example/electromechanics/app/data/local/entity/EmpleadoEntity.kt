package com.electromechanics.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla para guardar empleados registrados
@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey val id: String, // Usamos RUT o Correo como ID
    val rut: String,
    val nombreCompleto: String,
    val correo: String,
    val especialidad: String,
    val clave: String
)