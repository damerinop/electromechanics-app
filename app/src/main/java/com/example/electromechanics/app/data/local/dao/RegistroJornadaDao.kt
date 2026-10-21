package com.electromechanics.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.electromechanics.app.data.local.entity.RegistroJornadaEntity

@Dao
interface RegistroJornadaDao {
    @Insert
    suspend fun registrarMarcacion(registro: RegistroJornadaEntity)

    // Consulta para la pantalla de Auditoría
    @Query("SELECT * FROM registros_jornada WHERE idEmpleado = :idEmpleado ORDER BY id DESC")
    suspend fun obtenerJornadasPorEmpleado(idEmpleado: String): List<RegistroJornadaEntity>
}