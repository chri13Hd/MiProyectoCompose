package com.example.proyectofinalcompose

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

object NotificationUtils {
    private const val CHANNEL_ID = "user_notifications"
    private const val CHANNEL_NAME = "User Notifications"

    // Crear el canal de notificación solo si no existe
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Este es el canal para las notificaciones"
                }
                notificationManager.createNotificationChannel(channel)
                Log.d("NotificationChannel", "Canal de notificación creado")
            }
        }
    }



    fun getChannelId(): String = CHANNEL_ID
}
