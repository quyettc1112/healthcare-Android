package com.example.healthcarecomp.common

class Constant {
    companion object{
        val DEFAULT_ERROR_MESSAGE: String = "An error occurred"
        const val BASE_URL: String = "https://mockapi.io/projects/648fd2c81e6aa71680ca1f63"
        const val APP_DATABASE_NAME = "app_db"
        const val DOCTOR_SECURITY_DOCTOR = "bomaylabacsi"
        const val userSPKey: String = "USER"
    }

    enum class DoctorQuery(val queryField: String){
        PATH("Doctor"),
        EMAIL("email"),
        SPECIALTY("specialty"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENDER("gender"),
        PHONE("phone")
    }

    enum class PatientQuery(val queryField: String){
        PATH("Patient"),
        EMAIL("email"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        GENDER("gender"),
        PHONE("phone")
    }
}