package com.example.proyectofinalcompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    // Define las migraciones, si es necesario
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Si estás añadiendo una nueva columna a la tabla
                database.execSQL("ALTER TABLE user_entity ADD COLUMN accessCount INTEGER DEFAULT 0")
            }
        }
    }
}
