package com.example.healthcarecomp.ui.medicalrecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryUseCase
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalRecordViewModel @Inject constructor(
    private val medicalHistoryUseCase: MedicalHistoryUseCase
) : BaseViewModel() {

    val upsertMedicalRecord = MutableLiveData<Resource<MedicalRecord>>()
    var currentMedicalRecordId: String? = null
    val isEditMode = MutableLiveData<Boolean>()
    var patientId : String? = null
    init {
        isEditMode.value = false
    }

    operator fun invoke() {
        patientId = sessionManager.getMedicalHistoryPatientId()
    }

    fun onItemDataChange(listener: (Resource<MedicalRecord>) -> Unit) {
        currentMedicalRecordId?.let {
            medicalHistoryUseCase.onItemChange(currentMedicalRecordId!!, listener)
        }

    }

    fun upsertMedicalRecord(medicalRecord: MedicalRecord) = viewModelScope.launch{
        upsertMedicalRecord.value = Resource.Loading()
        upsertMedicalRecord.value = medicalHistoryUseCase.upsertMedicalRecord(medicalRecord)
    }

}