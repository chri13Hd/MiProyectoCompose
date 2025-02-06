package com.example.proyectofinalcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcompose.data.UserDao
import com.example.proyectofinalcompose.data.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginResult: StateFlow<LoginState> = _loginResult

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _loginResult.value = LoginState.Error("Todos los campos son obligatorios")
                return@launch
            }

            val user = userDao.getUserByEmail(email)

            if (user == null) {
                _loginResult.value = LoginState.Error("Usuario no encontrado")
            } else if (user.password != password) {
                _loginResult.value = LoginState.Error("Contraseña incorrecta")
            } else {
                // ✅ Incrementar el contador SOLO si es un nuevo inicio de sesión
                val updatedUser = user.copy(
                    accessCount = user.accessCount + 1, // Incrementa el contador
                    lastAccessDate = Date().toString() // Registra la fecha de acceso actual
                )

                // Guardar los cambios en la base de datos
                userDao.updateUser(updatedUser)

                _loginResult.value = LoginState.Success
            }
        }
    }
}

// Estado de Login: Para manejar los diferentes estados del login (en espera, éxito o error)
sealed class LoginState {
    object Idle : LoginState() // Estado inicial (esperando)
    object Success : LoginState() // Estado de éxito
    data class Error(val message: String) : LoginState() // Estado de error con mensaje
}
