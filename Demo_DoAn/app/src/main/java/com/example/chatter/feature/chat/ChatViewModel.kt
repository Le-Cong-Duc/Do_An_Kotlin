package com.example.chatter.feature.chat

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
// context đại diện cho mtruong ứng dụng giúp truy cập tài nguyên như file, csdl
class ChatViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val message = _messages.asStateFlow()

    val db = Firebase.database

    //Lấy dữ liệu tin nhắn từ database dựa theo id
    fun getListenMessage(userId: String) {
        db.getReference("message").child(userId).orderByChild("createAt")
            .addValueEventListener(object : ValueEventListener {
                //onDataChange : đại diện cho dữ liệu tin nhắn của user
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        // chuyển phần tử snapshot thành đối tượng message
                        val message = data.getValue(Message::class.java)
                        // nếu dữ liệu không rỗng thì thêm vào list
                        message?.let {
                            list.add(it)
                        }
                    }
                    _messages.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun sendMesssage(userId: String, messageText: String?, image: String? = null) {
        // Tạo 1 biến để chứa dữ liệu tin nhắn dựa theo class Message
        val message = Message(
            // firebase tự động tạo khóa cho mỗi lần push tin nhắn, nếu không tạo khóa thì UUID.randomUUID().toString() sẽ tạo 1 khóa ngẫu nhiên
            db.reference.push().key ?: UUID.randomUUID().toString(),
            //ấy uid(user id) ừ firebase auth để xác nhận người nhắn
            Firebase.auth.currentUser?.uid ?: "",
            messageText,
            System.currentTimeMillis(),
            // Lấy tên người gửi
            Firebase.auth.currentUser?.displayName ?: "",
            null,
            image
        )
        // đẩy tin nhắn lên firebase tại node "message" dựa trên userId
        // push() tạo 1 id duy nhất cho mỗi tin nhắn đảm bảo không trùng lặp id
        // setValue() gửi đối tượng lên message lên firebase
        db.getReference("message").child(userId).push()
            .setValue(message)
    }


    fun sendImageMessage(uri: Uri, userId: String) {
        viewModelScope.launch {
            //Tạo 1 biến gọi class liên kết supabase
            val storageUtils = SupabbaseStorageUtils(context)
            // phương thức tải ảnh lên supabase
            // uri: đường dẫn ảnh đã chọn từ thiết bị
            val downloadUri = storageUtils.upLoadImage(uri)
            // nếu đường đẫn không phải null thì sử dụng phương thức sendMessage để gửi tin nhắn
            downloadUri?.let {
                sendMesssage(userId, null, downloadUri.toString())
            }
        }
    }

}