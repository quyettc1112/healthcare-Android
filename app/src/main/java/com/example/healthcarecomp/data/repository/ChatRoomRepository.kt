package com.example.healthcarecomp.data.repository

import android.animation.ValueAnimator.AnimatorUpdateListener
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.Query

interface ChatRoomRepository {

    suspend fun onChatRoomLoad(userId: String, listener: (Resource<ChatRoom>) -> Unit, updateListener: (Resource<ChatRoom>) -> Unit)
    suspend fun upsert(chatRoom: ChatRoom) : (Resource<ChatRoom>)

    suspend fun findChatRoom(userId: String, partnerId: String, onChatRoomFound:(ChatRoom) -> Unit, onChatRoomNotFound: () -> Unit)


}