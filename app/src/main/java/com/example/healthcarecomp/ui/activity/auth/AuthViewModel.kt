package com.example.healthcarecomp.ui.activity.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
@HiltViewModel
class AuthViewModel@Inject constructor( val firebaseDatabase: FirebaseDatabase): ViewModel() {

    val NFCValue = MutableStateFlow<HashMap<String, String>>(HashMap())

}