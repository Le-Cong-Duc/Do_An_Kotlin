package com.example.chatter.model

data class User(
    val id: String = "",
    val name: String,
    val createAt: Long = System.currentTimeMillis()
)