package com.example.healthcarecomp.ui.user.login

import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFLow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    val currentUser: FirebaseUser? = loginUseCase.currentUser

    init {
        currentUser?.let {
            _loginFlow.value = Resource.Success(currentUser)
        }
    }

    fun login(email: String, password: String) =
        viewModelScope.launch {
            _loginFlow.value = Resource.Loading()
            val result = loginUseCase.login(email, password)
            _loginFlow.value = result
        }

}