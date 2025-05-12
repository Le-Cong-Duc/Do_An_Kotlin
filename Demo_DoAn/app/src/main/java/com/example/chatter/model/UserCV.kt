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
    val dateInterView: String? = "",
    val date: String? = "",
    val createdAt: Long = System.currentTimeMillis()
)
