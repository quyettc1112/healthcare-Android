package com.example.healthcarecomp.ui.activity.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class AuthViewModel@Inject constructor( val firebaseDatabase: FirebaseDatabase): ViewModel() {

}