package com.example.proyectofinalcompose.ui

import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalcompose.NotificationHelper
import com.example.proyectofinalcompose.data.ApiUser
import com.example.proyectofinalcompose.viewmodel.ApiViewModel
import kotlin.system.exitProcess

@Composable
fun ApiScreen(viewModel: ApiViewModel = viewModel(), onBackPressed: () -> Unit) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    val context = LocalContext.current

    // 🔹 Estado para controlar las notificaciones periódicas con Handler
    val notificationHandler = remember { Handler(Looper.getMainLooper()) }
    val notificationRunnable = remember {
        object : Runnable {
            override fun run() {
                NotificationHelper.showAccessNotification(context, "¡No olvides consultar la API!")
                notificationHandler.postDelayed(this, 30_000) // Se repite cada 30 segundos
            }
        }
    }

    // Usamos DisposableEffect para detener las notificaciones cuando la pantalla sea removida
    DisposableEffect(key1 = true) {
        // Cuando el composable es montado, iniciar las notificaciones
        notificationHandler.postDelayed(notificationRunnable, 30_000)

        onDispose {
            // Cuando la pantalla sea desmontada, detener las notificaciones
            notificationHandler.removeCallbacks(notificationRunnable)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.fetchUsers()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 🔙 Botón para volver y detener las notificaciones periódicas
                Button(
                    onClick = {
                        notificationHandler.removeCallbacks(notificationRunnable) // Detiene el Runnable
                        onBackPressed() // Llama a la acción de volver
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                ) {
                    Text("Volver", fontSize = 18.sp, color = Color.White)
                }

                // ❌ Botón para cerrar la aplicación
                Button(
                    onClick = {
                        Process.killProcess(Process.myPid()) // Cierra la app
                        exitProcess(0)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Text("Cerrar App", fontSize = 18.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📝 Mostrar usuarios o mensaje de carga
            if (users.isEmpty()) {
                Text(
                    text = "Cargando usuarios...",
                    style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // 📜 Lista de usuarios obtenidos
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(users) { user -> UserCard(user) }
                }
            }
        }
    }
}

// 🔹 Componente de Tarjeta para cada usuario
@Composable
fun UserCard(user: ApiUser) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            UserInfo(label = "Nombre", value = user.name)
            UserInfo(label = "Correo", value = user.email)
            UserInfo(label = "Ciudad", value = user.address.city)
        }
    }
}

// 🔹 Componente de Información para mostrar datos de usuario
@Composable
fun UserInfo(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "$label:",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF555555))
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 16.sp, color = Color(0xFF333333))
        )
    }
}

