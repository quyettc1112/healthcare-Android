package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.util.Resource

interface MedicalHistoryRepository {
    suspend fun upsert(medicalRecord: MedicalRecord): Resource<MedicalRecord>
    suspend fun remove(medicalRecord: MedicalRecord): Resource<MedicalRecord>
    fun onDataChange(listener: (Resource<MutableList<MedicalRecord>>) -> Unit)
    fun bindChildEvent()
}