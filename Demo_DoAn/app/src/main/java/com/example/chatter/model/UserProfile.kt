package com.example.chatter.model

import java.time.LocalDate

data class UserProfile(
    var firstName: String = "",
    var lastName: String = "",
    var location: String = "",
    var birthDate: LocalDate? = null,
    var gender: String = "",
    var education: String = "",
    var experience: String = ""
)
