package com.example.healthcarecomp.ui.user.login

import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import java.net.PasswordAuthentication
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return authRepository.login(email, password)
    }

    val currentUser: FirebaseUser? = authRepository.currentUser

}