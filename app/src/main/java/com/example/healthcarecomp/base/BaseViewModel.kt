package com.example.healthcarecomp.base

import androidx.lifecycle.ViewModel
import com.example.healthcarecomp.util.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    protected lateinit var sessionManager: SessionManager
}