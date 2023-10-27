package com.example.healthcarecomp.ui.user.register

import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {


    suspend fun signup(
        phone: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String,
        doctorCode: String?
    ): Resource<User> {
        return authRepository.signup(phone, password, confirmPassword, firstName, lastName, email, doctorCode)
    }
}