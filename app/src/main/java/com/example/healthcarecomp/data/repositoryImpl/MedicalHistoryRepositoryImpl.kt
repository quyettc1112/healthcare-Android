package com.example.healthcarecomp.data.repositoryImpl

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.repository.MedicalHistoryRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class MedicalHistoryRepositoryImpl @Inject constructor(
    private val firebaseRef: DatabaseReference
) : MedicalHistoryRepository {
    private var _onChildAddedListener: ((Resource<MutableList<MedicalRecord>>) -> Unit)? = null
    private val _dbRef = firebaseRef.child(Constant.MEDICAL_HISTORY_TBL)

    override suspend fun upsert(medicalRecord: MedicalRecord): Resource<MedicalRecord> {
        var result : Resource<MedicalRecord> =  Resource.Loading()
        _dbRef
            .child(medicalRecord.id)
            .setValue(medicalRecord)
            .addOnCompleteListener {
                result = Resource.Success(medicalRecord)
            }.addOnFailureListener {
                result = Resource.Error(it.message)
            }
        return result
    }

    override suspend fun remove(medicalRecord: MedicalRecord): Resource<MedicalRecord> {
        var result : Resource<MedicalRecord> =  Resource.Loading()
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

    override fun onDataChange(listener: (Resource<MutableList<MedicalRecord>>) -> Unit) {
        _onChildAddedListener = listener
        onDataChange()
    }

    private fun onDataChange(){
        val list = mutableListOf<MedicalRecord>()
        _dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {data ->
                    val mr = data.getValue(MedicalRecord::class.java)
                    mr?.let {
                        list.add(it)
                    }
                    _onChildAddedListener?.let { it(Resource.Success(list)) }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun bindChildEvent() {
        _dbRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                onDataChange()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    onDataChange()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                onDataChange()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}