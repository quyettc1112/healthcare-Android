package com.example.healthcarecomp.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<User>?>(null)
    val loginFLow: StateFlow<Resource<User>?> = _loginFlow

    val isBiometricSuccess = MutableStateFlow<Boolean>(false)

    fun getLoggedInUser() = loginUseCase.getLoggedInUser()

    val loginGGFlow = MutableStateFlow<Resource<User>?>(null)


    fun autoLogin(){
        getLoggedInUser()?.let {
            if(isBiometricSuccess.value){
                _loginFlow.value = Resource.Success(getLoggedInUser()!!)
            }
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