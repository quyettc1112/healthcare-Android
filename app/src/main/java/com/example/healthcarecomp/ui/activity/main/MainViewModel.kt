package com.example.healthcarecomp.ui.activity.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
): BaseViewModel(){
    var currentUser = MutableLiveData<User?>()

    init {
        val user = authRepository.getLoggedInUser()
        onUserChange(user)
    }

    private fun onUserChange(user: User?) = viewModelScope.launch{
        authRepository.onUserChange(user!!){
            when (it) {
                is Resource.Error -> Log.i("Main view model", it.message!!)
                is Resource.Success -> {
                    currentUser.value = it.data
                    Log.i("Main view model","user update")
                }
                else ->  Log.i("Main view model","unknown error")
            }
        }
    }



}