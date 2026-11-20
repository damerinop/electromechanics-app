package com.electromechanics.app.data.api

import com.electromechanics.app.model.Empleado
import com.electromechanics.app.model.EstadoRequest
import com.electromechanics.app.model.Jornada
import com.electromechanics.app.model.OrdenTrabajo
import com.electromechanics.app.model.RegistroActividad
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EmpleadoApi {
    @GET("api/empleados")
    suspend fun listar(): List<Empleado>

    @GET("api/empleados/{id}")
    suspend fun obtener(@Path("id") id: String): Empleado

    @POST("api/empleados")
    suspend fun crear(@Body empleado: Empleado): Empleado
}

interface OrdenTrabajoApi {
    @GET("api/ordenes-trabajo")
    suspend fun listar(): List<OrdenTrabajo>

    @GET("api/ordenes-trabajo/{id}")
    suspend fun obtener(@Path("id") id: String): OrdenTrabajo

    @POST("api/ordenes-trabajo")
    suspend fun crear(@Body orden: OrdenTrabajo): OrdenTrabajo

    @PATCH("api/ordenes-trabajo/{id}/estado")
    suspend fun actualizarEstado(@Path("id") id: String, @Body body: EstadoRequest): OrdenTrabajo
}

interface ActividadApi {
    @GET("api/actividades")
    suspend fun listar(): List<RegistroActividad>

    @POST("api/actividades")
    suspend fun crear(@Body actividad: RegistroActividad): RegistroActividad
}

interface JornadaApi {
    @GET("api/registrarjornadas")
    suspend fun listar(): List<Jornada>

    @POST("api/registrarjornadas")
    suspend fun crear(@Body jornada: Jornada): Jornada
}
