package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class MedicalHistoryRepositoryImpl @Inject constructor(
    private val firebaseRef: DatabaseReference
) : MedicalHistoryRepository {
    private val _dbRef = firebaseRef.child(Constant.MEDICAL_HISTORY_TBL)

    override suspend fun upsert(medicalRecord: MedicalRecord): Resource<MedicalRecord> {
        var result : Resource<MedicalRecord> =  Resource.Unknown()
        _dbRef
            .child(medicalRecord.id)
            .setValue(medicalRecord)
            .addOnCompleteListener {
                result = Resource.Success(medicalRecord)
            }.addOnFailureListener {
                result = Resource.Error(it.message)
            }
        Log.e("lol", "upsert")
        return result
    }

    override suspend fun remove(medicalRecord: MedicalRecord): Resource<MedicalRecord> {
        var result : Resource<MedicalRecord> =  Resource.Unknown()
        _dbRef
            .child(medicalRecord.id)
            .removeValue()
            .addOnCompleteListener {
                result = Resource.Success(medicalRecord)
            }.addOnFailureListener {
                result = Resource.Error(it.message)
            }
        return result
    }

    override fun onItemChange(
        itemId: String,
        listener: (Resource<MedicalRecord>) -> Unit
    ) {
        val query = _dbRef.child(itemId)
        itemFetchData(query, listener)
    }


    override suspend fun getAll(listener: (Resource<MutableList<MedicalRecord>>) -> Unit) {
        val query = _dbRef
        fetchData(query, listener)
    }

    override suspend fun getAllByDoctorID(
        doctorID: String,
        listener: (Resource<MutableList<MedicalRecord>>) -> Unit
    ) {
        val query = _dbRef.orderByChild("doctorId").equalTo(doctorID)
        fetchData(query, listener)
    }

    override suspend  fun getAllByPatientID(
        patientID: String,
        listener: (Resource<MutableList<MedicalRecord>>) -> Unit
    ) {
        val query = _dbRef.orderByChild("patientId").equalTo(patientID)
        fetchData(query, listener)
    }

    private fun itemFetchData(query: Query, listener: (Resource<MedicalRecord>) -> Unit){
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(MedicalRecord::class.java)
                listener?.let {
                   item?.let {
                       listener(Resource.Success(it))
                       Log.i("oi", "${item.toString()}")
                   }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }

    private fun fetchData(query: Query, listener: (Resource<MutableList<MedicalRecord>>) -> Unit){
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MedicalRecord>()
                snapshot.children.forEach {data ->
                    val mr = data.getValue(MedicalRecord::class.java)
                    mr?.let {
                        list.add(it)
                    }

                }
                listener?.let {
                    list.sortByDescending {
                        it.timestamps
                    }
                    it(Resource.Success(list))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }



}