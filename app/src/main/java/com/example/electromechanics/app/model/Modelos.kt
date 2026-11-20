package com.electromechanics.app.model

data class Empleado(
    val idEmpleado: String? = null,
    val rutTrabajador: String,
    val nombreCompleto: String,
    val rol: String = "TECNICO",
    val especialidadTecnica: String? = null,
    val correoCorporativo: String,
    val contrasena: String
) {
    val esSupervisor: Boolean get() = rol.equals("SUPERVISOR", ignoreCase = true)
    val esTecnico: Boolean get() = rol.equals("TECNICO", ignoreCase = true)
}

data class OrdenTrabajo(
    val idOrden: String? = null,
    val idSupervisor: String,
    val idTecnicoAsignado: String? = null,
    val cliente: String,
    val tarea: String,
    val estado: String = "Pendiente",
    val fechaCreacion: String? = null
)

data class RegistroActividad(
    val idActividad: String? = null,
    val idOrden: String,
    val idTecnico: String,
    val comentario: String? = null,
    val evidenciaFotografica: String? = null,
    val fechaHoraRegistro: String? = null
)

data class Jornada(
    val idRegistro: String? = null,
    val idEmpleado: String,
    val tipoMarcacion: String, // "ENTRADA" o "SALIDA"
    val fechaHoraMarcacion: String? = null,
    val latitudMarcacion: Double,
    val longitudMarcacion: Double
)

data class EstadoRequest(val estado: String)
