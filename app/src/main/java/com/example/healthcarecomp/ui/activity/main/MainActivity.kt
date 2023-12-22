package com.example.healthcarecomp.ui.activity.main

import android.Manifest
import android.app.ActionBar
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseActivity
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.helper.NFCHelper
import com.example.healthcarecomp.ui.activity.auth.AuthViewModel
import com.example.healthcarecomp.ui.schedule.ScheduleViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.remoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.FileInputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var loadingLayout: FrameLayout? = null
    val mainViewModel: MainViewModel by viewModels()
    var currentUser: User? = null
    lateinit var authViewModel: AuthViewModel


    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var writeMode = false
    var myTag: Tag? = null

    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = mainViewModel.localUser
        mainViewModel.currentUser.observe(this, Observer {
            currentUser = it
        })
        setUpThread()
        setContentView(R.layout.activity_main)
        setupBottomNav()
        setObservers()
        setupNotifications()
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            //finish()
        } else {
            //NFCHelper(authViewModel, myTag, this).readFromIntent(intent)
            pendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            )
            readFromIntent(intent = intent)
        }


    }
    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            myTag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
            mainViewModel.changeTag(myTag)
        }
    }



    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        //NFCHelper(authViewModel, myTag, this).readFromIntent(intent)
        readFromIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
    }



    private fun setupNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic(currentUser?.id!!)
    }

    private fun setUpThread() {
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        GlobalScope.launch {
            scheduleViewModel.getAllDoctor()
            scheduleViewModel.getAllPatient()
        }
    }

    override fun showLoading(isShow: Boolean) {
        loadingLayout?.visibility = if(isShow) View.VISIBLE else View.GONE
    }


    fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //handle display bottom selected when a fragment display
        val menuHashMap = HashMap<Int, MenuItem>()
        navView.menu?.let{ menu ->
            for(i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                menuHashMap[menuItem.itemId] = menuItem
            }
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
             val selectedId = when (destination.id) {
                in Constant.BottomNav.CHAT_NAV ->  R.id.navigation_chat
                in Constant.BottomNav.SCHEDULE_NAV -> R.id.navigation_schedule
                in Constant.BottomNav.INFO_NAV -> R.id.navigation_info
                else -> R.id.navigation_home
            }
            menuHashMap[selectedId]?.isChecked = true
        }



        navView.setupWithNavController(navController)

    }

    fun setObservers(){

    }

    public override fun onResume() {
        super.onResume()
     //   nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
       // myTag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?
    }






}