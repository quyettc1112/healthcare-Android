package com.example.healthcarecomp.ui.chatmessage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.healthcarecomp.base.BaseAdapter
import com.example.healthcarecomp.base.BaseItemViewHolder
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.RowItemChatMessageBinding

class ChatMessageRecyclerViewAdapter(private val user: User, private val partner: User) :
    BaseAdapter<Message, ChatMessageRecyclerViewAdapter.ChatMessageViewHolder>() {

    private var onItemDisplayListener: ((Message) -> Unit)? = null
    private var onDataSubmitListener: (() -> Unit)? = null

    inner class ChatMessageViewHolder(binding: RowItemChatMessageBinding) :
        BaseItemViewHolder<Message, RowItemChatMessageBinding>(binding) {
        override fun bind(item: Message) {
            binding.message.apply {
                val person: User
                val imageView: ImageView
                if (item.senderId == user.id) {
                    setStartDirection(true)
                    person = user
                    imageView = binding.ivMessageLeft
                } else {
                    setStartDirection(false)
                    person = partner
                    imageView = binding.ivMessageRight
                }
                item?.content?.let {
                    setContent(it)
                }
                Glide.with(context).load(person.avatar).into(imageView)
                setMessageSeen(item.seen)
                onItemDisplayListener?.let {
                    it(item)
                }
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<Message> {
        return object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder(
            RowItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setOnItemDisplayListener(listener: (Message) -> Unit) {
        onItemDisplayListener = listener
    }

    override fun submitList(list: MutableList<Message>) {
        super.submitList(list)
        onDataSubmitListener?.let {
            it()
        }
    }

    fun setOnDataSubmitListener(listener: () -> Unit) {
        onDataSubmitListener = listener
    }

}