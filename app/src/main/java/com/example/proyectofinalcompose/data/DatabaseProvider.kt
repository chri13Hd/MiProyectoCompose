package com.example.proyectofinalcompose.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    fun getDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "user-database"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2) // Asegúrate de agregar la migración aquí
            .build()
    }
}
