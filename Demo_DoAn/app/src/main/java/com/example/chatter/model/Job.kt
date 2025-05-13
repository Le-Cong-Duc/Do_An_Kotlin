package com.example.chatter.model

data class Job(
    var id: String? = "",
    val title: String? = "",
    val skills: List<String>? = emptyList(),
    val experience: String? = "",
    val salary: Int? = 0,
    val address: String? = "",
    val company: String? = "",
    val gender: String? = "",
    val age: String? = "",
    val education: String? = "",
    val jobType: String? = "",
    val workingForm: String? = "",
    val category: String? = ""
)
