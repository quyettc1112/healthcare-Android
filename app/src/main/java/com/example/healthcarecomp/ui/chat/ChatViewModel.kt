package com.example.healthcarecomp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
): BaseViewModel() {
    val chatRooms = MutableLiveData<Resource<MutableList<ChatRoom>>>()
    val chatRoomUpsert = MutableLiveData<Resource<ChatRoom>>()
    private lateinit var _userId: String

    operator fun invoke(userId: String){
        _userId = userId
        onChatRoomChange()
    }

    fun onChatRoomChange() = viewModelScope.launch {
        chatUseCase.onChatRoomChange(_userId) {
            chatRooms.value = it
        }
    }

    fun upsert(chatRoom: ChatRoom) = viewModelScope.launch {
        chatRoomUpsert.value = chatUseCase.upsert(chatRoom)
    }
}