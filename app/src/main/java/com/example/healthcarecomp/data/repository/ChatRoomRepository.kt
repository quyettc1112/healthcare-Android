package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.Query

interface ChatRoomRepository {

    suspend fun onChatRoomChange(userId: String,listener: (Resource<MutableList<ChatRoom>>) -> Unit)

    suspend fun upsert(chatRoom: ChatRoom) : (Resource<ChatRoom>)



}