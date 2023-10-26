package com.example.healthcarecomp.data.repositoryImpl

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.example.healthcarecomp.util.ValidationUtils.isValidDoctorSecurityCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireBaseDatabase: FirebaseDatabase,
    val applicationContext: Context
) : AuthRepository {
    override var currentUser: FirebaseUser? = null
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message)
        }
    }

    override suspend fun signup(
        email: String,
        password: String,
        confirmPassword: String
    ): Resource<FirebaseUser> {
        return if (!password.equals(confirmPassword)) {
            Resource.Error("Password does not match")
        } else if (!ValidationUtils.validateEmail(email)) {
            Resource.Error("Email format is not correct")
        } else if (!ValidationUtils.validatePassword(password)) {
            Resource.Error(
                "Password length must >= 8 characters," +
                        " has at least 1 uppercase, 1 digit, 1 special character"
            )
        } else {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                result?.user?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(email).build()
                )?.await()
                currentUser = result.user!!
                Resource.Success(result.user!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(e.message)
            }
        }

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
        }else if(isEmailDuplicated(email)) {
            result = Resource.Error("Email is already exist")
        }else if(isPhoneDuplicated(phone.toString())){
            result = Resource.Error("Phone number is already exist")
        } else {
            try {
                if (doctorCode != null) {
                    if(isValidDoctorSecurityCode(doctorCode)){

                        var user: User = Doctor(phone, email, password, firstName, lastName, null, null, null, null)
                        val setValueTask = fireBaseDatabase.reference.child("Doctor").child(user.id).setValue(user)
                        setValueTask.await()
                        result = Resource.Success(user)
                    } else if(!isValidDoctorSecurityCode(doctorCode))
                        result = Resource.Error("Doctor security code is not valid")
                } else{

                        var user: User = Patient(phone, email, password, firstName, lastName, null, null, null, null)
                        val setValueTask = fireBaseDatabase.reference.child("Patient").child(user.id).setValue(user)
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
        val doctorRef = fireBaseDatabase.reference.child("Doctor")
        val patientRef = fireBaseDatabase.reference.child("Patient")

        val doctorSnapshot = doctorRef.orderByChild("email").equalTo(emailToCheck).get().await()
        val patientSnapshot = patientRef.orderByChild("email").equalTo(emailToCheck).get().await()

        return doctorSnapshot.exists()
                || patientSnapshot.exists()
    }

    suspend fun isPhoneDuplicated(phoneToCheck: String): Boolean {
        val doctorRef = fireBaseDatabase.reference.child("Doctor")
        val patientRef = fireBaseDatabase.reference.child("Patient")

        val doctorSnapshot = doctorRef.orderByChild("phone").equalTo(phoneToCheck).get().await()
        val patientSnapshot = patientRef.orderByChild("phone").equalTo(phoneToCheck).get().await()

        return doctorSnapshot.exists()
                || patientSnapshot.exists()
    }

    override suspend fun loginByPhone(
        phone: String,
        password: String,
    ): Resource<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}