package com.example.healthcarecomp.ui.activity.auth

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
@HiltViewModel
class AuthViewModel@Inject constructor( val firebaseDatabase: FirebaseDatabase): ViewModel() {

    val NFCValue = MutableStateFlow<HashMap<String, String>>(HashMap())

    val PSSValue = MutableStateFlow<String>(String())

    val CheckBackPressed = MutableStateFlow<Boolean>(true)


    fun onTextChangePSSValue(string: String) {
        PSSValue.value = string
    }


    fun getOnBackPressedValue(): Boolean{
        return  CheckBackPressed.value
    }

    fun SetOnBackPressedT() {
        CheckBackPressed.value = true
    }
    fun SetOnBackPressedF() {
        CheckBackPressed.value = false
    }



    fun ClearNFCValue() {
        NFCValue.value.clear()
    }

}