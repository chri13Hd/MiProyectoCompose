/*package com.example.proyectofinalcompose.data

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.proyectofinalcompose.NotificationHelper

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Mostrar la notificación
        NotificationHelper.showAccessNotification(
            applicationContext,
            "Recuerda consultar la API"
        )

        // Indicar que el trabajo se completó con éxito
        return Result.success()
    }
}
*/