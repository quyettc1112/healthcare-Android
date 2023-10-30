package com.example.healthcarecomp.ui.auth.login

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

    fun getLoggedInUser() = authRepository.getLoggedInUser()

    suspend fun loginByMail(email: String): Resource<User> {
        return  authRepository.loginByEmail(email)
    }


}