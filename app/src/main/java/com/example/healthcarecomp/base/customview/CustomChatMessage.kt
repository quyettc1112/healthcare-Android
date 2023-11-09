package com.example.healthcarecomp.base.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.example.healthcarecomp.databinding.CustomChatMessageBinding
import com.example.healthcarecomp.R

class CustomChatMessage(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val binding: CustomChatMessageBinding
    private var startDirection: Boolean

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CustomChatMessage)
        val content =  attr.getString(R.styleable.CustomChatMessage_messageContent) ?: ""
        val avatar = attr.getResourceId(R.styleable.CustomChatMessage_android_src, R.drawable.default_user_avt)
        val leftBackGround = attr.getColorStateList(R.styleable.CustomChatMessage_leftBackGroundColor)
        val rightBackGround = attr.getColorStateList(R.styleable.CustomChatMessage_rightBackGroundColor)
        val textLeftColor = attr.getColor(R.styleable.CustomChatMessage_textLeftColor, resources.getColor(R.color.white))
        val textRightColor = attr.getColor(R.styleable.CustomChatMessage_textRightColor, resources.getColor(R.color.black))
        startDirection = attr.getBoolean(R.styleable.CustomChatMessage_startDirection, true)
        val seen = attr.getBoolean(R.styleable.CustomChatMessage_seen, false)

        attr.recycle()

        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.custom_chat_message,
            this,
            true
        )

        setStartDirection(startDirection)
        setContent(content)
        setAvatar(avatar)
        setLeftMessageBackground(leftBackGround)
        setRightMessageBackground(rightBackGround)
        setTextLeftColor(textLeftColor)
        setTextRightColor(textRightColor)
        setMessageSeen(seen)
    }

    fun setStartDirection(isStartDirection: Boolean) {
        startDirection = isStartDirection
        if(startDirection) {
            binding.llMessageLeft.visibility = View.VISIBLE
            binding.llMessageRight.visibility = View.GONE
        }else {
            binding.llMessageRight.visibility = View.VISIBLE
            binding.llMessageLeft.visibility = View.GONE
        }
    }

    fun setMessageSeen(isSeen: Boolean) {
        if(startDirection && !isSeen){
            binding.ivSendStatusLeft.visibility = View.VISIBLE
        }else{
            binding.ivSendStatusLeft.visibility = View.GONE
        }
    }

    fun setContent(content: String) {
        if(startDirection) {
            binding.tvMessageLeft.text = content
        }else{
            binding.tvMessageRight.text = content
        }
    }

    fun setAvatar(resource: Int) {
        if(startDirection) {
            binding.ivMessageLeft.setImageResource(resource)
            binding.ivMessageLeft.visibility = View.GONE
        }else{
            binding.ivMessageRight.setImageResource(resource)
        }
    }

    fun setLeftMessageBackground(colorStateList: ColorStateList?) {
        ViewCompat.setBackgroundTintList(binding.llContentMessageLeft, colorStateList)
    }

    fun setRightMessageBackground(colorStateList: ColorStateList?) {
        ViewCompat.setBackgroundTintList(binding.llContentMessageRight, colorStateList)
    }

    fun setTextLeftColor(color: Int) {
        binding.tvMessageLeft.setTextColor(color)
    }

    fun setTextRightColor(color: Int) {
        binding.tvMessageRight.setTextColor(color)
    }

}