package com.example.healthcarecomp.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : BaseItemViewHolder<T, *>> : RecyclerView.Adapter<VH>() {

    private var onItemClickListener: ((T) -> Unit)? = null

    private var setItemOrderBy: ((List<T>) -> Unit)? = null

    protected abstract fun differCallBack(): DiffUtil.ItemCallback<T>

    private val differ = AsyncListDiffer(this, differCallBack())

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        onItemClickListener?.let { listener ->
            holder.itemView.setOnClickListener { _ ->
                listener(item)
            }
        }
    }

    fun submitList(list: MutableList<T>) {
        setItemOrderBy?.let {
            it(list.toList())
        }
        differ.submitList(list.toList())
        notifyDataSetChanged()
    }

    fun setItemOrderBy(listener: (List<T>) -> Unit) {
        setItemOrderBy = listener
    }

    fun setItemOnclickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }

}