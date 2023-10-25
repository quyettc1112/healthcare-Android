package com.example.healthcarecomp.data.model

import java.util.UUID

data class User(
    val phone: Int?,
    val email: String?,
    var password: String?,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    val id: String = UUID.randomUUID().toString()
)