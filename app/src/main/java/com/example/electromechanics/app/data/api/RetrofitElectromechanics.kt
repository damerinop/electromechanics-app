package com.electromechanics.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    const val HOST = "10.0.2.2"

    const val PUERTO_EMPLEADOS = 8084
    const val PUERTO_ACTIVIDADES = 8088
    const val PUERTO_JORNADA = 8086
}

object RetrofitElectromechanics {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private fun <T> crear(puerto: Int, api: Class<T>): T =
        Retrofit.Builder()
            .baseUrl("http://${ApiConfig.HOST}:$puerto/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)

    val empleados: EmpleadoApi by lazy { crear(ApiConfig.PUERTO_EMPLEADOS, EmpleadoApi::class.java) }
    val ordenes: OrdenTrabajoApi by lazy { crear(ApiConfig.PUERTO_ACTIVIDADES, OrdenTrabajoApi::class.java) }
    val actividades: ActividadApi by lazy { crear(ApiConfig.PUERTO_ACTIVIDADES, ActividadApi::class.java) }
    val jornadas: JornadaApi by lazy { crear(ApiConfig.PUERTO_JORNADA, JornadaApi::class.java) }
}
