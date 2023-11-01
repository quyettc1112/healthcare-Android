package com.example.healthcarecomp.ui.medicalhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val medicalHistoryUseCase: MedicalHistoryUseCase,
    private val authRepository: AuthRepository
): BaseViewModel() {
    var medicalHistoryList = MutableLiveData<Resource<MutableList<MedicalRecord>>>()
    var medicalAdded = MutableLiveData<Resource<MedicalRecord>>()
    var doctorList = HashMap<String?, Doctor?>()
    var currentUser = MutableLiveData<User?>(authRepository.getLoggedInUser())

    lateinit var  patientId: String
    operator fun invoke(patientId: String){
        this.patientId = patientId
        loadMedicalRecord()
    }

    fun loadMedicalRecord(){
        viewModelScope.launch {
            medicalHistoryList.value = Resource.Loading()
            medicalHistoryUseCase.getAllByPatientID(patientId) {
                if(it is Resource.Success){
                    val data = it.data
                    data?.forEach {record ->
                        doctorList.putIfAbsent(record.doctorId, null)
                        Log.i("log",record.toString())
                    }
                    runBlocking {
                        medicalHistoryUseCase.getDoctorById(doctorList){
                            doctorList = it
                        }
                    }
                }
                medicalHistoryList.value = it

            }

        }
    }

    fun upsertMedialRecord(medicalRecord: MedicalRecord) = viewModelScope.launch {
        medicalAdded.value = medicalHistoryUseCase.upsertMedicalRecord(medicalRecord)
    }


}