package com.electromechanics.app.utils

import java.security.MessageDigest

// Hash SHA-256 con sal (el RUT). La contraseña nunca se guarda en texto plano en Room.
object Seguridad {
    fun hash(clave: String, sal: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest("$sal:$clave".toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
}
