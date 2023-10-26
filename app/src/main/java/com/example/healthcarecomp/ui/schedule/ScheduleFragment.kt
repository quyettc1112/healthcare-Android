package com.example.healthcarecomp.ui.schedule

import android.app.DatePickerDialog
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
import com.example.healthcarecomp.common.Adapter.ItemActitivyHomeAdapter
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ScheduleFragment: BaseFragment(R.layout.fragment_schedule){

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

            val today = Calendar.getInstance()

            // Gán giá trị của Calendar cho DatePickerDialog
            val dayPickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // ...
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            )

            dayPickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            dayPickerDialog.show()
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
}