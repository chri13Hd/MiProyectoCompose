package com.example.proyectofinalcompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcompose.data.DatabaseProvider
import com.example.proyectofinalcompose.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = DatabaseProvider.getDatabase(application).userDao()

    // Función suspendida que devuelve si hay usuarios registrados
    suspend fun isUserRegistered(): Boolean {
        return userDao.getAllUsers().isNotEmpty()
    }

    // Función para actualizar el contador de accesos cuando el usuario inicie sesión
    fun updateUserAccess(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Obtener al usuario por su email
                val user = userDao.getUserByEmail(email) // Asegúrate de que esta función existe en tu UserDao
                val updatedUser = user?.copy(
                    accessCount = user.accessCount + 1,  // Incrementar el contador de accesos
                    lastAccessDate = java.util.Date().toString() // Actualizar la fecha de último acceso
                )
                if (updatedUser != null) {
                    userDao.updateUser(updatedUser)
                }  // Actualizar el usuario en la base de datos
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
