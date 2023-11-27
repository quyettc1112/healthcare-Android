package com.example.healthcarecomp.ui.schedule

import android.content.Context
import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryUseCase
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.extension.isDoctor
import com.example.healthcarecomp.util.extension.isPatient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.locks.Condition
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    // Import use case for Viewmodel
    val scheduleUseCase: ScheduleUseCase,
    private val authRepository: AuthRepository,

    //Do trong MedicalRecordHistory có hàm để lấy danh sách các bác sĩ

) : BaseViewModel() {

    // Create a list of schedule today
    var scheduleListToday = MutableLiveData<Resource<MutableList<Schedule>>>()
    var scheduleListUpComing = MutableLiveData<Resource<MutableList<Schedule>>>()
    var scheduleAdd = MutableLiveData<Resource<Schedule>>()
    var currentUserID = authRepository.getLoggedInUser()?.id
    var currentUser = authRepository.getLoggedInUser()
    var doctorLIst =  MutableLiveData<Resource<MutableList<Doctor>>>()
    var patientLIst =  MutableLiveData<Resource<MutableList<Patient>>>()

    // hàm khởi tạo
    init {

        loadTodayScheduleByPatientID(currentUserID!!, "Today")
        loadUpComingcheduleByPatientID(currentUserID!!, "UpComing")

    }

    fun loadTodayScheduleByPatientID(currentUserID : String, condition: String) {
        viewModelScope.launch {
            scheduleListToday.value = Resource.Loading()
            if (currentUser!!.isPatient()) {
                if (condition.equals("Today")) {
                    scheduleUseCase.getAllScheduleByPatientID_Today(currentUserID, condition){
                        scheduleListToday.value = it
                    }
                }
            } else {
                if (condition.equals("Today")) {
                    scheduleUseCase.getAllScheduleByDoctorID_Today(currentUserID, condition){
                        scheduleListToday.value = it
                    }
                }
            }
        }
    }
    fun loadUpComingcheduleByPatientID(currentUserID : String, condition: String){
        viewModelScope.launch {
            scheduleListUpComing.value = Resource.Loading()
            if (currentUser!!.isPatient()) {
                if (condition.equals("UpComing")) {
                    scheduleUseCase.getAllScheduleByPatientID_UpComing(currentUserID, condition){
                        scheduleListUpComing.value = it
                    }
                }
            } else {
                if (condition.equals("UpComing")) {
                    scheduleUseCase.getAllScheduleByDoctorID_UpComing(currentUserID, condition){
                        scheduleListUpComing.value = it
                    }
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

    fun getListDoctor() : List<Doctor>? {
        return doctorLIst.value?.data?.toList()
    }

    fun getListPatient() : List<Patient> ? {
        return patientLIst.value?.data?.toList()
    }
    fun upsertSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleAdd.value = scheduleUseCase.upsertSchedule(schedule)
    }

    fun removeSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleUseCase.removeSchedule(schedule)
    }


    fun getAllDoctor(){
        GlobalScope.launch() {
            scheduleUseCase.getAllDoctor{
                doctorLIst.value = it
            }
        }
    }
    fun getAllPatient(){
        GlobalScope.launch() {
            scheduleUseCase.getAllPatient{
                patientLIst.value = it
            }
        }
    }



}