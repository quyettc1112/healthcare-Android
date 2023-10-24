package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

interface AuthRepository {

    val currentUser : FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(email: String, password: String, confirmPassword: String): Resource<FirebaseUser>

    suspend fun login(phone: Int, password: String): Resource<FirebaseUser>
    suspend fun signup(phone: Int, password: String, confirmPassword: String): Resource<FirebaseUser>

    fun logout()
}