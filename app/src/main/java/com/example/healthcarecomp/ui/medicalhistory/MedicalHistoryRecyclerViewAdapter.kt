package com.example.healthcarecomp.ui.medicalhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.MedicalRecord
import java.text.SimpleDateFormat
import java.util.Locale


class MedicalHistoryRecyclerViewAdapter: RecyclerView.Adapter<MedicalHistoryRecyclerViewAdapter.MedicalHistoryViewHolder>() {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.UK)


    class MedicalHistoryViewHolder(view: View) : ViewHolder(view) {
        val avatar = view.findViewById<ImageView>(R.id.ivMedicalHistoryItemAvatar)
        val doctorName = view.findViewById<TextView>(R.id.tvMedicalHistoryItemDoctorName)
        val date = view.findViewById<TextView>(R.id.tvMedicalHistoryItemDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicalHistoryRecyclerViewAdapter.MedicalHistoryViewHolder {
        return MedicalHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_medical_history, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MedicalHistoryRecyclerViewAdapter.MedicalHistoryViewHolder,
        position: Int
    ) {
        val medicalRecord =  differ.currentList[position]

        holder.itemView.setOnClickListener {
            onDetailButtonClick?.let {
                it(medicalRecord)
            }
        }

        holder.apply {
            medicalRecord?.date?.let {
                this.date.text = formatter.format(it)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallBack = object :DiffUtil.ItemCallback<MedicalRecord>() {
        override fun areItemsTheSame(oldItem: MedicalRecord, newItem: MedicalRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MedicalRecord, newItem: MedicalRecord): Boolean {
            return oldItem == newItem
        }

    }

    private var onDetailButtonClick: ((MedicalRecord) -> Unit)? = null

    fun setOnItemDetailButtonClick(listener: (MedicalRecord) -> Unit){
        onDetailButtonClick = listener
    }

    val differ = AsyncListDiffer(this, differCallBack)
}