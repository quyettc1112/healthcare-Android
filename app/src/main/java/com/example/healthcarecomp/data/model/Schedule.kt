package com.example.healthcarecomp.data.model

import java.util.Date
import java.util.UUID

data class Schedule(
    var id: String? = UUID.randomUUID().toString(),
    val doctorId: Int? = null,
    val patientID: Int? = null,
    val date_medical_examinaton: Date? = null,
    val status_medical_schedule: String? = null
){
  //  constructor()
}
