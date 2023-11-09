package com.example.healthcarecomp.data.model

import java.time.LocalDate
import java.util.UUID

open class User(
    val phone: String? = null,
    val email: String? = null,
    var password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var avatar: String? = null,
    var gender: Boolean? = null,
    var dob: LocalDate? = null,
    val id: String = UUID.randomUUID().toString()
) {
    override fun equals(other: Any?): Boolean {
        return this.id.equals((other as User).id)
    }

    override fun hashCode(): Int {
        var result = 31 * (phone?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + (gender?.hashCode() ?: 0)
        result = 31 * result + (dob?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(phone=$phone, email=$email, password=$password, firstName=$firstName, lastName=$lastName, avatar=$avatar, gender=$gender, dob=$dob, id='$id')"
    }


}