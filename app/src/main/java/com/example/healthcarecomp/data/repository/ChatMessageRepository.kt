package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.util.Resource

interface ChatMessageRepository {
    suspend fun upsert(message: Message): Resource<Message>

    suspend fun onChatLoad(
        chatRoomId: String,
        addListener: (Resource<Message>) -> Unit,
        updateListener: ((Resource<Message>) -> Unit)? = null,
        deleteListener: ((Resource<Message>) -> Unit)? = null
    )

    suspend fun getLastMessage(chatRoomId: String, listener: (Resource<Message>) -> Unit)
}