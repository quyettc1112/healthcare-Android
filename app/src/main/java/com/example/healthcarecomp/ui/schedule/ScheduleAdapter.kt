package com.example.healthcarecomp.ui.schedule

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.R

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.databinding.DialogConfirmBinding

import com.example.healthcarecomp.databinding.RvListScheduleBinding
import okhttp3.internal.ignoreIoExceptions
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.util.Locale

class ScheduleAdapter(val scheduleViewModel: ScheduleViewModel) :
    RecyclerView.Adapter<ScheduleAdapter.MainViewHolder>() {
    var onItemClick: ((Schedule) -> Unit)? = null

    inner class MainViewHolder(val itemBinding: RvListScheduleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(Item: Schedule) {
            val month = convertTimestampToCalendar(Item.date_medical_examinaton!!).get(Calendar.MONTH)
            itemBinding.tvMounthSchedule.text = getMonthName(month)
            itemBinding.tvDaySchedule.text = convertTimestampToCalendar(Item.date_medical_examinaton!!).get(Calendar.DAY_OF_MONTH).toString()
            itemBinding.tvTimeMeettingSchedule.text = convertTimestampToCalendar_SimpleTimeFormat(Item.date_medical_examinaton!!)
            itemBinding.tvYearSchedule.text = convertTimestampToCalendar(Item.date_medical_examinaton!!).get(Calendar.YEAR).toString()
            itemBinding.ivUserAVTSchedule.setImageResource(R.drawable.default_user_avt)
        }
        fun nonList(){
            itemBinding.tvTimeMeettingSchedule.text = "havw3 no schedule"
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {

        return MainViewHolder(
            RvListScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // sortDifferByDateTime()
        val Item = differ.currentList[position]
            holder.bindItem(Item)
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(Item)
            }


    }

    private val differCallBack = object : DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
            return oldItem == newItem
        }
    }

    // differ này cho list các schedule today
    val differ = AsyncListDiffer(this, differCallBack)


    inner class SimpleCallBack :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            scheduleViewModel.removeSchedule(differ.currentList[position])
        }
    }

    fun getSimpleCallBack(): SimpleCallBack {
        return SimpleCallBack()
    }

    fun getMonthName(month: Int): String {
        val months = hashMapOf<Int, String>(
            0 to "Jany",
            1 to "Feb",
            2 to "Mar",
            3 to "Apr",
            4 to "May",
            5 to "Jun",
            6 to "Jul",
            7 to "Aug",
            8 to "Sep",
            9 to "Oct",
            10 to "Nov",
            11 to "Dec"
        )
        return months[month] ?: ""
    }

    fun convertTimestampToCalendar(timestamp: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar
    }

    fun convertTimestampToCalendar_SimpleTimeFormat(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val dateFormat = SimpleDateFormat("HH:mm")
        var AM_PM = ""
        if (calendar.get(Calendar.HOUR_OF_DAY) < 12) AM_PM = " AM" else AM_PM = " PM"
        return dateFormat.format(calendar.time) + AM_PM

    }

}