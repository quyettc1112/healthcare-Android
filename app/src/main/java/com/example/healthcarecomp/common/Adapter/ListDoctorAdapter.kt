package com.example.healthcarecomp.common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Doctor
import com.example.healthcarecomp.data.model.Schedule
import com.example.healthcarecomp.databinding.FragmentScheduleBinding
import com.example.healthcarecomp.databinding.RvItemUserhomeBinding
import com.example.healthcarecomp.databinding.RvListDotorBinding
import com.squareup.picasso.Picasso

class ListDoctorAdapter :
    RecyclerView.Adapter<ListDoctorAdapter.MainViewHolder>() {

    var onItemClick: ((Doctor) -> Unit)? = null


    inner class MainViewHolder(val itemBinding: RvListDotorBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(Item: Doctor) {
            itemBinding.tvNameDoctor.text = Item.lastName
            itemBinding.tvSpecialList.text = Item.specialty.toString()
            Picasso.get()
                .load(Item?.avatar) // Assuming item.img is the URL string
                .placeholder(R.drawable.avatar_1) // Optional: Placeholder image while loading
                .error(R.drawable.default_user_avt) // Optional: Error image to display on load failure
                .into(itemBinding.ivAvtDoctor)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            RvListDotorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListDoctorAdapter.MainViewHolder, position: Int) {
        val Item = differ.currentList[position]
        holder.bindItem(Item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(Item)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private val differCallBack = object : DiffUtil.ItemCallback<Doctor>() {
        override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
}