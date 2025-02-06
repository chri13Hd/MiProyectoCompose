package com.example.proyectofinalcompose.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WelcomeScreen(
    userEmail: String,
    onApiButtonClicked: () -> Unit,
    onLogout: () -> Unit,
    context: Context,
    navController: NavController
) {
    // Obtiene SharedPreferences
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Claves únicas para este usuario
    val userAccessCountKey = "${userEmail}_accessCount"
    val userLastAccessDateKey = "${userEmail}_lastAccessDate"
    val sessionIncrementedKey = "${userEmail}_sessionIncremented"

    // Obtener los valores actuales
    var accessCount by remember { mutableStateOf(sharedPreferences.getInt(userAccessCountKey, 0)) }
    var lastAccessDate by remember { mutableStateOf(sharedPreferences.getString(userLastAccessDateKey, "Nunca") ?: "Nunca") }
    val sessionIncremented = sharedPreferences.getBoolean(sessionIncrementedKey, false)

    // Usamos LaunchedEffect para incrementar el contador solo una vez cuando se inicia la sesión.
    LaunchedEffect(key1 = userEmail) {
        // Solo si no se ha incrementado en esta sesión:
        if (!sessionIncremented) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            accessCount += 1
            lastAccessDate = currentDate
            editor.putInt(userAccessCountKey, accessCount)
            editor.putString(userLastAccessDateKey, lastAccessDate)
            editor.putBoolean(sessionIncrementedKey, true)  // Marcamos que ya se incrementó
            editor.apply()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF69AFFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Bienvenido a la App",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Número de accesos: $accessCount",
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "Último acceso: $lastAccessDate",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onApiButtonClicked,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
            ) {
                Text("Consultar API", fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Para cerrar sesión, eliminamos la credencial y también reiniciamos el flag de sesión
                    editor.remove("userEmail")
                    editor.putBoolean(sessionIncrementedKey, false)
                    editor.apply()
                    navController.navigate("login") {
                        popUpTo("welcome/$userEmail") { inclusive = true }
                    }
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
            ) {
                Text("Cerrar sesión", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
