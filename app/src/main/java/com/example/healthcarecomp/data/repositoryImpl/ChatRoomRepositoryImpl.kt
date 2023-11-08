package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.ChatRoom
import com.example.healthcarecomp.data.repository.ChatRoomRepository
import com.example.healthcarecomp.util.Resource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChatRoomRepositoryImpl @Inject constructor(
    private val fireBaseDatabase: FirebaseDatabase
) : ChatRoomRepository {
    private val charRoomRef =
        fireBaseDatabase.reference.child(Constant.ChatRoomQuery.PATH.queryField)

    override suspend fun onFirstUserChange(
        userId: String,
        listener: (Resource<ChatRoom>) -> Unit
    ) {
        val query = charRoomRef.orderByChild(Constant.ChatRoomQuery.FIRST_USER_ID.queryField)
            .equalTo(userId)
        fetch(query, listener)
    }

    override suspend fun onSecondUserChange(
        userId: String,
        listener: (Resource<ChatRoom>) -> Unit
    ) {
        val query2 = charRoomRef.orderByChild(Constant.ChatRoomQuery.SECOND_USER_ID.queryField)
            .equalTo(userId)
        fetch(query2, listener)
    }

    override suspend fun onChatRoomLoad(
        userId: String,
        listener: (Resource<ChatRoom>) -> Unit,
        updateListener: (Resource<ChatRoom>) -> Unit
    ) {
        charRoomRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatRoom = snapshot.getValue(ChatRoom::class.java)
                chatRoom?.let {
                    if (chatRoom?.firstUserId == userId || chatRoom.secondUserId == userId) {
                        listener.let {
                            it(Resource.Success(chatRoom))
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatRoom = snapshot.getValue(ChatRoom::class.java)
                chatRoom?.let {
                    if (chatRoom?.firstUserId == userId || chatRoom.secondUserId == userId) {
                        updateListener(Resource.Success(chatRoom))
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                listener?.let {
                    it(Resource.Error(error.message))
                }
            }

        })
    }

    override suspend fun upsert(chatRoom: ChatRoom): Resource<ChatRoom> {
        return suspendCoroutine { continuation ->
            charRoomRef.child(chatRoom.id).setValue(chatRoom).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Resource.Success(chatRoom))
                } else {
                    continuation.resume(Resource.Error("Not found"))
                }
            }.addOnFailureListener {
                continuation.resume(Resource.Error(it.message))
            }
        }
    }

    private fun fetch(query: Query, listener: (Resource<ChatRoom>) -> Unit) {
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val chatRoom = it.getValue(ChatRoom::class.java)
                    chatRoom?.let {
                        charRoomRef.child(chatRoom.id)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val chatItem = snapshot.getValue(ChatRoom::class.java)
                                    if (chatItem != null) {
                                        listener?.let {
                                            Log.i("humm", "load ")
                                            listener(Resource.Success(chatItem))
                                        }
                                    } else {
                                        listener?.let {
                                            listener(Resource.Error("Item null"))
                                        }
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

            }

            override fun onCancelled(error: DatabaseError) {
                listener?.let {
                    it(Resource.Error(error.message))
                }
            }

        })
    }
}