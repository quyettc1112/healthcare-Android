package com.example.healthcarecomp.ui.chatmessage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.Attachment
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.model.MessageNotification
import com.example.healthcarecomp.data.model.Notification
import com.example.healthcarecomp.data.model.SendMessageRequest
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatMessageViewModel @Inject constructor(
    private val useCase: ChatMessageUseCase
) : BaseViewModel() {
    val messageSend = MutableLiveData<Resource<Message>>()
    val messageList = MutableLiveData<Resource<MutableList<Message>>>()
    val selectedFileList = MutableLiveData<MutableList<Attachment>>()
    var fileUploaded = MutableLiveData<MutableList<Attachment>>()

    companion object {
        const val CHAT_MESSAGE_FOLDER = "ChatRooms"
    }

    lateinit var chatRoom: ChatRoom

    operator fun invoke(chatRoom: ChatRoom) {
        this.chatRoom = chatRoom
        onDataLoad()
        selectedFileList.value = mutableListOf()
    }

    fun onDataLoad() = viewModelScope.launch {
        messageList.value = Resource.Loading()

        useCase.onChatLoad(chatRoom.id, { resource ->
            if (messageList.value is Resource.Loading) {
                messageList.value = Resource.Success(mutableListOf())
            }
            if (resource is Resource.Success) {
                messageList.value = messageList.value.apply {
                    this?.data?.add(resource.data!!)
                }
            }

        }, { resource ->
            if (resource is Resource.Success) {
                messageList.value = messageList.value.apply {
                    val index = this?.data?.indexOfFirst {
                        it.timeStamp == resource.data?.timeStamp!!
                    }
                    index?.let {
                        this?.data?.set(index, resource.data!!)
                    }
                }
            }
        })
    }

    fun uploadFile() = viewModelScope.launch {
        val selectedList = selectedFileList.value
        fileUploaded = MutableLiveData<MutableList<Attachment>>()
        if (selectedList != null && selectedList.size!! > 0) {
            fileUploaded.value = mutableListOf()
            for (attachment in selectedList) {
                val folder = "$CHAT_MESSAGE_FOLDER/${chatRoom.id}"
                useCase.uploadFile(folder, Uri.parse(attachment.filePath), onFailure = { err ->
                    Log.i("err", "${err}")
                }, onSuccess = { uri ->
                    fileUploaded.value = fileUploaded.value.apply {
                        this?.add(
                            Attachment(
                                type = attachment.type, filePath = uri.toString()
                            )
                        )
                    }
                }, onProgress = {

                })
            }
        }
    }

    fun upsert(
        message: Message,
        context: Context? = null,
        sender: String? = null,
        to: String? = null
    ) = viewModelScope.launch {
        messageSend.value = useCase.upsert(message)
        useCase.upsertChatRoom(chatRoom.copy(lastActiveTime = System.currentTimeMillis()))
        if (sender != null && to != null && context != null) {
            // notification , topic is receiver who has subscribe this topic
            val notification = SendMessageRequest(
                message = MessageNotification(
                    notification = Notification(
                        title = sender,
                        body = if(message.content.isNullOrEmpty()) "Has seen a message" else message.content!!,
                    ),
                    topic = to
                )
            )
            useCase.sendNotification(notification, context)
        }
    }

    fun selectFile(type: String, filePath: String) {
        val file = Attachment(type = type, filePath = filePath)
        selectedFileList.value = selectedFileList.value.apply {
            this?.add(file)
        }
    }

    fun removeFile(file: Attachment) {
        selectedFileList.value = selectedFileList.value.apply {
            this?.remove(file)
        }
    }

}