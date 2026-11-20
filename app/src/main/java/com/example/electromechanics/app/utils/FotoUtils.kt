package com.electromechanics.app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File

// Bitmap reducido (máx. 800 px) -> JPEG 70% -> Base64 (va en evidenciaFotografica)
object FotoUtils {
    private const val MAX_LADO = 800
    private const val CALIDAD_JPEG = 70

    fun cargarEscalada(archivo: File): Bitmap? {
        return try {
            val ruta = archivo.absolutePath

            val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(ruta, limites)

            var muestra = 1
            while (limites.outWidth / muestra > MAX_LADO * 2 || limites.outHeight / muestra > MAX_LADO * 2) {
                muestra *= 2
            }
            val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
            var bmp = BitmapFactory.decodeFile(ruta, opciones) ?: return null

            val mayor = maxOf(bmp.width, bmp.height)
            if (mayor > MAX_LADO) {
                val f = MAX_LADO.toFloat() / mayor
                bmp = Bitmap.createScaledBitmap(bmp, (bmp.width * f).toInt(), (bmp.height * f).toInt(), true)
            }

            // La cámara suele guardar la rotación en EXIF y no en los píxeles
            val grados = when (ExifInterface(ruta).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
            if (grados != 0f) {
                val matriz = Matrix().apply { postRotate(grados) }
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matriz, true)
            }
            bmp
        } catch (e: Exception) {
            null
        }
    }

    fun aBase64(bitmap: Bitmap): String {
        val salida = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, CALIDAD_JPEG, salida)
        return Base64.encodeToString(salida.toByteArray(), Base64.NO_WRAP)
    }

    // Devuelve null si el texto no es una imagen Base64
    fun desdeBase64(texto: String?): Bitmap? {
        if (texto.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(texto, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }
}
