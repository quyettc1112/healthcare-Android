package com.example.healthcarecomp.data.repository


import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.util.Resource

interface ScheduleRepository {

    suspend fun upsert(schedule: Schedule): Resource<Schedule>

    suspend fun remove(schedule: Schedule): Resource<Schedule>

    fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit)

    fun bindChildEvent()
}