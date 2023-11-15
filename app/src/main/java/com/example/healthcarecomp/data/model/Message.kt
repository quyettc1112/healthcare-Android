package com.example.healthcarecomp.data.model

data class Message(
    val timeStamp: Long = System.currentTimeMillis(),
    val chatRoomId: String? = null,
    val content: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val seen: Boolean = false,
    val attachFiles: List<Attachment>? = null
)