package com.example.healthcarecomp.data.repositoryImpl

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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

    override fun getScheduleByPatientPhone(
        phone: String,
        listener: (Resource<MutableList<Schedule>>) -> Unit
    ): Resource<Schedule> {
        TODO("Not yet implemented")
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