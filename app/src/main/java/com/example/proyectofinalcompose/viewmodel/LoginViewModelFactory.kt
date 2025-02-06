package com.example.proyectofinalcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinalcompose.data.UserDao

class LoginViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {

    // Este método es el encargado de crear instancias del ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprobamos si el ViewModel solicitado es del tipo LoginViewModel
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Devolvemos una nueva instancia de LoginViewModel, pasando el userDao
            @Suppress("UNCHECKED_CAST") // Aseguramos que la conversión de tipo es segura
            return LoginViewModel(userDao) as T
        }
        // Si el ViewModel solicitado no es del tipo esperado, lanzamos una excepción
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
