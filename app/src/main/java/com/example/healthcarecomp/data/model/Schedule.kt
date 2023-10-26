package com.example.healthcarecomp.data.model

import java.util.Date
import java.util.UUID

data class Schedule(
    val id: String = UUID.randomUUID().toString(),
    val doctorId: Int,
    val patientID: Int,
    val date_medical_examinaton: Date,
    val status_medical_schedule: String?
)
