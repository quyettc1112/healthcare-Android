package com.example.healthcarecomp.ui.chatmessage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.model.SendMessageRequest
import com.example.healthcarecomp.data.repository.ChatMessageRepository
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.data.repository.FileUploadRepository
import com.example.healthcarecomp.data.repository.NotificationRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class ChatMessageUseCase @Inject constructor(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val notificationRepository: NotificationRepository,
    private val fileUploadRepository: FileUploadRepository
) {

    suspend fun onChatLoad(
        chatRoomId: String,
        addListener: (Resource<Message>) -> Unit,
        updateListener: ((Resource<Message>) -> Unit)? = null,
        deleteListener: ((Resource<Message>) -> Unit)? = null
    ) {
        chatMessageRepository.onChatLoad(chatRoomId, addListener, updateListener, deleteListener)
    }

    suspend fun upsert(message: Message): Resource<Message> {
        return chatMessageRepository.upsert(message)
    }

    suspend fun upsertChatRoom(chatRoom: ChatRoom): Resource<ChatRoom> {
        return chatRoomRepository.upsert(chatRoom)
    }

    suspend fun sendNotification(notification: SendMessageRequest, context: Context) {
        notificationRepository.sendNotification(notification, context)
    }

    suspend fun uploadFile(
        folder: String,
        fileUri: Uri,
        onSuccess: ((uri: Uri) -> Unit),
        onFailure: ((errorMessage: String) -> Unit),
        onProgress: ((MutableLiveData<Int>) -> Unit)
    ) {
        fileUploadRepository.uploadFile(folder,fileUri,onSuccess,onFailure,onProgress)
    }
}