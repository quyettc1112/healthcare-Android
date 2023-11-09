package com.example.healthcarecomp.util.extension

import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.User

fun User.isDoctor(): Boolean{
    return this is Doctor
}

fun User.isPatient(): Boolean{
    return this is Patient
}