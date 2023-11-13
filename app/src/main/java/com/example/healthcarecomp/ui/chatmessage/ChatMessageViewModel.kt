package com.example.healthcarecomp.ui.chatmessage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
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
    lateinit var chatRoom: ChatRoom

    operator fun invoke(chatRoom: ChatRoom) {
        this.chatRoom = chatRoom
        onDataLoad()
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

    fun upsert(message: Message,context: Context? = null, sender: String? = null, to: String? = null) = viewModelScope.launch {
        messageSend.value = useCase.upsert(message)
        useCase.upsertChatRoom(chatRoom.copy(lastActiveTime = System.currentTimeMillis()))
        if(sender != null && to != null && context != null){
            // notification , topic is receiver who has subscribe this topic
            val notification = SendMessageRequest(
                message = MessageNotification(
                    notification = Notification(
                        title = sender,
                        body = message.content!!,
                    ),
                    topic = to
                )
            )
            useCase.sendNotification(notification,context)
        }

    }
}