package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    var currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(
        email: String,
        password: String,
        confirmPassword: String
    ): Resource<FirebaseUser>

    suspend fun signup(
        phone: Int,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String
    ): Resource<User>

    suspend fun login(phone: Int, password: String): Resource<FirebaseUser>

    fun logout()
}