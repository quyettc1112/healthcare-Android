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
        TODO("Not yet implemented")
    }

    override fun onDataChange(listener: (Resource<MutableList<Schedule>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun bindChildEvent() {
        TODO("Not yet implemented")
    }
}