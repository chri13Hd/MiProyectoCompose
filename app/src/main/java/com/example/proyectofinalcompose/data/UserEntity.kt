package com.example.proyectofinalcompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val accessCount: Int = 0, // 🔹 Valor predeterminado para evitar problemas al incrementar
    val lastAccessDate: String = null.toString(), // Usar Long para almacenar el timestamp de la fecha
    val password: String = "" // 🔹 Se establece un valor predeterminado para evitar problemas con `NOT NULL`
)

