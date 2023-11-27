package com.example.healthcarecomp.ui.schedule

import android.util.Log
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.data.repository.PatientRepository
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import javax.inject.Inject

// Inject một cái repository
class ScheduleUseCase @Inject  constructor(
    private val scheduleRepository : ScheduleRepository,
    private val doctorRepository: DoctorRepository,
    private val patientRepository: PatientRepository

) {

    suspend fun upsertSchedule(schedule: Schedule): Resource<Schedule> {
        return scheduleRepository.upsert(schedule)
    }

    suspend fun removeSchedule(schedule: Schedule): Resource<Schedule> {
        return scheduleRepository.remove(schedule)
    }

    fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit) {
        scheduleRepository.onDataChange(listener)
    }

    suspend fun getAllScheduleByPatientID_Today(patientID: String,condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit){
        return scheduleRepository.getScheduleByPatientID_Today(patientID,condition, listener)
    }
    suspend fun getAllScheduleByPatientID_UpComing(patientID: String,condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit){
        return scheduleRepository.getScheduleByPatientID_UpComing(patientID,condition, listener)
    }

    suspend fun getAllScheduleByDoctorID_Today(DoctorID: String,condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit){
        return scheduleRepository.getScheduleByDoctorID_Today(DoctorID,condition, listener)
    }
    suspend fun getAllScheduleByDoctorID_UpComing(DoctorID: String,condition: String, listener: (Resource<MutableList<Schedule>>) -> Unit){
        return scheduleRepository.getScheduleByDoctorID_UpComing(DoctorID,condition, listener)
    }


    suspend fun getAllDoctor(listener: (Resource<MutableList<Doctor>>) -> Unit) {
        return doctorRepository.getAll(listener)
    }

    suspend fun getAllPatient( listener: (Resource<MutableList<Patient>>) -> Unit) {
        return patientRepository.getAll(listener)
    }




}