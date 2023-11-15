package com.example.healthcarecomp.data.api

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.SendMessageRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @POST("/v1/projects/healtcarecomp/messages:send")
    suspend fun postNotification(
        @Body messageData: SendMessageRequest,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = Constant.CONTENT_TYPE,
        @Header("Host") host: String = Constant.NOTIFICATION_HOST
    ) : Response<ResponseBody>
}