package com.example.healthcarecomp.ui.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.adapters.ToolbarBindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.base.dialog.ConfirmDialog
import com.example.healthcarecomp.base.dialog.ErrorDialog
import com.example.healthcarecomp.common.Adapter.ItemActitivyHomeAdapter
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.databinding.FragmentScheduleBinding
import com.example.healthcarecomp.ui.medicalhistory.MedicalHistoryRecyclerViewAdapter
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

@AndroidEntryPoint
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var _recyclerViewAdapter: ScheduleAdapter
    private lateinit var _recyclerViewAdapter_UpComing: ScheduleAdapter


    private var calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm")

    private var isCanceled: Boolean = false
    private var isTimeCanceled: Boolean = false


    var currentTime = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)

        setDate(binding)
        binding.tvDayChoose.text = Calendar.getInstance().time.toString()
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        setUpUI(binding, scheduleViewModel)
        setUpUI_UpComing(binding, scheduleViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

    }


    private fun planSchedule(calendar: Calendar?) {
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        scheduleViewModel.upsertSchedule(
            Schedule(
                doctorId = "cbf1d2ea-0249-452e-bd4e-7db757ad6f4c",
                patientID = "1a04ee07-5909-4471-b767-a62f8c1e99d1",
                date_medical_examinaton = calendar?.timeInMillis,
                status_medical_schedule = "Đã Đặt Lịch",
                note = "Tôi bị đau bụng",
            )
        )
    }

    private fun setDate(binding: FragmentScheduleBinding) {
        // Lấy ngày giờ hiện tại
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
            var setTime: Calendar?
            datePickerDialog.setOnCancelListener { isCanceled = true }
            datePickerDialog.setOnDismissListener {
                if (isCanceled == false) {
                    setTime = calendar
                    setTime(setTime, binding)
                } else isCanceled = false
            }
            datePickerDialog.datePicker.minDate = currentTime.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun setTime(calendar: Calendar?, binding: FragmentScheduleBinding) {
        var validationHour = 0
        var validationMin = 0
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                validationHour = hourOfDay
                validationMin = minute
                calendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar?.set(Calendar.MINUTE, minute)
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        )
        timePickerDialog.setOnCancelListener { isTimeCanceled = true }
        timePickerDialog.setOnDismissListener {
            currentTime = Calendar.getInstance()
            if (isTimeCanceled == false && (calendar?.get(Calendar.DAY_OF_MONTH)
                        == currentTime.get(Calendar.DAY_OF_MONTH))
            ) {
                if (validationHour.toInt() < currentTime.get(Calendar.HOUR_OF_DAY) ||
                    (validationHour == currentTime.get(Calendar.HOUR_OF_DAY)
                            && validationMin < currentTime.get(Calendar.MINUTE))
                ) {
                    val confirmDialog = ConfirmDialog(
                        requireContext(),
                        object : ConfirmDialog.ConfirmCallback {
                            override fun negativeAction() {}
                            override fun positiveAction() {
                                timePickerDialog.updateTime(
                                    currentTime.get(Calendar.HOUR_OF_DAY),
                                    currentTime.get(Calendar.MINUTE)
                                )
                                timePickerDialog.show()
                            }
                        },
                        title = "Error",
                        message = "Cannot Choose Time In The Past",
                        positiveButtonTitle = "Again",
                        negativeButtonTitle = "Cancel"
                    )
                    // Show the ConfirmDialog
                    confirmDialog.show()
                } else ConfirmDialog(
                    calendar,
                    binding,
                    "You will meet doctor at \n ${dateFormat.format(calendar?.time)}"
                )
            } else {
                when(isTimeCanceled) {
                    true -> {
                        isTimeCanceled = false
                    }
                    false -> {
                        ConfirmDialog(
                            calendar,
                            binding,
                            "You will meet doctor at \n ${dateFormat.format(calendar?.time)}"
                        )
                    }
                }
            }
        }
        timePickerDialog.show()
    }

    private fun setUpUI(binding: FragmentScheduleBinding, scheduleViewModel: ScheduleViewModel) {
        _recyclerViewAdapter = ScheduleAdapter(scheduleViewModel)
        binding.rvListTodaySchedule.apply {
            adapter = _recyclerViewAdapter
        }
        scheduleViewModel.scheduleListToday.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                        _recyclerViewAdapter.differ.submitList(it.data?.toList())
                }
                else -> {}
            }

        })
        val itemTouchHelper = ItemTouchHelper(_recyclerViewAdapter.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(binding.rvListTodaySchedule)
    }

    private fun setUpUI_UpComing(
        binding: FragmentScheduleBinding,
        scheduleViewModel: ScheduleViewModel
    ) {
        _recyclerViewAdapter_UpComing = ScheduleAdapter(scheduleViewModel)
        binding.rvListUpcomingSchedule.apply {
            adapter = _recyclerViewAdapter_UpComing
        }
        scheduleViewModel.scheduleListUpComing.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    _recyclerViewAdapter_UpComing.differ.submitList(it.data?.toList())
                }

                else -> {}
            }
        })
        val itemTouchHelper = ItemTouchHelper(_recyclerViewAdapter_UpComing.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(binding.rvListUpcomingSchedule)
    }


    fun ConfirmDialog(calendar: Calendar?, binding: FragmentScheduleBinding, message: String) {
        val confirmDialog = ConfirmDialog(
            requireContext(),
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {}
                override fun positiveAction() {
                    binding.tvDayChoose.text = calendar?.time.toString()
                    planSchedule(calendar)
                }
            },
            title = "Confirm",
            message = message,
            positiveButtonTitle = "Yes",
            negativeButtonTitle = "No"
        )
        confirmDialog.show()
    }





}

