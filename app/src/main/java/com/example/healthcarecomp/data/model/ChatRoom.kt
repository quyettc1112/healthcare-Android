package com.example.healthcarecomp.data.model

import java.io.Serializable
import java.util.UUID

data class ChatRoom(
    val firstUserId: String? = null,
    val secondUserId: String? = null,
    val lastActiveTime: Long? = null,
    val chatSeen: Boolean? = null,
    val id: String = UUID.randomUUID().toString()
) : Serializable
