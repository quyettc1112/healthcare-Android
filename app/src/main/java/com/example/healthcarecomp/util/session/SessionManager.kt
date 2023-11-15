package com.example.healthcarecomp.util.session

import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Patient
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.data.sharePreference.AppSharePreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val appSharePreference: AppSharePreference
) {

    companion object {
        const val KEY_MEDICAL_HISTORY_PATIENT_ID = "KEY_MEDICAL_HISTORY_PATIENT_ID"
        const val NULL_DATA = "NULL_DATA"
    }


    fun setMedicalHistoryPatientId(patientId: String) {
        appSharePreference.putString(KEY_MEDICAL_HISTORY_PATIENT_ID, patientId)
    }

    fun getMedicalHistoryPatientId() : String? {
        val result = appSharePreference.getString(KEY_MEDICAL_HISTORY_PATIENT_ID, NULL_DATA)
        return if(result == NULL_DATA) null else result
    }

    fun getLoggedInUser(): User? {
        val patientJson = appSharePreference.prefs.getString(Constant.PATIENT_SHARE_PREF_KEY, NULL_DATA)
        val doctorJson = appSharePreference.prefs.getString(Constant.DOCTOR_SHARE_PREF_KEY, NULL_DATA)
        Log.d("AuthUser", patientJson ?: doctorJson ?: "")
        if (patientJson != NULL_DATA) {
            val gson = Gson()
            val type = object : TypeToken<Patient>() {}.type
            return gson.fromJson(patientJson, type)
        } else if (doctorJson != NULL_DATA) {
            val gson = Gson()
            val type = object : TypeToken<Doctor>() {}.type
            return gson.fromJson(doctorJson, type)
        }
        return null
    }
}