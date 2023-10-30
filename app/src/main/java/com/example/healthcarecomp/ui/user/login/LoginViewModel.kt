package com.example.healthcarecomp.ui.user.login

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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<User>?>(null)
    val loginFLow: StateFlow<Resource<User>?> = _loginFlow

    val loginGGFlow = MutableStateFlow<Resource<User>?>(null)

    val currentUser: User? = loginUseCase.currentUser

    init {
        currentUser?.let {
            _loginFlow.value = Resource.Success(currentUser)
        }
    }

    fun loginByPhone(phone: String, password: String) =
        viewModelScope.launch {
            _loginFlow.value = Resource.Loading()
            val result = loginUseCase.loginByPhone(phone, password)
            _loginFlow.value = result
        }

    fun loginByMail(email:String) = viewModelScope.launch {
        loginGGFlow.value = Resource.Loading()
        val result = loginUseCase.loginByMail(email)
        loginGGFlow.value = result
    }

}