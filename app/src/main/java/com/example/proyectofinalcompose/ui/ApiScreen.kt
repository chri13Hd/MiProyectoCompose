package com.example.proyectofinalcompose.ui

import android.content.Context
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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.proyectofinalcompose.data.ApiUser
import com.example.proyectofinalcompose.data.NotificationWorker
import com.example.proyectofinalcompose.viewmodel.ApiViewModel
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

@Composable
fun ApiScreen(viewModel: ApiViewModel = viewModel(), onBackPressed: () -> Unit) {
    val users by viewModel.users.collectAsState()
    val context = LocalContext.current // Obtener contexto para WorkManager

    LaunchedEffect(key1 = true) { // Evitar recomposiciones innecesarias
        viewModel.fetchUsers()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo gris claro
            .padding(16.dp)
    ) {
        Column {
            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón para volver a la pantalla anterior y reactivar notificaciones
                Button(
                    onClick = {
                        scheduleNotification(context) // 🔔 Reactivar notificaciones periódicas
                        onBackPressed() // 🔙 Volver a la pantalla anterior
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                ) {
                    Text("Volver", fontSize = 18.sp, color = Color.White)
                }

                // Botón para cerrar la aplicación
                Button(
                    onClick = {
                        Process.killProcess(Process.myPid()) // Cierra la app
                        exitProcess(0) // Finaliza la ejecución
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Text("Cerrar App", fontSize = 18.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar usuarios o mensaje de carga
            if (users.isEmpty()) {
                Text(
                    text = "Cargando usuarios...",
                    style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(users) { user -> UserCard(user) }
                }
            }
        }
    }
}

// 🔹 Función para reactivar las notificaciones periódicas
fun scheduleNotification(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
        15, TimeUnit.MINUTES
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "NotificationWorker",
        ExistingPeriodicWorkPolicy.REPLACE, // 🔄 Reemplaza cualquier tarea anterior
        workRequest
    )
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
