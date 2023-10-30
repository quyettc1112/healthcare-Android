package com.example.healthcarecomp.ui.auth.register

import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val validFormFlow = MutableStateFlow<Boolean>(false)

    fun register(
        phone: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String,
        doctorCode: String?
    ) {
        viewModelScope.launch {
            _registerFlow.value = Resource.Loading()
            val result = registerUseCase.signup(phone, password, confirmPassword, firstName, lastName, email, doctorCode)
            _registerFlow.value = result
        }
    }
}