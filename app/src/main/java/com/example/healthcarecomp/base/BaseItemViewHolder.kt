package com.example.healthcarecomp.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

abstract class BaseItemViewHolder<T : Any, VB : ViewBinding>(val binding: VB) :
    ViewHolder(binding.root) {

    val itemContext: Context = binding.root.context
    abstract fun bind(item: T)

}