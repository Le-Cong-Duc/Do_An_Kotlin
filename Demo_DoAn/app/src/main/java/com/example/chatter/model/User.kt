package com.example.chatter.model

import java.time.LocalDate

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val numberPhone: String? = "",
    val role: Any? = null,
    var address: String? = "",
    var company: String = "",
    var birthDate: LocalDate? = null,
    var gender: String? = "",
    var education: String? = "",
    var experience: String? = "",
    val createAt: Long = System.currentTimeMillis()
)