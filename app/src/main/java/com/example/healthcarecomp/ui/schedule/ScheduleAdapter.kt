package com.example.healthcarecomp.ui.schedule

import android.icu.util.Calendar
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.R

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalRecord
import com.example.healthcarecomp.data.model.Schedule

import com.example.healthcarecomp.databinding.RvListScheduleBinding
import okhttp3.internal.ignoreIoExceptions
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset

class ScheduleAdapter(val scheduleList: List<Schedule>, val kindOfSchdule: String):  RecyclerView.Adapter<ScheduleAdapter.MainViewHolder>()  {

    var onItemClick: ((Schedule) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: RvListScheduleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(Item: Schedule) {
            itemBinding.tvDaySchedule.text = toCalendarUtc(Item.date_medical_examinaton!!).get(Calendar.DAY_OF_MONTH).toString()
            itemBinding.tvTimeMeettingSchedule.text = "${toCalendarUtc(Item.date_medical_examinaton!!).get(Calendar.HOUR_OF_DAY)}" +
                                                      " : ${toCalendarUtc(Item.date_medical_examinaton!!).get(Calendar.MINUTE)} "

            itemBinding.ivUserAVTSchedule.setImageResource(R.drawable.default_user_avt)
        }
    }

    fun toCalendarUtc(timestamp: Long): Calendar {
        val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.ofEpochMilli(timestamp)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val utcDateTime = instant.atZone(ZoneOffset.UTC).toLocalDateTime()

        val calendar = Calendar.getInstance()
        calendar.set(utcDateTime.year, utcDateTime.monthValue, utcDateTime.dayOfMonth, utcDateTime.hour, utcDateTime.minute, utcDateTime.second)
        return calendar
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
        if (kindOfSchdule.equals("Today")) return differ.currentList.size
      //  if (kindOfSchdule.equals("UpComing")) return Constant.getScheduleUpComing().size
        return 0
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
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
    val differ = AsyncListDiffer(this, differCallBack)

}