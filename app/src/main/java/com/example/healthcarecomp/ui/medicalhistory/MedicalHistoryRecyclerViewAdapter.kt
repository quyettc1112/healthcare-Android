package com.example.healthcarecomp.ui.medicalhistory

import android.content.Context
import android.util.Log
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
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.MedicalRecord
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.HashMap


class MedicalHistoryRecyclerViewAdapter: RecyclerView.Adapter<MedicalHistoryRecyclerViewAdapter.MedicalHistoryViewHolder>() {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.UK)
    lateinit var doctorList : HashMap<String?,Doctor?>
    private lateinit var _context: Context

    class MedicalHistoryViewHolder(view: View) : ViewHolder(view) {
        val avatar = view.findViewById<ImageView>(R.id.ivMedicalHistoryItemAvatar)
        val doctorName = view.findViewById<TextView>(R.id.tvMedicalHistoryItemDoctorName)
        val date = view.findViewById<TextView>(R.id.tvMedicalHistoryItemDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicalHistoryRecyclerViewAdapter.MedicalHistoryViewHolder {
        _context = parent.context
        return MedicalHistoryViewHolder(
            LayoutInflater.from(_context).inflate(R.layout.row_item_medical_history, parent, false)
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
            val doctor = doctorList[medicalRecord.doctorId]
            doctor?.let {
                this.doctorName.text = "Dr.${it.firstName} ${it.lastName}"
                Glide.with(_context).load(it.avatar).placeholder(R.drawable.default_user_avt).into(holder.avatar)
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