package com.example.healthcarecomp.data.repositoryImpl

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val firebaseRef: DatabaseReference
) : ScheduleRepository {

    // Tạo dbRef
    private val _dbRef = firebaseRef.child(Constant.SCHEDULE_TBL)


    override suspend fun upsert(schedule: Schedule): Resource<Schedule> {
        var result: Resource<Schedule> = Resource.Loading()
        _dbRef
            .child(schedule.id)
            .setValue(schedule)
            .addOnCompleteListener {
                result = Resource.Success(schedule)
            }
            .addOnFailureListener {
                result = Resource.Error(it.message)
            }
        return result
    }

    override fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun bindChildEvent() {
        TODO("Not yet implemented")
    }
}