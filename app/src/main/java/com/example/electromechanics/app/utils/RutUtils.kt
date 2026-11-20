package com.electromechanics.app.utils

// Validador de RUT chileno (módulo 11)
object RutUtils {

    // "12.345.678-5" -> "123456785"
    fun limpiar(rut: String): String =
        rut.replace(".", "").replace("-", "").replace(" ", "").uppercase()

    fun esValido(rut: String): Boolean {
        val limpio = limpiar(rut)
        if (limpio.length < 8 || limpio.length > 9) return false // cuerpo de 7 u 8 dígitos + DV

        val cuerpo = limpio.dropLast(1)
        val dv = limpio.last()
        if (!cuerpo.all { it in '0'..'9' }) return false

        var suma = 0
        var multiplicador = 2
        for (c in cuerpo.reversed()) {
            suma += (c - '0') * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        val resto = 11 - (suma % 11)
        val esperado = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> ('0' + resto)
        }
        return dv == esperado
    }

    // "123456785" o "12.345.678-5" -> "12345678-5" (formato que se envía al backend)
    fun formatear(rut: String): String {
        val limpio = limpiar(rut)
        return if (limpio.length < 2) limpio else "${limpio.dropLast(1)}-${limpio.last()}"
    }
}
