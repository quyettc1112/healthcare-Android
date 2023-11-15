package com.example.healthcarecomp.ui.chatmessage

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.healthcarecomp.base.BaseAdapter
import com.example.healthcarecomp.base.BaseItemViewHolder
import com.example.healthcarecomp.data.model.Attachment
import com.example.healthcarecomp.databinding.RowItemFileSendBinding

class FileSelectedRecyclerViewAdapter :
    BaseAdapter<Attachment, FileSelectedRecyclerViewAdapter.FileSelectedViewHolder>() {
    private var onXBtnClick: ((Attachment) -> Unit)? = null

    inner class FileSelectedViewHolder(binding: RowItemFileSendBinding) :
        BaseItemViewHolder<Attachment, RowItemFileSendBinding>(binding) {
        override fun bind(item: Attachment) {
            binding.cfs.apply {
                if (item.type == Attachment.TYPE_IMAGE) {
                    val uri = Uri.parse(item.filePath)
                    binding.ivSend.setImageURI(uri)
                }
                this.setOnXBtnClick {
                    onXBtnClick?.let {
                        it(item)
                    }
                }
            }
        }
    }

    override fun differCallBack(): DiffUtil.ItemCallback<Attachment> {
        return object : DiffUtil.ItemCallback<Attachment>() {
            override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
                return oldItem.filePath == newItem.filePath
            }

            override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileSelectedViewHolder {
        return FileSelectedViewHolder(
            RowItemFileSendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun setOnXBtnClickListener(listener: (Attachment) -> Unit) {
        onXBtnClick = listener
    }
}