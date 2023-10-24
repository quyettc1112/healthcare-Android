package com.example.healthcarecomp.common.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.databinding.RvItemUserhomeBinding

class ItemUserHomeAdapter(val ItemList: List<Constant.Item_recycleView>) :
    RecyclerView.Adapter<ItemUserHomeAdapter.MainViewHolder>() {

    var onItemClick: ((Constant.Item_recycleView) -> Unit)? = null

    inner class MainViewHolder(val itemBinding: RvItemUserhomeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(Item: Constant.Item_recycleView) {
            itemBinding.ivIconItemUserHome.setImageResource(Item.imageIcon)
            itemBinding.tvNameItemUserHome.text = Item.nameIcon
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            RvItemUserhomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemUserHomeAdapter.MainViewHolder, position: Int) {
        val Item = ItemList[position]
        holder.bindItem(Item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(Item)
        }
    }

    override fun getItemCount(): Int {
        return Constant.getItemListForRecycleView().size
    }


}