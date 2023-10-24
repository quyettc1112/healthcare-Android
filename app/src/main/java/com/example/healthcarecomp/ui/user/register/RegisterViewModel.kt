package com.example.healthcarecomp.ui.user.register

import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.ui.user.login.LoginUseCase
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel() {

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _registerFlow.value = Resource.Loading()
            val result = registerUseCase.signup(email, password, confirmPassword)
            _registerFlow.value = result
        }
    }
}