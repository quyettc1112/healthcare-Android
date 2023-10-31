package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val firebaseRef: DatabaseReference
) : ScheduleRepository {

    // Tạo dbRef
    private val _dbRef = firebaseRef.child(Constant.SCHEDULE_TBL)
    private var _onChildAddedListener: ((Resource<MutableList<Schedule>>) -> Unit)? = null


    override suspend fun upsert(schedule: Schedule): Resource<Schedule> {
        var result: Resource<Schedule> = Resource.Loading()
        _dbRef
            .child(schedule.id!!)
            .setValue(schedule)
            .addOnCompleteListener {
                result = Resource.Success(schedule)
            }
            .addOnFailureListener {
                result = Resource.Error(it.message)
            }
        return result
    }

    override suspend fun remove(schedule: Schedule): Resource<Schedule> {
        var result: Resource<Schedule> = Resource.Loading()
        _dbRef
            .child(schedule.id!!)
            .removeValue()
            .addOnCompleteListener {
                result = Resource.Success(schedule)
            }
            .addOnFailureListener {
                result = Resource.Error(it.message)
            }
        return result
    }
    override fun getScheduleByPatientID(patientID: String, listener: (Resource<MutableList<Schedule>>) -> Unit) {
        val query: Query = _dbRef.orderByChild("patientID").equalTo("0fc296b0-945d-4cb1-b06f-82241b1fc1ba")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Schedule>()
                snapshot.children.forEach {data ->
                    val schedule = data.getValue(Schedule::class.java)
                    schedule?.let {
                        list.add(it)
                    }
                }
                listener?.let {
                    it(Resource.Success(list))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })

    }



    override fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit) {
        _onChildAddedListener = listener
        onDataChange()
    }

    private fun onDataChange() {
        _dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Schedule>()
                snapshot.children.forEach { data ->
                    val mr = data.getValue(Schedule::class.java)
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