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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.R
import com.example.healthcarecomp.base.BaseFragment
import com.example.healthcarecomp.common.Adapter.ItemActitivyHomeAdapter
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.databinding.FragmentScheduleBinding
import java.util.Calendar

class ScheduleFragment: BaseFragment(R.layout.fragment_schedule){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("CheckMnag3", Constant.getScheduleToday().toString())
        val binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.btnShowDatePicker.setOnClickListener {
            val dayPickerDialog = DatePickerDialog(requireContext())
            dayPickerDialog.show()
        }

        var adapter = ScheduleAdapter(Constant.getScheduleToday(), "Today")
        binding.rvListTodaySchedule.adapter = adapter

        val adapter_upcoming = ScheduleAdapter(Constant.getScheduleUpComing(), "UpComing")
        binding.rvListUpcomingSchedule.adapter = adapter_upcoming

        return binding.root
    }
}