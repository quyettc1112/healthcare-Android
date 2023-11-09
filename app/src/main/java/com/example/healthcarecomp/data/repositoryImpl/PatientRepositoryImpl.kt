package com.example.healthcarecomp.data.repositoryImpl

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.repository.PatientRepository
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    override val firebaseRef: DatabaseReference
) : BaseRepositoryImpl<Patient>(firebaseRef), PatientRepository {

    init {
        invoke(
            Constant.PatientQuery.PATH.queryField, Patient::class.java
        )
    }

}