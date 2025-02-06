package com.example.proyectofinalcompose.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Converters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC") // Usamos UTC para evitar problemas con zonas horarias
    }

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return try {
            value?.let { dateFormat.parse(it) }
        } catch (e: Exception) {
            null // Devuelve null si hay un error al parsear la fecha
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }
}
