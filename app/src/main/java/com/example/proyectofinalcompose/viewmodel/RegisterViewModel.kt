package com.example.proyectofinalcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcompose.data.UserDao
import com.example.proyectofinalcompose.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val userDao: UserDao) : ViewModel() {

    private val _registerResult = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerResult: StateFlow<RegisterState> = _registerResult

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = userDao.getUserByEmail(email)
                if (existingUser != null) {
                    _registerResult.value = RegisterState.Error("El correo electrónico ya está registrado")
                    return@launch
                }

                val newUser = UserEntity(name = name, email = email, password = password, accessCount = 0)
                userDao.insertUser(newUser)

                _registerResult.value = RegisterState.Success
            } catch (e: Exception) {
                _registerResult.value = RegisterState.Error("Error al registrar usuario: ${e.message}")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
