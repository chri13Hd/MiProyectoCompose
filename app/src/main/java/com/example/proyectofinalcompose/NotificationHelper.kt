package com.example.proyectofinalcompose

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
    fun showAccessNotification(context: Context, message: String) {
        // Verificar si el permiso está otorgado
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Crear un Intent para abrir la API cuando se haga clic en la notificación
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("navigateTo", "api")
            }

            // Crear un PendingIntent para la acción de la notificación
            val pendingIntent = PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(), // Usar un identificador único
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Construir la notificación
            val notification = NotificationCompat.Builder(context, NotificationUtils.getChannelId())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Consulta la API")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    "Abrir API",
                    pendingIntent
                )
                .setAutoCancel(true)
                .build()

            // Enviar la notificación
            with(NotificationManagerCompat.from(context)) {
                notify(System.currentTimeMillis().toInt(), notification) // Usar identificador único
            }
        }
    }
}
