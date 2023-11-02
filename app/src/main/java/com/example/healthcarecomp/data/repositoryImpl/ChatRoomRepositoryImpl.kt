package com.example.healthcarecomp.data.repositoryImpl

import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val fireBaseDatabase: FirebaseDatabase
) : ChatRoomRepository {
    private val charRoomRef = fireBaseDatabase.reference.child(Constant.ChatRoomQuery.PATH.queryField)

    override suspend fun onChatRoomChange(
        userId: String,
        listener: (Resource<MutableList<ChatRoom>>) -> Unit
    ) {
        val query = charRoomRef.orderByChild(Constant.ChatRoomQuery.ID.queryField).equalTo(userId)
        fetch(query,listener)
    }

    override suspend fun upsert(chatRoom: ChatRoom): Resource<ChatRoom> {
        var result: Resource<ChatRoom> = Resource.Unknown()
        try {
            charRoomRef.child(chatRoom.id).setValue(chatRoom).addOnCompleteListener {task ->
                if(task.isSuccessful){
                    result = Resource.Success(chatRoom)
                }else{
                    result = Resource.Error("Not found")
                }
            }.addOnFailureListener {
                result = Resource.Error(it.message)
            }
        }catch (e: Exception){
            result = Resource.Error(e.message)
        }
        return result
    }

    private fun fetch(query: Query, listener: (Resource<MutableList<ChatRoom>>) -> Unit) {
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ChatRoom>()
                snapshot.children.forEach {
                   val chatRoom =  it.getValue(ChatRoom::class.java)
                    chatRoom?.let { room ->
                        list.add(room)
                    }
                }
                listener?.let {
                    list.sortByDescending {room ->
                        room.lastActiveTime
                    }
                    it(Resource.Success(list))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listener?.let {
                    it(Resource.Error(error.message))
                }
            }

        })
    }
}