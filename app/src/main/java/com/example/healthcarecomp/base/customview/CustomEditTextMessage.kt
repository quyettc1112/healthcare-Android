package com.example.healthcarecomp.base.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.healthcarecomp.R
import com.example.healthcarecomp.databinding.CustomEditTextSendBinding

class CustomEditTextMessage(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val binding: CustomEditTextSendBinding

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextMessage)
        val text = attr.getString(R.styleable.CustomEditTextMessage_android_text) ?: ""
        val hint = attr.getString(R.styleable.CustomEditTextMessage_android_hint) ?: ""
        val backGroundColor = attr.getResourceId(R.styleable.CustomEditTextMessage_backgroundColor, R.color.i_gray)

        attr.recycle()

        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.custom_edit_text_send,
            this,
            true
        )

        setText(text)
        setTextHint(hint)
        setBackGroundColor(backGroundColor)
    }

    fun setText(text: String) {
        binding.tvEditText.setText(text)
    }

    fun getText(): String {
        return binding.tvEditText.text.toString()
    }

    fun setTextHint(text: String) {
        binding.tvEditText.hint = text
    }

    fun setBackGroundColor(color: Int) {
        binding.llTextBox.setBackgroundResource(color)
    }

    fun onSendBtnClickListener(listener: () -> Unit) {
        binding.ibBtnSend.setOnClickListener {
            listener()
        }
    }

    fun onPhotoBtnClickListener(listener: () -> Unit) {
        binding.ibBtnImage.setOnClickListener {
            listener()
        }
    }

    fun onAttachBtnClickListener(listener: () -> Unit) {
        binding.ibBtnAttach.setOnClickListener {
            listener()
        }
    }

    fun setDisplayAttachFile(boolean: Boolean) {
        binding.llAttachFile.visibility = if(boolean) View.VISIBLE else View.GONE
    }
}