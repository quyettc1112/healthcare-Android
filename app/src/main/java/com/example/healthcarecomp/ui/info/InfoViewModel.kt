package com.example.healthcarecomp.ui.info

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.ImageRepository
import com.example.healthcarecomp.data.repository.PatientRepository
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.extension.isDoctor
import com.example.healthcarecomp.util.extension.isPatient
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val doctorRepository: DoctorRepository,
    val patientRepository: PatientRepository,
    val imageRepository: ImageRepository
) : BaseViewModel() {

    var isEditing = MutableLiveData<Boolean>(false)

    var userEditState = MutableLiveData<Resource<out User>>()

    fun upsertUser(
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        avatar: String?,
        gender: Boolean,
        dob: Long? = null,
    ) {
        userEditState.value = Resource.Loading()
        if (confirmPassword != password) {
            userEditState.value = Resource.Error("Confirm pass word does not match")
        }
        authRepository.getLoggedInUser()?.let {
            if (it.isDoctor()) {
                Log.d("UserRole","Doctor")
                it.password = password
                it.firstName = firstName
                it.lastName = lastName
                it.avatar = avatar
                it.gender = gender
                it.dob = dob
                viewModelScope.launch {
                    userEditState.postValue(doctorRepository.upsert(it as Doctor, it.id))
                }
            } else if (it.isPatient()) {
                Log.d("UserRole","Patient")
                it.password = password
                it.firstName = firstName
                it.lastName = lastName
                it.avatar = avatar
                it.gender = gender
                it.dob = dob
                viewModelScope.launch {
                    userEditState.postValue(patientRepository.upsert(it as Patient, it.id))
                }
            } else {
                userEditState.value = Resource.Error("Update user info unsuccessfully")
            }
        }
    }

    fun logout() {
        stopReceiveNotification()
        authRepository.logout()
    }

    fun stopReceiveNotification(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(sessionManager.getLoggedInUser()?.id!!)
    }

    fun uploadImage(
        imageUri: Uri,
        onSuccess: ((uri: Uri) -> Unit),
        onFailure: ((errorMessage: String) -> Unit),
        onProgress: (() -> Unit)
    ) {
        viewModelScope.launch {
            imageRepository.uploadImage(imageUri, onSuccess, onFailure, onProgress)
        }
    }

}