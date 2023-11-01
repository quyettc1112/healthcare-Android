package com.example.healthcarecomp.ui.info

import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class InfoViewModel @Inject constructor(
    authRepository: AuthRepository
) : BaseViewModel() {
    private val _infoFormState = MutableStateFlow<User?>(null)


}