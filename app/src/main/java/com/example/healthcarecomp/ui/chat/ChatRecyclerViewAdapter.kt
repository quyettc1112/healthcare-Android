package com.example.healthcarecomp.ui.chat

import android.util.Log
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
import com.example.healthcarecomp.data.model.User
import okhttp3.internal.notify

class ChatRecyclerViewAdapter : RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>() {

    lateinit var currentUserId: String
    lateinit var dataList: MutableList<Pair<ChatRoom, User>>
    private var onItemClickListener : ((User, String) -> Unit)? = null

    fun setOnItemClickListener(listener: ((User, String) -> Unit)){
        onItemClickListener = listener
    }
    fun submitData(list: MutableList<Pair<ChatRoom, User>>){
        dataList = list
        updateUI()
    }

    fun updateUI(){
        dataList.sortByDescending {
            it.first.lastActiveTime
        }
        differ.submitList(dataList.toList())
    }

    fun submitData(data: Pair<ChatRoom, User>){
        val dataIndex = dataList.indexOfFirst { it.first.id == data.first.id }
        if(dataIndex != -1){
            dataList[dataIndex] = data
        }else{
            dataList.add(data)
        }
        updateUI()
    }

    private val differCallback = object : DiffUtil.ItemCallback<Pair<ChatRoom, User>>() {
        override fun areItemsTheSame(
            oldItem: Pair<ChatRoom, User>,
            newItem: Pair<ChatRoom, User>
        ): Boolean {
            return oldItem.first.id == newItem.first.id
        }

        override fun areContentsTheSame(
            oldItem: Pair<ChatRoom, User>,
            newItem: Pair<ChatRoom, User>
        ): Boolean {
            return oldItem.first == newItem.first
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
        val data = differ.currentList[position]
        val chatRoom = data.first
        val user = data.second
        holder.apply {
            Glide.with(itemView.context).load(chatRoom).placeholder(R.drawable.default_user_avt).into(userAvatar)
            userNameDisplay.text = user?.firstName
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(user, chatRoom.id)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}