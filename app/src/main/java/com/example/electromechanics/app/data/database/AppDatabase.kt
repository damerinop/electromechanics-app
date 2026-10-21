package com.electromechanics.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.electromechanics.app.data.local.dao.EmpleadoDao
import com.electromechanics.app.data.local.dao.RegistroJornadaDao
import com.electromechanics.app.data.local.entity.EmpleadoEntity
import com.electromechanics.app.data.local.entity.RegistroJornadaEntity

@Database(entities = [EmpleadoEntity::class, RegistroJornadaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun empleadoDao(): EmpleadoDao
    abstract fun registroJornadaDao(): RegistroJornadaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "electromechanics_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}