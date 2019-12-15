package com.monicatifanyz.digimagz.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.model.Notif
import com.monicatifanyz.digimagz.view.activity.SplashActivity
import java.util.*

class FirebaseMessaging : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.size > 0) {
            val notif = Notif()
            notif.title = remoteMessage.data["title"]
            notif.description = remoteMessage.data["body"]
            notif.isRead = 0
            showMessages(remoteMessage)
        }
    }

    private fun showMessages(rm: RemoteMessage) {
        val rand = Random().nextInt(9999) + 1
        val data = rm.data
        val textTitle = data["title"]
        val textContent = data["body"]
        val activityIntent = Intent(this, SplashActivity::class.java)
        activityIntent.putExtra("id", rand)
        val pendingActivity =
            PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence =
                CHANNEL_NAME
            val description =
                CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            )
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
        val builder =
            NotificationCompat.Builder(
                this,
                CHANNEL_ID
            )
                .setSmallIcon(R.mipmap.ic_launcher_digimagz)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(textContent)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingActivity)
        builder.setVibrate(longArrayOf(1000, 1000))
        notificationManager.notify(rand, builder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    companion object {
        const val CHANNEL_ID = "DIGIMAGZ"
        const val CHANNEL_NAME = "DigiMagz"
    }
}