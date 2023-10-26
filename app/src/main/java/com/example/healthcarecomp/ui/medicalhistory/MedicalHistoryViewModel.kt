package com.example.healthcarecomp.ui.medicalhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val medicalHistoryUseCase: MedicalHistoryUseCase
): BaseViewModel() {
    var medicalHistoryList = MutableLiveData<Resource<MutableList<MedicalRecord>>>()
    var medicalAdded = MutableLiveData<Resource<MedicalRecord>>()
    init {
        medicalHistoryList.value = Resource.Loading()
        medicalHistoryUseCase.onDataChange {
            update(it)
        }
    }

    fun update(data: Resource<MutableList<MedicalRecord>>){
        medicalHistoryList.value = data
    }

    fun upsertMedialRecord(medicalRecord: MedicalRecord) = viewModelScope.launch {
        medicalAdded.value = medicalHistoryUseCase.upsertMedicalRecord(medicalRecord)
    }


}