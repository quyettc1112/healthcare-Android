package com.example.healthcarecomp.ui.activity.auth

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseActivity
import com.example.healthcarecomp.ui.activity.main.MainActivity
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.helper.NFCHelper
import com.example.healthcarecomp.ui.auth.login.LoginFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and


@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    lateinit var authViewModel: AuthViewModel


    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private lateinit var googleLoginListener: ((User?) -> Unit)


    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var writeMode = false
    var myTag: Tag? = null


    companion object {
        const val REQ_ONE_TAP = 1
        const val TAG = "Auth activity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setupLoginWithGoggle()


        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish()
        }
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
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
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        val signInIntent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, FULLSCREEN_MODE_REQUEST_ENTER)

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

    /**
     * For reading the NFC when the app is already launched
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        NFCHelper(authViewModel, myTag, this).readFromIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)

    }
    override fun onPause() {
        super.onPause()
       // nfcAdapter!!.disableForegroundDispatch(this)
    }


}