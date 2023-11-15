package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.ScheduleRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.Calendar
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

    override fun getScheduleByPatientID_Today(
        patientID: String,
        condition: String,
        listener: (Resource<MutableList<Schedule>>) -> Unit
    ) {
        val query: Query =
            _dbRef.orderByChild("patientID").equalTo(patientID)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Schedule>()
                snapshot.children.forEach { data ->
                    val schedule = data.getValue(Schedule::class.java)
                    val currentDay = Calendar.getInstance()
                    if (Constant.convertTimestampToCalendar(schedule?.date_medical_examinaton!!)
                            .get(Calendar.DAY_OF_YEAR)
                        == currentDay.get(Calendar.DAY_OF_YEAR) && condition.equals("Today")
                    ) {
                        schedule?.let { list.add(it) }
                    }
                }
                listener?.let {
                    list.sortBy { it.date_medical_examinaton }
                    it(Resource.Success(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }
        })
    }

    override fun getScheduleByPatientID_UpComing(
        patientID: String,
        condition: String,
        listener: (Resource<MutableList<Schedule>>) -> Unit
    ) {
        val query: Query =
            _dbRef.orderByChild("patientID").equalTo(patientID)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Schedule>()
                snapshot.children.forEach { data ->
                    val schedule = data.getValue(Schedule::class.java)
                    val currentDay = Calendar.getInstance()
                    if (Constant.convertTimestampToCalendar(schedule?.date_medical_examinaton!!)
                            .get(Calendar.DAY_OF_YEAR)
                        > currentDay.get(Calendar.DAY_OF_YEAR) && condition.equals("UpComing")
                    ) {
                        schedule?.let { list.add(it) }
                    }
                }
                listener?.let {
                    list.sortBy { it.date_medical_examinaton }
                    it(Resource.Success(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }

    override fun getScheduleByDoctorID_Today(
        DoctorID: String,
        condition: String,
        listener: (Resource<MutableList<Schedule>>) -> Unit
    ) {
        val query: Query =
            _dbRef.orderByChild("doctorId").equalTo(DoctorID)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Schedule>()
                snapshot.children.forEach { data ->
                    val schedule = data.getValue(Schedule::class.java)
                    val currentDay = Calendar.getInstance()
                    if (Constant.convertTimestampToCalendar(schedule?.date_medical_examinaton!!)
                            .get(Calendar.DAY_OF_YEAR)
                        == currentDay.get(Calendar.DAY_OF_YEAR) && condition.equals("Today")
                    ) {
                        schedule?.let { list.add(it) }
                    }
                }
                listener?.let {
                    list.sortBy { it.date_medical_examinaton }
                    it(Resource.Success(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }

    override fun getScheduleByDoctorID_UpComing(
        DoctorID: String,
        condition: String,
        listener: (Resource<MutableList<Schedule>>) -> Unit
    ) {
        val query: Query =
            _dbRef.orderByChild("doctorId").equalTo(DoctorID)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Schedule>()
                snapshot.children.forEach { data ->
                    val schedule = data.getValue(Schedule::class.java)
                    val currentDay = Calendar.getInstance()
                    if (Constant.convertTimestampToCalendar(schedule?.date_medical_examinaton!!)
                            .get(Calendar.DAY_OF_YEAR)
                        > currentDay.get(Calendar.DAY_OF_YEAR) && condition.equals("UpComing")
                    ) {
                        schedule?.let { list.add(it) }
                    }
                }
                listener?.let {
                    list.sortBy { it.date_medical_examinaton }
                    it(Resource.Success(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }

    override suspend  fun getAllByPatientID(
        patientID: String,
        listener: (Resource<MutableList<Patient>>) -> Unit
    ) {
        val query = _dbRef.orderByChild("patientID").equalTo(patientID)
        fetchData(query, listener)
    }
    private fun fetchData(query: Query, listener: (Resource<MutableList<Patient>>) -> Unit){
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Patient>()
                snapshot.children.forEach {data ->
                    val mr = data.getValue(Patient::class.java)
                    mr?.let {
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