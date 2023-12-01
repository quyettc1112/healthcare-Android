package com.example.healthcarecomp.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.healthcarecomp.R
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.FileInputStream
import java.io.IOException
import kotlin.random.Random


const val chanelId = "message"
const val chanelName = "Message Notification"

class FirebaseNotificationService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"

    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.i(TAG,"notification")
        message?.let {
            val desc = if(message.notification?.body.isNullOrEmpty()) "Has seen a message" else message.notification?.body
            generateNotification(message.notification?.title!!, desc!!)
            Log.i(TAG,"notification2")
        }
    }

    private fun generateNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val notificationId = Random.nextInt()

        createNotificationChanel(notificationManager)

        val notification = NotificationCompat.Builder(applicationContext, chanelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChanel(notificationManager: NotificationManager) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "My chanel description"
                enableLights(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }




}