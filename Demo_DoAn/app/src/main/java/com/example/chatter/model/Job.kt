package com.example.chatter.model

data class Job(
    val id: Int,
    val title: String,
    val skills: List<String>,
    val experience: String,
    val salary: String,
    val location: String,
    val company: String
)