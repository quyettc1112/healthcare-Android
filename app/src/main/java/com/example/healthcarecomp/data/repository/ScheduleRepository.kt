package com.example.healthcarecomp.data.repository


import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.util.Resource
import java.util.concurrent.locks.Condition

interface ScheduleRepository {

    suspend fun upsert(schedule: Schedule): Resource<Schedule>

    suspend fun remove(schedule: Schedule): Resource<Schedule>

    fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit)

    fun bindChildEvent()

    fun  getScheduleByPatientID_Today(patientID : String, condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit)
    fun  getScheduleByPatientID_UpComing(patientID : String, condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit)

    fun  getScheduleByDoctorID_Today(DoctorID : String, condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit)
    fun  getScheduleByDoctorID_UpComing(DoctorID: String, condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit)
}