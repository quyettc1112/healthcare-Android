package com.example.healthcarecomp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.healthcarecomp.data.database.dao.DoctorDao
import com.example.healthcarecomp.data.database.entity.DoctorEntity


@Database(entities = [DoctorEntity::class],version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun doctorDao(): DoctorDao
}