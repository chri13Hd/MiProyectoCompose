package com.example.proyectofinalcompose

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import com.example.proyectofinalcompose.ui.*

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                NotificationHelper.showAccessNotification(
                    this,
                    "No tienes permisos para recibir notificaciones"
                )
            }
        }

    private val handler = Handler(Looper.getMainLooper())
    private var notificationsEnabled = false  // Variable para controlar si las notificaciones están activadas
    private var hasConsultedApi = false  // Variable para saber si el usuario ha consultado la API

    private val notificationRunnable = object : Runnable {
        override fun run() {
            if (!hasConsultedApi) {  // Solo mostrar la notificación si no se ha consultado la API
                NotificationHelper.showAccessNotification(
                    this@MainActivity,
                    "Consulta la API para obtener datos nuevos"
                )
                handler.postDelayed(this, 500) // Repite cada 0.5 segundos
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear el canal de notificación al iniciar la aplicación
        NotificationUtils.createNotificationChannel(applicationContext)

        // Solicitar permisos en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Mostrar notificación al acceder a la aplicación
        NotificationHelper.showAccessNotification(
            this,
            "Bienvenido, puedes consultar la API"
        )

        // Verificar si el usuario ya está registrado
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        // Si el usuario está registrado, iniciar el ciclo de notificaciones
        if (userEmail != null) {
            startNotificationLoop() // Iniciar notificaciones si el usuario está registrado
        }

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = if (userEmail != null) "welcome/$userEmail" else "login"
            ) {
                composable("login") {
                    LoginScreen(
                        onLoginSuccess = { email ->
                            sharedPreferences.edit().apply {
                                putString("userEmail", email)
                                apply()
                            }
                            startNotificationLoop() // Iniciar notificaciones
                            navController.navigate("welcome/$email") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onRegisterClicked = { navController.navigate("register") }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        onRegisterSuccess = {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                    )
                }

                composable("welcome/{userEmail}") { backStackEntry ->
                    val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                    WelcomeScreen(
                        userEmail = userEmail,
                        onApiButtonClicked = {
                            // Cuando el usuario consulta la API, desactivamos las notificaciones
                            hasConsultedApi = true
                            stopNotificationLoop()
                            navController.navigate("api")
                        },
                        onLogout = {
                            sharedPreferences.edit().remove("userEmail").apply()
                            stopNotificationLoop() // Detener notificaciones al cerrar sesión
                            navController.navigate("login") {
                                popUpTo("welcome/$userEmail") { inclusive = true }
                            }
                        },
                        context = this@MainActivity,
                        navController = navController
                    )
                }

                composable("api") {
                    ApiScreen(onBackPressed = {
                        // Reactivar las notificaciones al presionar el botón de volver
                        hasConsultedApi = false
                        startNotificationLoop()
                        navController.popBackStack()
                    })
                }
            }
        }
    }

    // Función para iniciar el ciclo de notificaciones
    private fun startNotificationLoop() {
        if (!notificationsEnabled) {
            notificationsEnabled = true
            handler.postDelayed(notificationRunnable, 500) // Inicia en 0.5 segundos
        }
    }

    // Función para detener el ciclo de notificaciones
    private fun stopNotificationLoop() {
        notificationsEnabled = false
        handler.removeCallbacks(notificationRunnable) // Detiene el Runnable
    }

    // Asegurarse de detener las notificaciones al cerrar la app
    override fun onDestroy() {
        super.onDestroy()
        stopNotificationLoop() // Asegurar que se detienen las notificaciones al cerrar la app
    }
}
