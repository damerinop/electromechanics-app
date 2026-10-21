package com.electromechanics.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.electromechanics.app.data.local.entity.EmpleadoEntity

@Dao
interface EmpleadoDao {

    @Query("SELECT * FROM empleados WHERE rut = :rut AND clave = :clave LIMIT 1")
    suspend fun autenticar(rut: String, clave: String): EmpleadoEntity?

    // Consulta para comprobar si ya existe un usuario registrado con este RUT
    @Query("SELECT * FROM empleados WHERE id = :rut LIMIT 1")
    suspend fun obtenerPorRut(rut: String): EmpleadoEntity?

    // Usamos OnConflictStrategy.ABORT para evitar que se sobrescriba el usuario existente
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarEmpleado(empleado: EmpleadoEntity)
}