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
import android.widget.Toast
import androidx.lifecycle.Observer
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
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryRecyclerViewAdapter
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

@AndroidEntryPoint
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var _recyclerViewAdapter: ScheduleAdapter
    private var calendar = Calendar.getInstance()
    private var isCanceled: Boolean = false
    private var isTimeCanceled: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)

        setDate(binding)
        binding.tvDayChoose.text = Calendar.getInstance().time.toString()
        setUpUI(binding)
        return binding.root
    }

    private fun planSchedule(calendar: Calendar?) {
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        scheduleViewModel.upsertSchedule(
            Schedule(
                doctorId = 1,
                patientID = 1,
                date_medical_examinaton = calendar?.timeInMillis,
                status_medical_schedule = "Đã Đặt Lịch",
                note = "Tôi bị đau bụng"
            )
        )
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
//        var adapter = ScheduleAdapter(Constant.getScheduleToday(), "Today")
        _recyclerViewAdapter = ScheduleAdapter(Constant.getScheduleToday(), "Today")
        binding.rvListTodaySchedule.apply {
            adapter = _recyclerViewAdapter
        }
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        scheduleViewModel.scheduleListToday.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> _recyclerViewAdapter.differ.submitList(it.data)
                else -> {}

            }
        })


//        val adapter_upcoming = ScheduleAdapter(Constant.getScheduleUpComing(), "UpComing")
//        binding.rvListUpcomingSchedule.adapter = adapter_upcoming
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
                    planSchedule(calendar)
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

