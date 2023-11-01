package com.example.healthcarecomp.data.model

import java.io.Serializable
import java.util.Date
import java.util.UUID

data class MedicalRecord(
    val doctorId: String? = null,
    val patientId: String? = null,
    val bodyTemperature: Float? = null,
    val height: Float? = null,
    val weight: Float? = null,
    val bloodPressure: String? = null,
    val bloodSugar: Int? = null,
    val hearthRate: Int? = null,
    val general: String? = null,
    val date: Date? = null,
    val id: String = UUID.randomUUID().toString()
): Serializable
