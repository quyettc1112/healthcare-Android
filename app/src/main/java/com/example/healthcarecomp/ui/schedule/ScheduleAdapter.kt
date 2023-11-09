package com.example.healthcarecomp.ui.schedule

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R

import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.data.repository.DoctorRepository

import com.example.healthcarecomp.databinding.RvListScheduleBinding
import com.example.healthcarecomp.util.extension.isPatient
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

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
            if (scheduleViewModel.currentUser!!.isPatient()) {bindingPatient(Item)}
            else {

            }


        }

        private fun bindingPatient(Item: Schedule) {
            val doctorId = Item.doctorId
            val doctor =
                scheduleViewModel.getListDoctor()?.filter { it.id == doctorId }?.firstOrNull()
            if (doctor != null) {
                Picasso.get()
                    .load(doctor?.avatar) // Assuming item.img is the URL string
                    .placeholder(R.drawable.avatar_1) // Optional: Placeholder image while loading
                    .error(R.drawable.default_user_avt) // Optional: Error image to display on load failure
                    .into(itemBinding.ivUserAVTSchedule)
                itemBinding.tvNameUserMeetingSchedule.text = "Meeting with ${doctor.lastName}"
            } else {
                itemBinding.ivUserAVTSchedule.setImageResource(R.drawable.default_user_avt)
            }
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