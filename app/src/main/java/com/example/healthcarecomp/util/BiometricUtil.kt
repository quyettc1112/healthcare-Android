package com.example.healthcarecomp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class BiometricUtil {
//
//    fun hasBiometricPermission(context: Context): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.USE_BIOMETRIC
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    fun authenticateWithFingerprint(
//        successCallback: () -> Unit,
//        errorCallback: (errorCode: Int, errorMessage: String) -> Unit,
//        context: Context
//    ) {
//        val biometricPrompt = BiometricPrompt.Builder(context)
//            .setTitle("Fingerprint Authentication")
//            .setSubtitle("Place your finger on the sensor")
//            .setDescription("Touch the fingerprint sensor to unlock")
//            .setNegativeButton("Cancel", context.mainExecutor) { _, _ ->
//                // Handle cancellation
//                errorCallback(-1, "Authentication canceled")
//            }
//            .build()
//
//        val cancellationSignal = CancellationSignal()
//
//        biometricPrompt.authenticate(
//            cancellationSignal,
//            context.mainExecutor,
//            BiometricPrompt.AuthenticationCallback()
//        )
//
//        biometricPrompt.authenticate(cancellationSignal, context.mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
//            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                super.onAuthenticationSucceeded(result)
//                successCallback()
//            }
//
//            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                super.onAuthenticationError(errorCode, errString)
//                errorCallback(errorCode, errString.toString())
//            }
//        })
//    }
}