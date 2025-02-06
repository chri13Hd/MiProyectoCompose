package com.example.proyectofinalcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcompose.data.ApiUser
import com.example.proyectofinalcompose.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {

    private val _users = MutableStateFlow<List<ApiUser>>(emptyList())
    val users: StateFlow<List<ApiUser>> get() = _users

    fun fetchUsers() {
        // Lanzamos una corutina para ejecutar la función suspendida
        viewModelScope.launch {
            try {
                // Llamamos a la función suspendida getUsers
                val fetchedUsers = RetrofitClient.apiService.getUsers()
                _users.value = fetchedUsers
            } catch (e: Exception) {
                // Manejo de errores si falla la petición
                _users.value = emptyList()
            }
        }
    }
}
