package com.example.healthcarecomp.helper

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.healthcarecomp.R


/**
 * BiometricHelper is a utility class for handling biometric authentication in Android.
 * It checks for biometric support, initializes the BiometricPrompt, and provides
 * error, success, and failure callbacks.
 *
 * @param activity The hosting AppCompatActivity for the BiometricHelper.
 * @param onError Callback function to handle authentication error.
 * @param onSucceeded Callback function to handle successful authentication.
 * @param onFailed Callback function to handle authentication failure.
 */
@RequiresApi(Build.VERSION_CODES.P)
class BiometricHelper(
    private val fragmentActivity: Fragment,
    private val onError: (Int, CharSequence) -> Unit,
    private val onSucceeded: () -> Unit,
    private val onFailed: () -> Unit
) {

    // Lazy-initialized BiometricPrompt and PromptInfo for better performance.
    private val biometricPrompt by lazy { createBiometricPrompt() }
    private val promptInfo by lazy { createPromptInfo() }
    private val TAG = "BiometricHelper"
    private val requestCode: Int = 123

    /**
     * Creates a BiometricPrompt instance with the provided executor and authentication callback.
     */
    private fun createBiometricPrompt(): BiometricPrompt {
//        val executor = ContextCompat.getMainExecutor(fragmentActivity)
//        return BiometricPrompt(fragmentActivity, executor, createAuthenticationCallback())
        return BiometricPrompt(fragmentActivity,createAuthenticationCallback())
    }

    /**
     * Creates a PromptInfo for the BiometricPrompt with title, subtitle, and authenticators.
     */
    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragmentActivity.getString(R.string.biometric_authentication_title))
            .setSubtitle(fragmentActivity.getString(R.string.biometric_authentication_subtitle))
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    /**
     * Creates an authentication callback to handle various authentication events.
     */
    private fun createAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                logMessage("Authentication error: $errString")
                onError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                logMessage("Authentication succeeded!")
                onSucceeded()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                logMessage("Authentication failed")
                onFailed()
            }
        }
    }

    /**
     * Logs a message with the specified tag.
     */
    private fun logMessage(message: String) {
        Log.d(TAG, message)
    }

    /**
     * Initiates the biometric authentication process if biometric support is available.
     */
    fun authenticateWithBiometric() {
        if (isBiometricSupport()) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    /**
     * Checks whether biometric authentication is supported on the device.
     * If supported, it returns true; otherwise, it handles error conditions and returns false.
     */
    private fun isBiometricSupport(): Boolean {
        val biometricManager = BiometricManager.from(fragmentActivity.requireContext())
        when (biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                logMessage("Biometric authentication is available")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                logMessage("This device doesn't support biometric authentication")
                onError(
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                    fragmentActivity.getString(R.string.biometric_support_unavailable)
                )
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                logMessage("Biometric authentication is currently unavailable")
                onError(
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                    fragmentActivity.getString(R.string.biometric_currently_unavailable)
                )
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                logMessage("No biometric credentials are enrolled")
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                if (enrollIntent.resolveActivity(fragmentActivity.requireActivity().packageManager) != null) {
                    enrollIntent.putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL
                    )
                    fragmentActivity.startActivityForResult(enrollIntent, requestCode)
                } else {
                    logMessage("Biometric enrollment is not available on this device.")
                    onError(
                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                        fragmentActivity.getString(R.string.biometric_enrollment_unavailable)
                    )
                }
            }
        }
        return false
    }
}
