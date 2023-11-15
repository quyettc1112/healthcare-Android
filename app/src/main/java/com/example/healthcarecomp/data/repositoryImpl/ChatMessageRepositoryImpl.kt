package com.example.healthcarecomp.data.repositoryImpl

import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.healthcarecomp.common.Constant
import com.example.healthcarecomp.data.model.Message
import com.example.healthcarecomp.data.repository.ChatMessageRepository
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

class ChatMessageRepositoryImpl @Inject constructor(
    private val fireBaseDatabase: FirebaseDatabase
) : ChatMessageRepository {
    private val chatMessageRef =
        fireBaseDatabase.reference.child(Constant.ChatMessageQuery.PATH.queryField)

    override suspend fun upsert(message: Message): Resource<Message> {
        return suspendCoroutine { continuation ->
            chatMessageRef.child(message.chatRoomId!!).child(message.timeStamp.toString()).setValue(message)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Resource.Success(message))
                    } else {
                        continuation.resume(Resource.Error("unknown error"))
                    }
                }.addOnFailureListener {
                    continuation.resume(Resource.Error(it.message))
                }
        }
    }

    override suspend fun onChatLoad(
        chatRoomId: String,
        addListener: (Resource<Message>) -> Unit,
        updateListener: ((Resource<Message>) -> Unit)?,
        deleteListener: ((Resource<Message>) -> Unit)?
    ) {
        chatMessageRef.child(chatRoomId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    addListener(Resource.Success(message))
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    updateListener?.let {
                        it(Resource.Success(message))
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    deleteListener?.let {
                        it(Resource.Success(message))
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override suspend fun getLastMessage(chatRoomId: String, listener: (Resource<Message?>) -> Unit) {
        chatMessageRef.child(chatRoomId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.children.count() != 0){
                    val lastMessage =  snapshot.children.last()
                    val message = lastMessage.getValue(Message::class.java)
                    message?.let {
                        listener(Resource.Success(message))
                    }
                }else {
                    listener(Resource.Success(null))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                listener(Resource.Error(error.message))
            }

        })
    }


}