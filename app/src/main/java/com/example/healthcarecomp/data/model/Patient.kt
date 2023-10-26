package com.example.healthcarecomp.data.model

import java.time.LocalDate

class Patient(
    phone: String?,
    email: String?,
    password: String?,
    firstName: String?,
    lastName: String?,
    avatar: String?,
    gender: Boolean?,
    dob: LocalDate?,
    val healthInsurance: Long?
) : User(phone, email, password, firstName, lastName, avatar, gender, dob) {

    override fun toString(): String {
        return "Patient(phone=${super.phone}, email=${super.email}, " +
                "password=${super.password}, firstName=${super.firstName}, " +
                "lastName=${super.lastName}, avatar=${super.avatar}, " +
                "gender=${super.gender}, dob=${super.dob}, " +
                "healthInsurance=$healthInsurance)"
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (healthInsurance?.hashCode() ?: 0)
        return result
    }


}
