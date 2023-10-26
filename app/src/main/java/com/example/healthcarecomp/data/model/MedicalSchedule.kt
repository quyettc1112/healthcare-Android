package com.example.healthcarecomp.data.model

import java.util.Date

data class MedicalSchedule(
    val doctorId: Int,
    val patientID: Int,
    val date_medical_examinaton: Date,
    val status_medical_schedule: String?
)
