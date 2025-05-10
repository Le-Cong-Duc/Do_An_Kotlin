package com.example.chatter.model

data class UserCV(
    var id: String = "",
    val userId: String = "",
    val name: String = "",
    val gender: String = "",
    val education: String = "",
    val phone: String = "",
    val experience: String = "",
    val skills: List<String> = emptyList(),
    val cvUrl: String = "",
    val jobId: String = "",
    val jobTitle: String = "",
    val status: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
