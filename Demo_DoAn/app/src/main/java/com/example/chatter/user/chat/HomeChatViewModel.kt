package com.example.chatter.user.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chatter.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
// @Inject constructor() -> giúp Hilt tự động tạo HomeViewModel mà không cần tạo thủ công (hiltViewModel<HomeChatViewModel>())
class HomeChatViewModel @Inject constructor() : ViewModel() {
    //Tạo biến kết nối với firebase realtime database
    private val firebaseDatabase = Firebase.database

    // Tạo một biến chưa danh sách các user trong database
    // StateFlow: có thể thay đổi - ViewModel sẽ tự động cập nhật
    private val _users = MutableStateFlow<List<User>>(emptyList())

    // Tạo một biến public dùng cho UI thông qua đảm bảo UI chỉ có thể đọc không thể thay đổi dữ liệu
    // asStateFlow : chuyển từ MutableStateFlow (có thể chỉnh sửa)  sang StateFlow ( chỉ đọc) : giúp dấu dữ liệu nội bộ
    val user = _users.asStateFlow()

    // init : chạy ngay đối tượng getUser khi chạy tới ViewModel này
    init {
        getUser()
    }

    // Tải dữ liệu từ firebase
    private fun getUser() {
        // truy cập vào node "user" trong database và lấy ra bằng get()
        firebaseDatabase.getReference("user").get().addOnSuccessListener {
            // Tạo list chưa danh sách user
            val list = mutableListOf<User>()
            // it là dữ liệu đọc được
            it.children.forEach { data ->
                // Lấy user trong dữ liệu
//                val user = User(
                val id = data.key!!
                val name = data.child("name").value.toString()
                val email = data.child("email").value.toString()
                val role = data.child("role").getValue(Boolean::class.java) ?: false
//                )

                // add vào list
                if (role == false) {
                    list.add(User(id = id, name = name, email = email, role = role))
                }
            }
            // gán list user vừa tạo được vào _users để có thể tự cập nhaatj được danh sách mới
            _users.value = list

        }.addOnFailureListener {
            Log.e("FIREBASE", "Error loading data", it)
        }
    }

}