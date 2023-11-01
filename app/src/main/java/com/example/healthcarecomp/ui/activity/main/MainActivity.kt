package com.example.healthcarecomp.ui.activity.main

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var loadingLayout: FrameLayout? = null
     val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setupBottomNav()
        setObservers()
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