package com.electromechanics.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.electromechanics.app.data.local.entity.EmpleadoEntity

@Dao
interface EmpleadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(empleado: EmpleadoEntity)

    @Query("SELECT * FROM empleados WHERE sesionActiva = 1 LIMIT 1")
    suspend fun obtenerSesion(): EmpleadoEntity?

    @Query("SELECT * FROM empleados")
    suspend fun obtenerTodos(): List<EmpleadoEntity>

    @Query("UPDATE empleados SET sesionActiva = 0")
    suspend fun cerrarSesiones()
}
