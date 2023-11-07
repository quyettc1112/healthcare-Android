package com.example.healthcarecomp.data.repository

import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.util.Resource
import javax.inject.Inject

interface DoctorRepository : BaseRepository<Doctor>{
    suspend fun getDoctorById(doctorKeys: HashMap<String?,Doctor?>, listener: (HashMap<String?, Doctor?>) -> Unit)
    suspend fun getDoctorByIdKey(doctor: Doctor): Resource<Doctor?>


}