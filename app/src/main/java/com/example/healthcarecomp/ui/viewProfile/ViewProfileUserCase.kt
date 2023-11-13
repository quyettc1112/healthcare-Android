package com.example.healthcarecomp.ui.viewProfile

import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class ViewProfileUserCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend fun findChatRoom(userId: String, partnerId: String, onChatRoomFound:(ChatRoom) -> Unit, onChatRoomNotFound: () -> Unit){
        chatRoomRepository.findChatRoom(userId, partnerId, onChatRoomFound, onChatRoomNotFound)
    }

    suspend fun upsert(chatRoom: ChatRoom): Resource<ChatRoom> {
        return chatRoomRepository.upsert(chatRoom)
    }
}