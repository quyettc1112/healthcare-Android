package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.util.Resource

interface MedicalHistoryRepository {
    suspend fun upsert(medicalRecord: MedicalRecord): Resource<MedicalRecord>
    suspend fun remove(medicalRecord: MedicalRecord): Resource<MedicalRecord>
    fun onDataChange(listener: (Resource<MutableList<MedicalRecord>>) -> Unit)

    suspend fun getAll(listener: (Resource<MutableList<MedicalRecord>>) -> Unit )
    suspend fun getAllByDoctorID(doctorID: String, listener: (Resource<MutableList<MedicalRecord>>) -> Unit )
      fun getAllByPatientID(patientID: String, listener: (Resource<MutableList<MedicalRecord>>) -> Unit )
}