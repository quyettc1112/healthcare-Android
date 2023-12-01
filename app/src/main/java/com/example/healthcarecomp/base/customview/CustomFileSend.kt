package com.example.healthcarecomp.base.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.healthcarecomp.R
import com.example.healthcarecomp.databinding.CustomFileSendBinding

class CustomFileSend (context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val binding: CustomFileSendBinding


    init {

        val attr = context.obtainStyledAttributes(attrs, R.styleable.CustomFileSend)
        val img = attr.getResourceId(R.styleable.CustomFileSend_android_src, R.drawable.folder_zip_24px)

        attr.recycle()

        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.custom_file_send,
            this,
            true
        )
        setDisplayImg(img)
    }

    fun setDisplayImg(img: Int) {
        binding.ivSend.setImageResource(img)
    }
    fun setOnXBtnClick(listener: () -> Unit) {
        binding.ivBtnClose.setOnClickListener {
            listener()
        }
    }
}