package com.example.healthcarecomp.data.model

import java.time.LocalDate
import java.util.UUID

class Doctor(
    phone: String? = null,
    email: String? = null,
    password: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    avatar: String? = null,
    gender: Boolean? = null,
    dob: LocalDate? = null,
    val specialty: String? = null
) : User(phone, email, password, firstName, lastName, avatar, gender, dob){

    override fun toString(): String {
        return "Doctor(phone=${super.phone}, email=${super.email}, " +
                "password=${super.password}, firstName=${super.firstName}, " +
                "lastName=${super.lastName}, avatar=${super.avatar}, " +
                "gender=${super.gender}, dob=${super.dob}, " +
                "specialty=$specialty)"
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (specialty?.hashCode() ?: 0)
        return result
    }


}