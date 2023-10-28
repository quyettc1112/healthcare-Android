package com.example.healthcarecomp.ui.schedule

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
import java.text.SimpleDateFormat

class ScheduleAdapter(val scheduleList: List<Schedule>, val kindOfSchdule: String):  RecyclerView.Adapter<ScheduleAdapter.MainViewHolder>()  {

    var onItemClick: ((Schedule) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: RvListScheduleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(Item: Schedule) {
            itemBinding.tvMounthSchedule.text = getMonthName(Item.date_medical_examinaton?.month!!)
            itemBinding.tvDaySchedule.text = Item.date_medical_examinaton?.date.toString()
            //itemBinding.tvTimeMeettingSchedule
            itemBinding.ivUserAVTSchedule.setImageResource(R.drawable.default_user_avt)
            //itemBinding.tvTimeMeettingSchedule
        }
    }

    fun getMonthName(month: Int): String {
        val months = hashMapOf<Int, String>(
            0 to "Jan",
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