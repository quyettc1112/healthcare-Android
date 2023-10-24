package com.example.healthcarecomp.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {
    private var loadingLayout: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Frank","MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // loadingLayout = findViewById(R.id.loadingLayout)
        setupBottomNav()
    }

    override fun showLoading(isShow: Boolean) {
        loadingLayout?.visibility = if(isShow) View.VISIBLE else View.GONE
    }


    fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

    }






}