package com.electromechanics.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla para guardar la auditoría de asistencia con GPS
@Entity(tableName = "registros_jornada")
data class RegistroJornadaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idEmpleado: String,
    val tipoMarcacion: String, // "Entrada" o "Salida"
    val fechaHoraMarcacion: String,
    val latitudMarcacion: Double,
    val longitudMarcacion: Double
)