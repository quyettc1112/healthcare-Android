package com.example.healthcarecomp.ui.schedule

import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    // Import use case for Viewmodel
    private val scheduleUseCase: ScheduleUseCase
) : BaseViewModel() {

    // Create a list of schedule today
    var scheduleListToday = MutableLiveData<Resource<MutableList<Schedule>>>()

    // hàm khởi tạo
    init {
        scheduleListToday.value = Resource.Loading()
        scheduleUseCase.onDataChange {
            update(it)
        }
    }
    fun update(data : Resource<MutableList<Schedule>>) {
        scheduleListToday.value = data
    }

}