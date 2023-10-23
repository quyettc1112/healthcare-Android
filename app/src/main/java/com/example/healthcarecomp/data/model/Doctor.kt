package com.example.healthcarecomp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctor")
data class Doctor(@PrimaryKey val id: Int)