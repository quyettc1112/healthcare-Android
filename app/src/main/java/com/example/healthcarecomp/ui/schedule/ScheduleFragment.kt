package com.example.healthcarecomp.ui.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.base.dialog.ConfirmDialog
import com.example.healthcarecomp.common.Adapter.ItemActitivyHomeAdapter
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private var calendar = Calendar.getInstance()
    private var isCanceled: Boolean = false
    private var isTimeCanceled: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentScheduleBinding.inflate(inflater, container, false)
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        setDate(binding)
        binding.tvDayChoose.text = Calendar.getInstance().time.toString()
       // setUpUI(binding)
        return binding.root
    }

    private fun setDate(binding: FragmentScheduleBinding) {
        binding.btnShowDatePicker.setOnClickListener {
            // Tạo đối tượng DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            var setTime: Calendar? = null
            datePickerDialog.setOnCancelListener { isCanceled = true }
            datePickerDialog.setOnDismissListener {
                if (isCanceled == false) {
                    setTime = calendar
                    setTime(setTime, binding)
                } else isCanceled = false
            }
            datePickerDialog.show()
        }
    }

    private fun setTime(calendar: Calendar?, binding: FragmentScheduleBinding) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar?.set(Calendar.MINUTE, minute)
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        )
        timePickerDialog.setOnCancelListener { isTimeCanceled = true }
        timePickerDialog.setOnDismissListener {
            if (isTimeCanceled == false) {
                ConfirmDialog(calendar, binding)
            } else isTimeCanceled = false
        }
        timePickerDialog.show()

    }

    private fun setUpUI(binding: FragmentScheduleBinding) {


        var adapter = ScheduleAdapter(Constant.getScheduleToday(), "Today")
        binding.rvListTodaySchedule.adapter = adapter

        val adapter_upcoming = ScheduleAdapter(Constant.getScheduleUpComing(), "UpComing")
        binding.rvListUpcomingSchedule.adapter = adapter_upcoming
    }

    fun ConfirmDialog(calendar: Calendar?, binding: FragmentScheduleBinding) {

        val confirmDialog = ConfirmDialog(
            requireContext(),
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {
                    // Do something when user dont want to schedule
                }
                override fun positiveAction() {
                   binding.tvDayChoose.text = calendar?.time.toString()
                }
            },
            // Set the title of the ConfirmDialog
            title = "Confirm",
            // Set the message of the ConfirmDialog
            message = "Are you sure you want to proceed?",
            // Set the positive button title of the ConfirmDialog
            positiveButtonTitle = "Yes",
            // Set the negative button title of the ConfirmDialog
            negativeButtonTitle = "No"
        )
        // Show the ConfirmDialog
        confirmDialog.show()
    }

}

