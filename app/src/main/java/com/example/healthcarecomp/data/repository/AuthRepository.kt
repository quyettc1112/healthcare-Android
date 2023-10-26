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
        phone: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String,
        doctorCode: String?
    ): Resource<User>

    suspend fun loginByPhone(phone: String, password: String): Resource<FirebaseUser>

    fun logout()
}