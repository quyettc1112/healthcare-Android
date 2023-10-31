package com.example.healthcarecomp.ui.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    // Import use case for Viewmodel
    private val scheduleUseCase: ScheduleUseCase
) : BaseViewModel() {

    // Create a list of schedule today
    var scheduleListToday = MutableLiveData<Resource<MutableList<Schedule>>>()
    var scheduleListUpComing = MutableLiveData<Resource<MutableList<Schedule>>>()
    var scheduleAdd = MutableLiveData<Resource<Schedule>>()

    // hàm khởi tạo
    init {
        scheduleListToday.value = Resource.Loading()
        loadTodayScheduleByPatientID()
        scheduleUseCase.onDataChange {
            update(it)
        }

    }

    fun loadTodayScheduleByPatientID() {
        viewModelScope.launch {
            scheduleListUpComing.value = Resource.Loading()
            scheduleUseCase.getAllScheduleByPatientID("1a04ee07-5909-4471-b767-a62f8c1e99d1"){
                scheduleListUpComing.value = it

            }
        }
    }

    fun update(data : Resource<MutableList<Schedule>>) {
        scheduleListToday.value = data
        //scheduleListUpComing.value = data
    }

    fun upsertSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleAdd.value = scheduleUseCase.upsertSchedule(schedule)
    }

    fun removeSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleUseCase.removeSchedule(schedule)
    }


}