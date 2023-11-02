package com.example.healthcarecomp.ui.chat

import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
     suspend fun onChatRoomChange(
        userId: String,
        listener: (Resource<MutableList<ChatRoom>>) -> Unit
    ) {
         chatRoomRepository.onChatRoomChange(userId, listener)
     }

    suspend fun upsert(chatRoom: ChatRoom) : Resource<ChatRoom> {
       return chatRoomRepository.upsert(chatRoom)
    }
}