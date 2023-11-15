package com.example.healthcarecomp.util

import android.app.appsearch.AppSearchSchema.BooleanPropertyConfig
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.healthcarecomp.common.Constant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ValidationUtils{

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun validatePassword(password: String): Boolean {
        if (password.length < 8) {
            return false
        }

        var hasUpperCase = false;
        var hasDigitCharacter = false;
        var hasLowerCase = false;

        var count = 0
        for (i in password.indices) {
            val character = password[i]
            when {
                Character.isDigit(character) -> {
                    hasDigitCharacter = true;
                    count++;
                }
                Character.isUpperCase(character) -> {
                    hasUpperCase = true;
                    count++;
                }
                Character.isLowerCase(character) -> {
                    hasLowerCase = true;
                    count++;
                }
            }
        }

        val hasSpecialCharacter = count != password.length

        if (!hasUpperCase || !hasDigitCharacter || !hasLowerCase || !hasSpecialCharacter) {
            return false
        }

        return true
    }

    fun isValidDoctorSecurityCode(doctorSecurityCode: String): Boolean{
        return doctorSecurityCode.equals(Constant.DOCTOR_SECURITY_DOCTOR)
    }

    fun isValidFirstName(firstName: String): Boolean{
        return firstName.isNotEmpty()
    }

    fun isValidLastName(lastName: String): Boolean{
        return lastName.length >= 2
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean{
        return phoneNumber.toString().length in 9..11
    }

    fun isValidConfirmPassword(confirmPassword: String, password: String): Boolean {
        return confirmPassword.equals(password)
    }

    fun isValidWeight(weight: String): Boolean{
        val weight = weight.trim().toFloatOrNull()
        return weight != null && weight in Constant.MEDICAL.FLOAT.WEIGHT.range
    }

    fun isValidHeight(height: String): Boolean{
        val height = height.trim().toFloatOrNull()
        return height != null && height in Constant.MEDICAL.FLOAT.HEIGHT.range
    }

    fun isValidBodyTemperature(bodyTemperature: String): Boolean{
        val bodyTemperature = bodyTemperature.trim().toFloatOrNull()
        return bodyTemperature != null && bodyTemperature in Constant.MEDICAL.FLOAT.BODY_TEMPERATURE.range
    }

    fun isValidHearthRate(hearthRate: String): Boolean{
        val hearthRate = hearthRate.trim().toIntOrNull()
        return hearthRate != null && hearthRate in Constant.MEDICAL.INT.HEARTH_RATE.range
    }

    fun isValidBloodSugar(bloodSugar: String): Boolean {
        val bloodSugar = bloodSugar.trim().toIntOrNull()
        return bloodSugar != null && bloodSugar in Constant.MEDICAL.INT.BLOOD_SUGAR.range
    }

    fun isValidBloodPressure(bloodPressure: String): Boolean {
        return Constant.MEDICAL.STRING.BLOOD_PRESSURE.regex.matches(bloodPressure.trim())
    }

    fun isValidDob(dobCharSequence: CharSequence): Boolean{
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.parse(dobCharSequence, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                true
            } else {
                false
            }
        }catch (e: Exception){
            false
        }
    }
}