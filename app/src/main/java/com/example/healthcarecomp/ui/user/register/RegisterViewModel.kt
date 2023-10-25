package com.example.healthcarecomp.ui.user.register

import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : BaseViewModel() {


    private val _registerFlow = MutableStateFlow<Resource<User>?>(null)
    val registerFlow: StateFlow<Resource<User>?> = _registerFlow

//    fun register(email: String, password: String, confirmPassword: String) {
//        viewModelScope.launch {
//            _registerFlow.value = Resource.Loading()
//            val result = registerUseCase.signup(email, password, confirmPassword)
//            _registerFlow.value = result
//        }
//    }

    fun register(
        phone: Int,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String
    ) {
        viewModelScope.launch {
            _registerFlow.value = Resource.Loading()
//            delay(2000L)
            val result = registerUseCase.signup(phone, password, confirmPassword, firstName, lastName, email)
            _registerFlow.value = result
        }
    }
}