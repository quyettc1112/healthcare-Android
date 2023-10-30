package com.example.healthcarecomp.ui.schedule

import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import javax.inject.Inject

// Inject một cái repository
class ScheduleUseCase @Inject  constructor(
    private val scheduleRepository : ScheduleRepository

) {

    suspend fun upsertSchedule(schedule: Schedule): Resource<Schedule> {
        return scheduleRepository.upsert(schedule)
    }

    fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit) {
        scheduleRepository.onDataChange(listener)
    }


}