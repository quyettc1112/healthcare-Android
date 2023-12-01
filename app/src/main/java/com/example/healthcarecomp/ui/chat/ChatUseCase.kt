package com.example.healthcarecomp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.ChatMessageRepository
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val authRepository: AuthRepository,
    private val chatMessageRepository: ChatMessageRepository
) {

    suspend fun upsert(chatRoom: ChatRoom): Resource<ChatRoom> {
        return chatRoomRepository.upsert(chatRoom)
    }

    suspend fun onChatRoomLoad(
        userId: String,
        listener: (Resource<ChatRoom>) -> Unit,
        updateListener: (Resource<ChatRoom>) -> Unit
    ) {
        chatRoomRepository.onChatRoomLoad(userId, listener, updateListener)
    }

    suspend fun getUserById(
        userId: String,
        listener: (Resource<User?>) -> Unit
    ) {
        authRepository.getUserById(userId, listener)
    }

    suspend fun getLastMessage(chatRoomId: String, listener: (Resource<Message?>) -> Unit) {
        chatMessageRepository.getLastMessage(chatRoomId, listener)
    }

    suspend fun searchUsersByName(
        name: String,
        listener: (MutableLiveData<Resource<MutableList<User>>>) -> Unit
    ) {
        return authRepository.searchUsersByName(name, listener)
    }

    suspend fun findChatRoom(
        userId: String,
        partnerId: String,
        onChatRoomFound: (ChatRoom) -> Unit,
        onChatRoomNotFound: () -> Unit
    ) {
        chatRoomRepository.findChatRoom(userId, partnerId, onChatRoomFound, onChatRoomNotFound)
    }
}