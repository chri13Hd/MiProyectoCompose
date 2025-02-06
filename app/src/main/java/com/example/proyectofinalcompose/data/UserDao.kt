package com.example.proyectofinalcompose.data

import androidx.room.*
import java.util.Date

@Dao
interface UserDao {
    // Insertar un nuevo usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Obtener un usuario por correo electrónico
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    // Actualizar un usuario
    @Update
    suspend fun updateUser(user: UserEntity)

    // Obtener todos los usuarios
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    // Buscar usuario por correo y contraseña
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?

    // Actualizar el acceso del usuario
    @Query("UPDATE users SET accessCount = accessCount + 1, lastAccessDate = :lastAccessDate WHERE email = :email")
    suspend fun updateUserAccess(email: String, lastAccessDate: String)

    @Query("SELECT accessCount FROM users WHERE id = :userId")
    fun getAccessCount(userId: String): Int

}
