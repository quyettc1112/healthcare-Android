package com.example.healthcarecomp.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.MedicalSchedule

import com.example.healthcarecomp.databinding.RvListScheduleBinding

class ScheduleAdapter(val scheduleList: List<MedicalSchedule>):  RecyclerView.Adapter<ScheduleAdapter.MainViewHolder>()  {

    var onItemClick: ((MedicalSchedule) -> Unit)? = null
    inner class MainViewHolder(val itemBinding: RvListScheduleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(Item: MedicalSchedule) {
            itemBinding.tvMounthSchedule.text = Item.date_medical_examinaton.month.toString()
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
        return Constant.getScheduleToday().size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val Item = scheduleList[position]
        holder.bindItem(Item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(Item)
        }
    }

}