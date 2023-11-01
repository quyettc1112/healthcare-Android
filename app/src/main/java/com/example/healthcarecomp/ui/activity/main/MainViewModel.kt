package com.example.healthcarecomp.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository
) : BaseViewModel(){
    private var _user = MutableLiveData<User?>(authRepository.getLoggedInUser())
    val user: LiveData<User?> = _user
}