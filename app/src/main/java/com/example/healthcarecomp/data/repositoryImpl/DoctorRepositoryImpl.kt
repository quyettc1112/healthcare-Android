package com.example.healthcarecomp.data.repositoryImpl

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    override val firebaseRef: DatabaseReference
) : BaseRepositoryImpl<Doctor>(firebaseRef), DoctorRepository {
    private val _doctorRef = firebaseRef.child(Constant.DoctorQuery.PATH.queryField)

    init {
        invoke(
            Constant.DoctorQuery.PATH.queryField, Doctor::class.java
        )
    }

    override suspend fun getDoctorById(
        doctorKeys: HashMap<String?, Doctor?>,
        listener: (HashMap<String?, Doctor?>) -> Unit
    ) {
        doctorKeys.forEach {
            val doctor = _doctorRef.child(it.key!!).get().await().getValue(Doctor::class.java)
            doctorKeys[it.key] = doctor
            listener(doctorKeys)
        }


    }

}