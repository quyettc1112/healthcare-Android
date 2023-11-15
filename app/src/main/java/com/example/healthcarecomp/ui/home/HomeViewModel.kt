package com.example.healthcarecomp.ui.home

import com.example.healthcarecomp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    fun setMedicalHistoryPatientId(patientId: String){
        sessionManager.setMedicalHistoryPatientId(patientId)
    }
}