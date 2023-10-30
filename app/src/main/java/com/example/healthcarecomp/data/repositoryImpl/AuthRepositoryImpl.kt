package com.example.healthcarecomp.data.repositoryImpl

import android.content.SharedPreferences
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.example.healthcarecomp.util.ValidationUtils.isValidDoctorSecurityCode
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val sharePreference: SharedPreferences,
    private val fireBaseDatabase: FirebaseDatabase
) : AuthRepository {

    override var currentUser: User? = null
        get() = getUser()

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

    private suspend fun queryUserByEmail(email: String): Resource<User> {
        val doctorRef = fireBaseDatabase.reference.child(Constant.DoctorQuery.PATH.queryField)
        val patientRef = fireBaseDatabase.reference.child(Constant.PatientQuery.PATH.queryField)

        val doctorSnapshot =
            doctorRef.orderByChild(Constant.DoctorQuery.EMAIL.queryField)
                .equalTo(email)
                .get().await()
        val patientSnapshot =
            patientRef.orderByChild(Constant.PatientQuery.EMAIL.queryField)
                .equalTo(email)
                .get().await()

        if (doctorSnapshot.exists()) {
            val doctor = doctorSnapshot.children.first().getValue(Doctor::class.java)
            return if(doctor != null){
                saveUser(doctor)
                Resource.Success(doctor)
            }else{
                Resource.Unknown()
            }

        } else if (patientSnapshot.exists()) {
            val patient = patientSnapshot.children.first().getValue(Patient::class.java)
            return if (patient != null) {
                saveUser(patient)
                Resource.Success(patient)
            } else {
                Resource.Unknown()
            }
        } else {
            return Resource.Error("Email does not exist")
        }
    }

    fun saveUser(user: User) {
        val gson = Gson()
        val userJson = gson.toJson(user)

        sharePreference.edit().putString(Constant.userSPKey, userJson).apply()
    }

    fun getUser(): User? {
        val userJson = sharePreference.getString(Constant.userSPKey, null)

        if (userJson != null) {
            val gson = Gson()
            val type = object : TypeToken<User>() {}.type
            return gson.fromJson(userJson, type)
        }

        return null
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

//    override suspend fun signup(
//        email: String,
//        password: String,
//        confirmPassword: String
//    ): Resource<FirebaseUser> {
//        return if (!password.equals(confirmPassword)) {
//            Resource.Error("Password does not match")
//        } else if (!ValidationUtils.validateEmail(email)) {
//            Resource.Error("Email format is not correct")
//        } else if (!ValidationUtils.validatePassword(password)) {
//            Resource.Error(
//                "Password length must >= 8 characters," +
//                        " has at least 1 uppercase, 1 digit, 1 special character"
//            )
//        } else {
//            try {
//                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//                result?.user?.updateProfile(
//                    UserProfileChangeRequest.Builder().setDisplayName(email).build()
//                )?.await()
//                currentUser = result.user!!
//                Resource.Success(result.user!!)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Resource.Error(e.message)
//            }
//        }
//
//    }

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
        email: String
    ): Resource<User> {
        return try {
            queryUserByEmail(email)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage)
        }
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}