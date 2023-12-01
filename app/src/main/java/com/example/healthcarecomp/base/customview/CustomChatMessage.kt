package com.example.healthcarecomp.base.customview

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.healthcarecomp.databinding.CustomChatMessageBinding
import com.example.healthcarecomp.R
import com.example.healthcarecomp.data.model.Attachment
import com.example.healthcarecomp.helper.FileHelper
import com.google.android.flexbox.FlexboxLayout
import java.net.URLConnection

class CustomChatMessage(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val binding: CustomChatMessageBinding
    private var startDirection: Boolean
    val leftBackGround : ColorStateList?
    val rightBackGround : ColorStateList?
    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CustomChatMessage)
        val content =  attr.getString(R.styleable.CustomChatMessage_messageContent) ?: ""
        val avatar = attr.getResourceId(R.styleable.CustomChatMessage_android_src, R.drawable.default_user_avt)
        leftBackGround = attr.getColorStateList(R.styleable.CustomChatMessage_leftBackGroundColor)
        rightBackGround = attr.getColorStateList(R.styleable.CustomChatMessage_rightBackGroundColor)
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

    fun refreshContent(){
        binding.flImgContainerRight.removeAllViews()
        binding.flImgContainerLeft.removeAllViews()
        binding.flFileContainerleft.removeAllViews()
        binding.flFileContainerRight.removeAllViews()
        binding.tvMessageLeft.layoutParams = layoutParams
        binding.tvMessageRight.layoutParams = layoutParams
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

    fun setContentVisible(boolean: Boolean) {
        if(startDirection) {
            binding.llContentMessageLeft.visibility = if(boolean) View.VISIBLE else View.GONE
        }else{
            binding.llContentMessageRight.visibility = if(boolean) View.VISIBLE else View.GONE
        }
    }

    fun displayAttachments(list: List<Attachment>, context: Context) {
        if(startDirection) {
            binding.flImgContainerLeft.visibility = View.VISIBLE
            binding.flFileContainerleft.visibility = View.VISIBLE
            for(attachment in list) {
                val fileView = createAttachmentView(attachment, context)
                if(attachment.type == Attachment.TYPE_IMAGE) {
                    binding.flImgContainerLeft.addView(fileView)
                    binding.flImgContainerLeft.backgroundTintList = leftBackGround
                }else {
                    binding.flFileContainerleft.addView(fileView)
                    binding.flFileContainerleft.backgroundTintList = leftBackGround
                }
            }
        }else {
            binding.flImgContainerRight.visibility = View.VISIBLE
            binding.flFileContainerRight.visibility = View.VISIBLE
            for(attachment in list) {
                val fileView = createAttachmentView(attachment, context)
                if(attachment.type == Attachment.TYPE_IMAGE) {
                    binding.flImgContainerRight.addView(fileView)
                    binding.flImgContainerRight.backgroundTintList = rightBackGround
                }else {
                    binding.flFileContainerRight.addView(fileView)
                    binding.flFileContainerRight.backgroundTintList = rightBackGround
                }
            }
        }

    }

    fun createAttachmentView(attachment: Attachment, context: Context) : View {
        val imageView = ImageView(context)
        when (attachment.type) {
            Attachment.TYPE_IMAGE -> {
                Glide.with(context).load(attachment.filePath).into(imageView)
            }
            Attachment.TYPE_FILE -> {
                imageView.setImageResource(R.drawable.folder_zip_24px)
            }
            else -> imageView.setImageResource(attachment.getFileDisplay())
        }
        val layoutParams = when (attachment.type) {
            Attachment.TYPE_IMAGE -> {
                FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    resources.getDimensionPixelSize(R.dimen.img_height_file_message)
                )
            }
            else -> {
                FlexboxLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.file_height_file_message),
                    resources.getDimensionPixelSize(R.dimen.file_height_file_message)
                )
            }
        }

        layoutParams.setMargins(
            resources.getDimensionPixelSize(R.dimen.file_message_margin_horizontal),
            resources.getDimensionPixelSize(R.dimen.file_message_margin_vertical),
            resources.getDimensionPixelSize(R.dimen.file_message_margin_horizontal),
            resources.getDimensionPixelSize(R.dimen.file_message_margin_vertical)
        )
        imageView.layoutParams = layoutParams

        if(attachment.type != Attachment.TYPE_IMAGE) {
            val wrapperText = LinearLayout(context)
            wrapperText.orientation = LinearLayout.VERTICAL
            wrapperText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val wrapper = LinearLayout(context)
            wrapper.orientation = LinearLayout.HORIZONTAL
            wrapper.layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            val fileName = TextView(context)
            fileName.text = attachment.fileName
            fileName.layoutParams = FlexboxLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.file_name_message_width),
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            fileName.maxLines = 1
            fileName.ellipsize = TextUtils.TruncateAt.MIDDLE
            val fileSize = TextView(context)
            fileSize.text = attachment.fileSize
            fileSize.layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            wrapperText.addView(fileName)
            wrapperText.addView(fileSize)
            wrapper.addView(imageView)
            wrapper.addView(wrapperText)
            wrapper.gravity = Gravity.CENTER_VERTICAL
            wrapper.setOnClickListener {
                attachment.filePath?.let {
                    FileHelper.downloadFile(attachment.filePath, context, attachment.fileName!!)
                }

            }
            return wrapper
        }
        imageView.setOnClickListener {
            FileHelper.showFullScreenImage(attachment.filePath!!,context, attachment.fileName!!)
        }
        return imageView
    }



}