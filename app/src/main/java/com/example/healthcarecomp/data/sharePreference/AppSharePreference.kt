package com.example.healthcarecomp.data.sharePreference

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppSharePreference @Inject constructor(private val context: Context) {
    companion object{
        const val APP_SHARE_KEY = "com.example.healthcarecomp"
    }

    private fun getSharedPreferences(): SharedPreferences?{
        return context.getSharedPreferences(APP_SHARE_KEY,Context.MODE_PRIVATE)
    }

}