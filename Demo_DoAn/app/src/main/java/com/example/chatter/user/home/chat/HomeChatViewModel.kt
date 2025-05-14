package com.example.chatter.user.home.chat

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
class HomeChatViewModel @Inject constructor() : ViewModel() {
    private val firebaseDatabase = Firebase.database

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val user = _users.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        firebaseDatabase.getReference("user").get().addOnSuccessListener {
            val list = mutableListOf<User>()
            it.children.forEach { data ->
                val id = data.key!!
                val name = data.child("name").value.toString()
                val email = data.child("email").value.toString()
                val role = data.child("role").getValue(Boolean::class.java) ?: false

                list.add(User(id = id, name = name, email = email, role = role))
            }

            _users.value = list

        }.addOnFailureListener {
            Log.e("FIREBASE", "Error loading data", it)
        }
    }

}