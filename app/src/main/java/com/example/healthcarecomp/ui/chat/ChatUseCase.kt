package com.example.healthcarecomp.ui.chat

import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val authRepository: AuthRepository
) {
     suspend fun onFirstUserChange(
        userId: String,
        listener: (Resource<ChatRoom>) -> Unit
    ) {
         chatRoomRepository.onFirstUserChange(userId, listener)
     }

    suspend fun onSecondUserChange(
        userId: String,
        listener: (Resource<ChatRoom>) -> Unit
    ) {
        chatRoomRepository.onSecondUserChange(userId, listener)
    }

    suspend fun upsert(chatRoom: ChatRoom) : Resource<ChatRoom> {
       return chatRoomRepository.upsert(chatRoom)
    }

    suspend fun onChatRoomLoad(userId: String, listener: (Resource<ChatRoom>) -> Unit, updateListener: (Resource<ChatRoom>) -> Unit){
        chatRoomRepository.onChatRoomLoad(userId,listener, updateListener)
    }

    suspend fun getUserById(
        userId: String,
        listener: (Resource<User?>) -> Unit
    ){
        authRepository.getUserById(userId, listener)
    }
}