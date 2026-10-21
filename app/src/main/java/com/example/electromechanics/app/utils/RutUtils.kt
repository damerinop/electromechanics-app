package com.electromechanics.app.utils

// Validación simple de RUT o Correo para facilitar pruebas
object RutUtils {
    fun esValido(entrada: String): Boolean {
        if (entrada.isBlank()) return false
        // Si contiene un arroba es correo, si no asumimos RUT de más de 7 dígitos
        return entrada.contains("@") || entrada.replace(".", "").replace("-", "").length >= 7
    }
}