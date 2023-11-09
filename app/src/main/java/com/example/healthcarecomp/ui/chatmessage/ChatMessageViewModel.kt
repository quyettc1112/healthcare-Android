package com.example.healthcarecomp.ui.chatmessage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatMessageViewModel @Inject constructor(
    private val useCase: ChatMessageUseCase
) : BaseViewModel(){
    val messageSend  =  MutableLiveData<Resource<Message>>()
    val messageList = MutableLiveData<Resource<MutableList<Message>>>()
    var chatRoomId = ""

    operator fun invoke(chatRoomId: String) {
        this.chatRoomId = chatRoomId
        onDataLoad()
    }

    fun onDataLoad() = viewModelScope.launch {
        messageList.value = Resource.Loading()

        useCase.onChatLoad(chatRoomId, { resource ->
            if(messageList.value is Resource.Loading){
                messageList.value = Resource.Success(mutableListOf())
            }
            if(resource is Resource.Success) {
                messageList.value = messageList.value.apply {
                    this?.data?.add(resource.data!!)

                }
            }

        }, {resource ->
            if(resource is Resource.Success){
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

    fun upsert(message: Message) = viewModelScope.launch {
        messageSend.value = useCase.upsert(message)
    }
}