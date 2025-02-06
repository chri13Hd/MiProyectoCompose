package com.example.proyectofinalcompose.data

import retrofit2.http.GET

data class ApiUser(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val address: Address = Address()
)

data class Address(
    val street: String = "",
    val suite: String = "",
    val city: String = "",
    val zipcode: String = ""
)

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<ApiUser>
}
