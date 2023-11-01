package com.example.healthcarecomp.data.repositoryImpl

import android.content.SharedPreferences
import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.example.healthcarecomp.util.ValidationUtils.isValidDoctorSecurityCode
import com.example.healthcarecomp.util.extension.isDoctor
import com.example.healthcarecomp.util.extension.isPatient
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val sharePreference: SharedPreferences,
    private val fireBaseDatabase: FirebaseDatabase
) : AuthRepository {

    var currentUser: User? = null
        get() = getLoggedInUser()

    override suspend fun loginByPhone(email: String, password: String): Resource<User> {
        return try {
            return queryUserByPhone(email, password)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }

    private suspend fun queryUserByPhone(phone: String, password: String): Resource<User> {
        val doctorRef = fireBaseDatabase.reference.child(Constant.DoctorQuery.PATH.queryField)
        val patientRef = fireBaseDatabase.reference.child(Constant.PatientQuery.PATH.queryField)

        val doctorSnapshot =
            doctorRef.orderByChild(Constant.DoctorQuery.PHONE.queryField)
                .equalTo(phone)
                .get().await()
        val patientSnapshot =
            patientRef.orderByChild(Constant.PatientQuery.PHONE.queryField)
                .equalTo(phone)
                .get().await()

        if (doctorSnapshot.exists()) {
            val doctor = doctorSnapshot.children.first().getValue(Doctor::class.java)
            val retrievedPassword = doctor?.password
            if (!retrievedPassword.isNullOrEmpty() && retrievedPassword.equals(password)) {
                saveUser(doctor)
                return Resource.Success(doctor)
            } else {
                return Resource.Error("Password incorrect")
            }
        } else if (patientSnapshot.exists()) {
            val patient = patientSnapshot.children.first().getValue(Patient::class.java)
            val retrievedPassword = patient?.password
            if (!retrievedPassword.isNullOrEmpty() && retrievedPassword.equals(password)) {
                saveUser(patient)
                return Resource.Success(patient)
            } else {
                return Resource.Error("Password incorrect")
            }
        } else {
            return Resource.Error("Phone does not exist")
        }
    }

    fun saveUser(user: User) {
        var sharePrefKey: String = Constant.USER_SHARE_PREF_KEY
        if(user.isDoctor()){
            sharePrefKey = Constant.DOCTOR_SHARE_PREF_KEY
        }else if(user.isPatient()){
            sharePrefKey = Constant.PATIENT_SHARE_PREF_KEY
        }
        val gson = Gson()
        val userJson = gson.toJson(user)
        Log.d("Auth",userJson)
        sharePreference.edit().putString(sharePrefKey, userJson).apply()
    }

    override fun getLoggedInUser(): User? {
        val patientJson = sharePreference.getString(Constant.PATIENT_SHARE_PREF_KEY, null)
        val doctorJson = sharePreference.getString(Constant.DOCTOR_SHARE_PREF_KEY, null)
        Log.d("AuthUser",patientJson?:doctorJson?:"")
        if (patientJson != null) {
            val gson = Gson()
            val type = object : TypeToken<Patient>() {}.type
            return gson.fromJson(patientJson, type)
        } else if(doctorJson != null){
            val gson = Gson()
            val type = object : TypeToken<Doctor>() {}.type
            return gson.fromJson(doctorJson, type)
        }
        return null
    }

    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    override suspend fun signup(
        phone: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String,
        doctorCode: String?
    ): Resource<User> {
        var result: Resource<User> = Resource.Loading()
        if (!password.equals(confirmPassword)) {
            result = Resource.Error("Password does not match")
        } else if (!ValidationUtils.validateEmail(email)) {
            result = Resource.Error("Email format is not correct")
        } else if (!ValidationUtils.validatePassword(password)) {
            result = Resource.Error(
                "Password length must >= 8 characters," +
                        " has at least 1 uppercase, 1 digit, 1 special character"
            )
        } else if (isEmailDuplicated(email)) {
            result = Resource.Error("Email is already exist")
        } else if (isPhoneDuplicated(phone.toString())) {
            result = Resource.Error("Phone number is already exist")
        } else {
            try {
                if (doctorCode != null) {
                    if (isValidDoctorSecurityCode(doctorCode)) {

                        var user: User = Doctor(
                            phone,
                            email,
                            password,
                            firstName,
                            lastName,
                            null,
                            null,
                            null,
                            null
                        )
                        val setValueTask =
                            fireBaseDatabase.reference.child(Constant.DoctorQuery.PATH.queryField)
                                .child(user.id).setValue(user)
                        setValueTask.await()
                        result = Resource.Success(user)
                    } else if (!isValidDoctorSecurityCode(doctorCode))
                        result = Resource.Error("Doctor security code is not valid")
                } else {

                    var user: User =
                        Patient(phone, email, password, firstName, lastName, null, null, null, null)
                    val setValueTask =
                        fireBaseDatabase.reference.child(Constant.PatientQuery.PATH.queryField)
                            .child(user.id).setValue(user)
                    setValueTask.await()
                    result = Resource.Success(user)

                }
            } catch (e: Exception) {
                e.printStackTrace()
                result = Resource.Error("Sign up unsuccessfully")
            }
        }
        return result
    }

    suspend fun isEmailDuplicated(emailToCheck: String): Boolean {
        val doctorRef = fireBaseDatabase.reference.child(Constant.DoctorQuery.PATH.queryField)
        val patientRef = fireBaseDatabase.reference.child(Constant.PatientQuery.PATH.queryField)

        val doctorSnapshot =
            doctorRef.orderByChild(Constant.DoctorQuery.EMAIL.queryField).equalTo(emailToCheck)
                .get().await()
        val patientSnapshot =
            patientRef.orderByChild(Constant.PatientQuery.EMAIL.queryField).equalTo(emailToCheck)
                .get().await()

        return doctorSnapshot.exists()
                || patientSnapshot.exists()
    }

    suspend fun isPhoneDuplicated(phoneToCheck: String): Boolean {
        val doctorRef = fireBaseDatabase.reference.child(Constant.DoctorQuery.PATH.queryField)
        val patientRef = fireBaseDatabase.reference.child(Constant.PatientQuery.PATH.queryField)

        val doctorSnapshot =
            doctorRef.orderByChild(Constant.DoctorQuery.PHONE.queryField).equalTo(phoneToCheck)
                .get().await()
        val patientSnapshot =
            patientRef.orderByChild(Constant.PatientQuery.PHONE.queryField).equalTo(phoneToCheck)
                .get().await()

        return doctorSnapshot.exists()
                || patientSnapshot.exists()
    }

    override suspend fun loginByEmail(
        email: String,
        password: String,
    ): Resource<User> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}