package com.example.healthcarecomp.ui.schedule

import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryUseCase
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.extension.isDoctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Condition
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    // Import use case for Viewmodel
    private val scheduleUseCase: ScheduleUseCase,
    private val authRepository: AuthRepository,

    //Do trong MedicalRecordHistory có hàm để lấy danh sách các bác sĩ

) : BaseViewModel() {

    // Create a list of schedule today
    var scheduleListToday = MutableLiveData<Resource<MutableList<Schedule>>>()
    var scheduleListUpComing = MutableLiveData<Resource<MutableList<Schedule>>>()
    var scheduleAdd = MutableLiveData<Resource<Schedule>>()
    var patientID = authRepository.getLoggedInUser()?.id
    var doctorLIst =  MutableLiveData<Resource<MutableList<Doctor>>>()

    // hàm khởi tạo
    init {
        loadTodayScheduleByPatientID(patientID!!, "Today")
        loadUpComingcheduleByPatientID(patientID!!, "UpComing")

    }

    fun loadTodayScheduleByPatientID(patientID : String, condition: String) {
        viewModelScope.launch {
            if (condition.equals("Today")) {
                scheduleListToday.value = Resource.Loading()
                scheduleUseCase.getAllScheduleByPatientID_Today(patientID, condition){
                    scheduleListToday.value = it
                }
            }
        }
    }
    fun getListToday(): List<Schedule>? {
        return scheduleListToday.value?.data?.toList()

    }

    fun getListTodayUPComing(): List<Schedule>?{
        return scheduleListUpComing.value?.data?.toList()

    }

    fun loadUpComingcheduleByPatientID(patientID : String, condition: String){
        viewModelScope.launch {
            if (condition.equals("UpComing")) {
                scheduleListUpComing.value = Resource.Loading()
                scheduleUseCase.getAllScheduleByPatientID_UpComing(patientID, condition){
                    scheduleListUpComing.value = it
                }
            }
        }
    }

    fun upsertSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleAdd.value = scheduleUseCase.upsertSchedule(schedule)
    }

    fun removeSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleUseCase.removeSchedule(schedule)
    }

    fun getAllDoctor(){
        viewModelScope.launch {
            scheduleUseCase.getAllDoctor{
                doctorLIst.value = it
            }
        }

    }


}