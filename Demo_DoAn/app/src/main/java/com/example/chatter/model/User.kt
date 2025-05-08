package com.example.chatter.model

data class User(
    val id: String = "",
    val name: String,
    val email: String,
    val role: Any?,
    val createAt: Long = System.currentTimeMillis()
)