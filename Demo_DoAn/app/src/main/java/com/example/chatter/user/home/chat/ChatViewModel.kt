package com.example.chatter.user.home.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatter.SupabbaseStorageUtils
import com.example.chatter.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val message = _messages.asStateFlow()

    private val db = Firebase.database

    private fun createChatRoomId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "chat_${userId1}_${userId2}"
        } else {
            "chat_${userId2}_${userId1}"
        }
    }

    fun getListenMessage(otherUserId: String) {
        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val chatRoomId = createChatRoomId(currentUserId, otherUserId)

        db.getReference("message").child(chatRoomId).orderByChild("createAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        message?.let {
                            list.add(it)
                        }
                    }
                    _messages.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun sendMesssage(otherUserId: String, messageText: String?, image: String? = null) {
        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val chatRoomId = createChatRoomId(currentUserId, otherUserId)

        val message = Message(
            db.reference.push().key ?: UUID.randomUUID().toString(),
            Firebase.auth.currentUser?.uid ?: "",
            messageText,
            System.currentTimeMillis(),
            Firebase.auth.currentUser?.displayName ?: "",
            null,
            image
        )

        db.getReference("message").child(chatRoomId).push()
            .setValue(message)
    }

    fun sendImageMessage(uri: Uri, otherUserId: String) {
        viewModelScope.launch {
            val storageUtils = SupabbaseStorageUtils(context)
            val downloadUri = storageUtils.upLoadImage(uri)
            downloadUri?.let {
                sendMesssage(otherUserId, null, downloadUri.toString())
            }
        }
    }

}