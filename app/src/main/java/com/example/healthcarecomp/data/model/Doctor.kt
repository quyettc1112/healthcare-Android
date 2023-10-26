package com.example.healthcarecomp.data.model

import java.time.LocalDate
import java.util.UUID

class Doctor(
    phone: String?,
    email: String?,
    password: String?,
    firstName: String?,
    lastName: String?,
    avatar: String?,
    gender: Boolean?,
    dob: LocalDate?,
    val specialty: String?
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