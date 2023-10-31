package com.example.healthcarecomp.data.model

import java.sql.Time
import java.util.Date
import java.util.UUID

data class Schedule(
    var id: String? = UUID.randomUUID().toString(),
    val doctorId: String? = null,
    val patientID: String? = null,
    val date_medical_examinaton: Long? = null,
    val status_medical_schedule: String? = null,
    val note: String? = null
){
  //  constructor()
}
