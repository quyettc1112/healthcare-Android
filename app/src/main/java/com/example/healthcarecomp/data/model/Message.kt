package com.example.healthcarecomp.data.model

data class Message(
    val timeStamp: Long = System.currentTimeMillis(),
    val roomChatId: String? = null,
    val content: String? = null
)