package com.example.healthcarecomp.ui.user.login

import com.example.healthcarecomp.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase
) : BaseViewModel() {

}