package com.example.healthcarecomp.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.model.User
import com.example.healthcarecomp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : BaseViewModel() {
    val chatRoomUpsert = MutableLiveData<Resource<ChatRoom>>()
    val chatRooms = MutableLiveData<Resource<MutableList<Triple<ChatRoom, User, Message?>>>>()
    private lateinit var listener: (Triple<ChatRoom, User, Message?>) -> Unit
    private lateinit var _userId: String

    operator fun invoke(userId: String, listener: (Triple<ChatRoom, User, Message?>) -> Unit) {
        _userId = userId
        this.listener = listener
        onChatRoomLoad()
    }

    // this function will add event when load and update
    // chatUseCase have 3 parameter:
    // 1: userId: is current user login in system to load chat box user owner
    // 2: add listener: is event when load each chat room of user
    // 3: update listener: is event when chat room update
    // when load success chat room , set that in runBlocking to wait to load who message
    fun onChatRoomLoad() = viewModelScope.launch {
        chatRooms.value = Resource.Loading()
        viewModelScope.launch {
            chatUseCase.onChatRoomLoad(_userId, { resource ->
                if (resource is Resource.Success) {
                    if (chatRooms.value is Resource.Loading) {
                        chatRooms.value = Resource.Success(mutableListOf())
                    }
                    viewModelScope.launch {
                        if (resource?.data!!.firstUserId == _userId) {
                            chatUseCase.getUserById(resource.data?.secondUserId!!) { userResource ->
                                userResource?.data?.let { user ->
                                    viewModelScope.launch {
                                        chatUseCase.getLastMessage(resource?.data?.id!!) {
                                            if (it is Resource.Success) {
                                                addData(resource.data!!, user, it.data)
                                            }
                                        }
                                    }

                                }
                            }
                        } else {
                            chatUseCase.getUserById(resource.data?.firstUserId!!) { userResource ->
                                userResource?.data?.let { user ->
                                    viewModelScope.launch {
                                        chatUseCase.getLastMessage(resource?.data?.id!!) {
                                            if (it is Resource.Success) {
                                                addData(resource.data!!, user, it.data)
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }, { resource ->
                viewModelScope.launch {
                    if (resource?.data!!.firstUserId == _userId) {
                        chatUseCase.getUserById(resource.data?.secondUserId!!) { userResource ->
                            userResource?.data?.let { user ->
                                viewModelScope.launch {
                                    chatUseCase.getLastMessage(resource?.data?.id!!) {
                                        if (it is Resource.Success) {
                                            updateData(resource.data!!, user, it.data)
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        chatUseCase.getUserById(resource.data?.firstUserId!!) { userResource ->
                            userResource?.data?.let { user ->
                                viewModelScope.launch {
                                    chatUseCase.getLastMessage(resource?.data?.id!!) {
                                        if (it is Resource.Success) {
                                            updateData(resource.data!!, user, it.data)
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
            }
            )
        }

    }

    private fun addData(chat: ChatRoom, user: User, message: Message?) {
        chatRooms.value = chatRooms.value.apply {
            this?.data?.add(Triple(chat, user, message))
        }
    }

    private fun updateData(chat: ChatRoom, user: User, message: Message?) {
        chatRooms.value = chatRooms.value.apply {
            val index = this?.data?.indexOfFirst {
                it.first.id == chat.id
            }
            index?.let {
                this?.data?.set(it, Triple(chat, user, message))
            }

        }
    }


    fun upsert(chatRoom: ChatRoom) = viewModelScope.launch {
        chatRoomUpsert.value = chatUseCase.upsert(chatRoom)
    }
}