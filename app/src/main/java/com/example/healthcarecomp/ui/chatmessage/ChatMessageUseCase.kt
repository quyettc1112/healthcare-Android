package com.example.healthcarecomp.ui.chatmessage

import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.repository.ChatMessageRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class ChatMessageUseCase @Inject constructor(
    private val chatMessageRepository: ChatMessageRepository
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
}