package com.example.healthcarecomp.ui.viewProfile

import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewProfileViewModel @Inject constructor(
    private val useCase: ViewProfileUserCase
) : BaseViewModel() {

    fun findChatRoom(
        userId: String,
        partnerId: String,
        onChatRoomFound: (ChatRoom) -> Unit,
        onChatRoomNotFound: () -> Unit
    ) = viewModelScope.launch {
        useCase.findChatRoom(userId, partnerId, onChatRoomFound, onChatRoomNotFound)
    }

    fun upset(chatRoom: ChatRoom, onSuccess: (ChatRoom) -> Unit) = viewModelScope.launch {
        val result = useCase.upsert(chatRoom)
        if(result is Resource.Success){
            onSuccess(result.data!!)
        }
    }
}