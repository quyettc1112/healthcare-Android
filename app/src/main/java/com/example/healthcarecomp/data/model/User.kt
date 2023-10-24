package com.example.healthcarecomp.data.model

data class User(
    val id: Int?,
    val phone: Int?,
    val email: String?,
    var password: String?,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?
)