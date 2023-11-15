package com.example.healthcarecomp.data.repositoryImpl

import android.content.Context
import android.util.Log
import com.example.healthcarecomp.data.api.RetrofitInstance
import com.example.healthcarecomp.data.model.SendMessageRequest
import com.example.healthcarecomp.data.repository.NotificationRepository
import com.google.auth.oauth2.GoogleCredentials
import com.example.healthcarecomp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    val TAG = "notification repository"
    val ACCESS_TOKEN_PREFIX = "Bearer "
    private val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
    private val SCOPES = mutableListOf(MESSAGING_SCOPE)
    private var accessToken: String? = null

    /**
     * this function will call api to post notification
     * if accessToke is expire or null then recall getAccessToken to get access token
     */
    override suspend fun sendNotification(notification: SendMessageRequest, context: Context) {
        try {
            if (accessToken != null) {
                val response =
                    RetrofitInstance.notificationAPI.postNotification(notification, accessToken!!)
                if (response.isSuccessful) {
                    Log.d(TAG, "response: $notification")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                    accessToken = getAccessToken(context)
                    sendNotification(notification, context)
                }
            } else {
                accessToken = getAccessToken(context)
                sendNotification(notification, context)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun getAccessToken(context: Context): String? {
        return withContext(Dispatchers.IO) {
            try {
                val googleCredentials: GoogleCredentials = GoogleCredentials
                    .fromStream(context.resources.openRawResource(R.raw.service_account))
                    .createScoped(SCOPES)
                googleCredentials.refresh()
                return@withContext "$ACCESS_TOKEN_PREFIX${googleCredentials.accessToken.tokenValue}"
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                return@withContext null
            }
        }
    }


}