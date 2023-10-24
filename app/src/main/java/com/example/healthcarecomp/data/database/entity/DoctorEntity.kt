package com.example.healthcarecomp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Doctor")
data class DoctorEntity (
    @PrimaryKey val id: Int,
    val phone: Int,
    var password: String,
    var firstName: String,
    var lastName: String
)
