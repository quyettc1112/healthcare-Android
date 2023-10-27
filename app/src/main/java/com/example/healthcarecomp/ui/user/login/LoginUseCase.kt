package com.example.healthcarecomp.ui.user.login

import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun loginByPhone(phone: String, password: String): Resource<User> {
        return authRepository.loginByPhone(phone, password)
    }

    val currentUser: User? = authRepository.currentUser

}