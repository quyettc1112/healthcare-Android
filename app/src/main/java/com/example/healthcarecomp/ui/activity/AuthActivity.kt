package com.example.healthcarecomp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}