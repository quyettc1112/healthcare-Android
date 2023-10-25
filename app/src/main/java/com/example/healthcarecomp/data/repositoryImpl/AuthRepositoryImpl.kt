package com.example.healthcarecomp.data.repositoryImpl

import android.content.Context
import android.widget.Toast
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.repository.AuthRepository
import com.example.healthcarecomp.util.Resource
import com.example.healthcarecomp.util.ValidationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireBaseRef: DatabaseReference,
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
        phone: Int,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        email: String
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
        } else {
            var user = User(phone, email, password, firstName, lastName, null)
//            val setValueTask = fireBaseRef.child("Users").child(user.id).setValue(user)
//                .addOnCompleteListener {
//                    result = Resource.Success(user)
//                    Toast.makeText(applicationContext, "Sign up successfully",Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener{
//                    result = Resource.Error("Sign up unsuccessfully")
//                }.await()
            try {
                val setValueTask = fireBaseRef.child("Users").child(user.id).setValue(user)
                setValueTask.await()
                result =  Resource.Success(user)
            } catch (e: Exception) {
                e.printStackTrace()
                result = Resource.Error("Sign up unsuccessfully")
            }
        }
        return result
    }

    override suspend fun login(
        phone: Int,
        password: String,
    ): Resource<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}