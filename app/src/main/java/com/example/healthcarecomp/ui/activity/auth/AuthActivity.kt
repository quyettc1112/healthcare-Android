package com.example.healthcarecomp.ui.activity.auth

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseActivity
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.data.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var authViewModel: AuthViewModel

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private lateinit var googleLoginListener: ((User?) -> Unit)

    companion object {
        const val REQ_ONE_TAP = 1
        const val TAG = "Auth activity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        authViewModel  = ViewModelProvider(this)[AuthViewModel::class.java]
        setupLoginWithGoggle()
    }
    private fun setupLoginWithGoggle(){
        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            ).setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).setAutoSelectEnabled(true)
            .build()
        auth = Firebase.auth

    }

    fun loginWithGoogle(listener: (User?) -> Unit) {
        googleLoginListener = listener
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Could not start one tap UI ${e.localizedMessage}")
                }
            }.addOnFailureListener(this) { e ->
                Log.e(TAG, e.localizedMessage)

            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        val loginUser = auth.currentUser
                                        var user: User? = null
                                        loginUser?.let {
                                            var firstName = ""
                                            var lastName = ""
                                            if(loginUser.displayName!!.contains(" ")){
                                                val nameParts = loginUser.displayName!!.split("\\s+".toRegex())
                                                when(nameParts.size){
                                                    1 -> {
                                                        firstName = nameParts[0]
                                                        lastName = nameParts[0]
                                                    }
                                                    2 -> {
                                                        firstName = nameParts[0]
                                                        lastName = nameParts[1]
                                                    }
                                                    3 -> {
                                                        firstName = nameParts[0]
                                                        lastName = "${nameParts[1]} ${nameParts[2]}"
                                                    }
                                                    4 -> {
                                                        firstName = "${nameParts[0]} ${nameParts[1]}"
                                                        lastName = "${nameParts[2]} ${nameParts[3]}"
                                                    }
                                                    else -> {
                                                        firstName = nameParts[0]
                                                        lastName = "${nameParts[1]} ${nameParts[2]}"
                                                    }
                                                }
                                            }
                                            user = User(
                                                email = loginUser?.email,
                                                phone = loginUser?.phoneNumber,
                                                firstName = firstName,
                                                lastName = lastName
                                            )
                                        }
                                        googleLoginListener(user)

                                    }else{
                                        Log.w(TAG, "SignIn With Credential failed", task.exception)
                                    }
                                }
                        }
                    }
                }catch (e: ApiException){
                    Log.e(TAG, "api exception")
                }
            }
        }
    }




}