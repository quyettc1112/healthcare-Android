package com.example.healthcarecomp.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.ChatRoom

class ChatRecyclerViewAdapter : RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
           return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    class ChatViewHolder(view: View) : ViewHolder(view) {
        val userAvatar = view.findViewById<ImageView>(R.id.ivChatItemAvatar)
        val userNameDisplay = view.findViewById<TextView>(R.id.tvChatItemUserName)
        val messageContent = view.findViewById<TextView>(R.id.tvChatItemContent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatRecyclerViewAdapter.ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_item_chat_user,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatRecyclerViewAdapter.ChatViewHolder, position: Int) {
        val chatRoom = differ.currentList[position]
        holder.apply {
            //Glide.with(itemView.context).load(chatRoom)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}