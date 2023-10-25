package com.example.healthcarecomp.data.model

import java.util.Date
import java.util.UUID

data class MedicalRecord(
    val doctorId: String? = null,
    val patientId: String? = null,
    val bodyTemperature: Float? = null,
    val bMI: Float? = null,
    val bloodPressure: Int? = null,
    val bloodSugar: Int? = null,
    val healthRate: Int? = null,
    val general: String? = null,
    val date: Date? = null,
    val id: String = UUID.randomUUID().toString()
)
