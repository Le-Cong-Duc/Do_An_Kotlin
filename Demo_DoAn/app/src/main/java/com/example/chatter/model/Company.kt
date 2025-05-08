package com.example.chatter.model

data class Company(
    val uid: String? = null,
    val companyName: String? = "",
    val phoneNumber: String? = "",
    val address: String? = "",
    val email: String? = ""
) {
    constructor() : this(null, "", "", "", "")
}