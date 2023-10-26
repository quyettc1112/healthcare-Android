package com.example.healthcarecomp.ui.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
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
import java.util.Calendar

@AndroidEntryPoint
class ScheduleFragment : BaseFragment(R.layout.fragment_schedule) {

    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // biến binding
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)

        // tạo biến để observer
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        binding.btnShowDatePicker.setOnClickListener {
            // Tạo đối tượng DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // Cập nhật ngày tháng đã chọn
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)

                    // Tạo đối tượng TimePickerDialog
                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minute ->
                            // Cập nhật giờ đã chọn
                            ConfirmDialog()
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            // In ra dữ liệu

//                            if (ConfirmDialog() == true) {
//                                Log.d("dayAndTime", calendar.time.toString())
//                            }


                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    // Hiển thị TimePickerDialog
                    timePickerDialog.show()

                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            // Hiển thị DatePickerDialog
            datePickerDialog.show()


        }


        setUpUI(binding)
        return binding.root
    }

    private fun setUpUI(binding: FragmentScheduleBinding) {
        var adapter = ScheduleAdapter(Constant.getScheduleToday(), "Today")
        binding.rvListTodaySchedule.adapter = adapter

        val adapter_upcoming = ScheduleAdapter(Constant.getScheduleUpComing(), "UpComing")
        binding.rvListUpcomingSchedule.adapter = adapter_upcoming
    }

    fun ConfirmDialog(): Boolean? {
        var result: Boolean? = false
        val confirmDialog = ConfirmDialog(
            requireContext(),
            // Pass in a callback object to handle the actions that should be taken when the user clicks on the negative and positive buttons of the ConfirmDialog
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {
                   result = false
                }

                override fun positiveAction() {
                    result = true
                }
            },
            // Set the title of the ConfirmDialog
            title = "Confirm",
            // Set the message of the ConfirmDialog
            message = "Are you sure you want to proceed?",
            // Set the positive button title of the ConfirmDialog
            positiveButtonTitle = "Yes",
            // Set the negative button title of the ConfirmDialog
            negativeButtonTitle = "No",

        )

        // Show the ConfirmDialog
        confirmDialog.show()
        return result
    }
}