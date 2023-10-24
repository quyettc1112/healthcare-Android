package com.example.healthcarecomp.ui.user.register

import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    val currentUser: FirebaseUser?
        get() = authRepository.currentUser
    suspend fun signup(email: String, password: String, confirmPassword: String): Resource<FirebaseUser> {
        return authRepository.signup(email, password, confirmPassword)
    }
}