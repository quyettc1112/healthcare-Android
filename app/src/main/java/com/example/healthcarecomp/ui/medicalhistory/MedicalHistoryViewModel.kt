package com.example.healthcarecomp.ui.medicalhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val medicalHistoryUseCase: MedicalHistoryUseCase
): BaseViewModel() {
    var medicalHistoryList = MutableLiveData<Resource<MutableList<MedicalRecord>>>()
    var medicalAdded = MutableLiveData<Resource<MedicalRecord>>()
    init {
        loadMedicalRecord()
    }

    fun loadMedicalRecord(){
        viewModelScope.launch {
            medicalHistoryList.value = Resource.Loading()
            medicalHistoryUseCase.getAllByPatientID("2222222") {
                medicalHistoryList.value = it
            }
        }
    }

    fun upsertMedialRecord(medicalRecord: MedicalRecord) = viewModelScope.launch {
        medicalAdded.value = medicalHistoryUseCase.upsertMedicalRecord(medicalRecord)
    }


}