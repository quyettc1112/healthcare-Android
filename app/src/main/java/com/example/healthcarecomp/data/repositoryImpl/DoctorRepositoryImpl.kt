package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.repository.DoctorRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val fireBaseDatabase: FirebaseDatabase
) : DoctorRepository{
    private val _doctorRef = fireBaseDatabase.reference.child(Constant.DoctorQuery.PATH.queryField)

    override suspend fun getDoctorById(doctorKeys: HashMap<String?, Doctor?>,listener: (HashMap<String?, Doctor?>) -> Unit) {
        doctorKeys.forEach {
            val doctor = _doctorRef.child(it.key!!).get().await().getValue(Doctor::class.java)
            doctorKeys[it.key] = doctor
            listener(doctorKeys)
        }


    }

}