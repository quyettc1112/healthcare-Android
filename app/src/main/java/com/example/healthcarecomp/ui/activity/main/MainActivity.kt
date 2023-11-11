package com.example.healthcarecomp.ui.activity.main

import android.Manifest
import android.app.ActionBar
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.example.healthcarecomp.data.model.User
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
    }

    private fun setupNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic(currentUser?.id!!)
    }

    private fun setUpThread() {
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        GlobalScope.launch {
            scheduleViewModel.getAllDoctor()
        }
    }

    override fun showLoading(isShow: Boolean) {
        loadingLayout?.visibility = if(isShow) View.VISIBLE else View.GONE
    }


    fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

    }

    fun setObservers(){

    }



}