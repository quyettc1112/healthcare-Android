package com.example.healthcarecomp.ui.medicalhistory

import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.data.repositoryImpl.MedicalHistoryRepositoryImpl
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

class MedicalHistoryUseCase @Inject constructor(
    private val medicalHistoryRepository: MedicalHistoryRepository,
    private val doctorRepository: DoctorRepository
)  {
    suspend fun upsertMedicalRecord(medicalRecord: MedicalRecord): Resource<MedicalRecord>{
       return medicalHistoryRepository.upsert(medicalRecord)
    }

    suspend fun removeMedicalRecord(medicalRecord: MedicalRecord): Resource<MedicalRecord> {
        return medicalHistoryRepository.remove(medicalRecord)
    }


    suspend fun getAllByDoctorID(doctorID: String, listener: (Resource<MutableList<MedicalRecord>>) -> Unit){
        medicalHistoryRepository.getAllByDoctorID(doctorID,listener)
    }

    suspend fun getAllByPatientID(patientID: String, listener: (Resource<MutableList<MedicalRecord>>) -> Unit) {
        medicalHistoryRepository.getAllByPatientID(patientID, listener)
    }

    suspend fun getAll(listener: (Resource<MutableList<MedicalRecord>>) -> Unit){
        medicalHistoryRepository.getAll(listener)
    }

     fun onItemChange(itemId: String, listener: (Resource<MedicalRecord>) -> Unit) {
        medicalHistoryRepository.onItemChange(itemId, listener)
    }

    suspend fun getDoctorById(doctorKeys: (HashMap<String?, Doctor?>),listener: (HashMap<String?, Doctor?>) -> Unit){
        doctorRepository.getDoctorById(doctorKeys,listener)
    }
}