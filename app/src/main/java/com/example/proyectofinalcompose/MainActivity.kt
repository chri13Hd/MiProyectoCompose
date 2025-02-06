package com.example.proyectofinalcompose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.core.content.ContextCompat
import androidx.navigation.compose.*
import androidx.work.*
import com.example.proyectofinalcompose.data.NotificationWorker
import com.example.proyectofinalcompose.ui.*
import java.util.concurrent.TimeUnit

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationUtils.createNotificationChannel(this)

        // Solicitar permisos de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Programar notificaciones
        scheduleNotification(this)

        // Verificar si el usuario ya está registrado
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        setContent {
            val navController = rememberNavController()
            val isLoggedIn = userEmail != null

            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) "welcome/$userEmail" else "login"
            ) {
                composable("login") {
                    LoginScreen(
                        onLoginSuccess = { email ->
                            sharedPreferences.edit().apply {
                                putString("userEmail", email)
                                apply()
                            }
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
                        onApiButtonClicked = { navController.navigate("api") },
                        onLogout = {
                            sharedPreferences.edit().remove("userEmail").apply()
                            navController.navigate("login") {
                                popUpTo("welcome/$userEmail") { inclusive = true }
                            }
                        },
                        context = this@MainActivity,
                        navController = navController
                    )
                }

                composable("api") {
                    ApiScreen(onBackPressed = { navController.popBackStack() })
                }
            }
        }
    }

    private fun scheduleNotification(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            15, TimeUnit.MINUTES
        ).setInitialDelay(5, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
