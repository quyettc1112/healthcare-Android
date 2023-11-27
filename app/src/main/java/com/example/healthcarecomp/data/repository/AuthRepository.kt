package com.example.healthcarecomp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.util.Resource

interface AuthRepository {

    fun getLoggedInUser(): User?

    fun isLoggedIn(): Boolean

    var currentUser: User?
    suspend fun loginByEmail(email: String): Resource<User>

    suspend fun loginByPhone(phone: String, password: String): Resource<User>

    suspend fun getUserById(userId: String, listener: (Resource<User?>) -> Unit)

    suspend fun signup(
        phone: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String,
        doctorCode: String?
    ): Resource<User>

    suspend fun onUserChange(user: User, listener: (Resource<User>) -> Unit)

    fun logout()

    fun saveUser(user: User)

    fun removeUser(user: User)

    suspend fun searchUsersByName(name: String, listener: (MutableLiveData<Resource<MutableList<User>>>) -> Unit)
}