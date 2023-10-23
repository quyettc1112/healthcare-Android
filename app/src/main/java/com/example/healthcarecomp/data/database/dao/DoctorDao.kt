package com.example.healthcarecomp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.healthcarecomp.data.database.entity.DoctorEntity

@Dao
interface DoctorDao {

    @Query("select * from doctor")
    fun getAll(): List<DoctorEntity>

}