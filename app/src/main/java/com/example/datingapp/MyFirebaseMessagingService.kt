package com.example.datingapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "NOTIFICATION_CHANNEL"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, HomeScreen::class.java)


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

        val intent1 = PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_MUTABLE)
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_favorite_24)
            .setAutoCancel(true)
            .setContentIntent(intent1)
            .build()

        manager.notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel (manager: NotificationManager) {
        val channel = NotificationChannel(channelId, "datingapp", NotificationManager.IMPORTANCE_HIGH)

        channel.description = "Nuevo mensaje"
        channel.enableLights(true)

        manager.createNotificationChannel(channel)
    }
}