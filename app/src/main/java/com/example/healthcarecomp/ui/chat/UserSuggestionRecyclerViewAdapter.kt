package com.example.healthcarecomp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.healthcarecomp.base.BaseAdapter
import com.example.healthcarecomp.base.BaseItemViewHolder
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.databinding.RowItemSearchSuggestionBinding

class UserSuggestionRecyclerViewAdapter() :
    BaseAdapter<User, UserSuggestionRecyclerViewAdapter.UserSuggestionViewHolder>() {
    var typedText = ""

    inner class UserSuggestionViewHolder(binding: RowItemSearchSuggestionBinding) :
        BaseItemViewHolder<User, RowItemSearchSuggestionBinding>(binding) {
        override fun bind(item: User) {
            val suggestionText = "${item.firstName} ${item.lastName} (${item.email})"
            val suggestion = cutText(suggestionText, typedText)
            binding.tvSuggestionFirst.text = suggestion.first
            binding.tvSuggestionMid.text = suggestion.second
            binding.tvSuggestionLast.text = suggestion.third
        }

    }

    private fun cutText(inputText: String, typed: String): Triple<String, String, String> {
        val index = inputText.lowercase().indexOf(typed.lowercase())
        if (index != -1) {
            val first = inputText.substring(0, index)
            val second = inputText.substring(index, index + typed.length)
            val last = inputText.substring(index + typed.length)
            return Triple(first, second, last)
        }
        return Triple("", "", "")
    }

    override fun differCallBack(): DiffUtil.ItemCallback<User> {
        return object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSuggestionViewHolder {
        return UserSuggestionViewHolder(
            RowItemSearchSuggestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}