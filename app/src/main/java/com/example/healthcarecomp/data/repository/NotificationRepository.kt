package com.example.healthcarecomp.data.repository

import android.content.Context
import com.example.healthcarecomp.data.model.MessageNotification
import com.example.healthcarecomp.data.model.SendMessageRequest

interface NotificationRepository {
    suspend fun sendNotification(notification: SendMessageRequest, context: Context)
}