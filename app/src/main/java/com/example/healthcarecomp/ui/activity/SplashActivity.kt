package com.example.healthcarecomp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.healthcarecomp.R
import com.example.healthcarecomp.ui.activity.auth.AuthActivity
import com.example.healthcarecomp.ui.schedule.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var progressBarProgress = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Set up for progress bar run at 3 second and change activity fo MainAcitivity
        val progressBar = findViewById<ProgressBar>(R.id.progressBar_Splash)
        // Timedelay is a time to wait unitl change new acitvity: 100 = 1 second
        startProgressBar(progressBar, timeDelay = 200)



    }

    private fun openAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startProgressBar(progressBar: ProgressBar, timeDelay: Int) {
        // Khởi tạo một Timer để định kỳ cập nhật thanh progress.
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Cập nhật giá trị tiến trình hiện tại của thanh progress.
                progressBarProgress += 1
                progressBar.progress =( progressBarProgress * 100 / timeDelay)
                // Kiểm tra xem các tác vụ xử lý đã hoàn tất hay chưa.
                if (progressBarProgress >= timeDelay) {
                    timer.cancel()
                    // Chuyển sang main activity.
                    openAuthActivity()
                }
            }
        }, 0, 10)
    }



}