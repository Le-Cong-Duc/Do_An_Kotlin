package com.example.chatter.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.Exclude
import java.time.LocalDate

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val numberPhone: String? = "",
    val role: Any? = null,
    var address: String? = "",
    var company: String = "",
    var birthDate: String? = null,
    var gender: String? = "",
    var education: String? = "",
    var experience: String? = "",
    val createAt: Long = System.currentTimeMillis()
){
    @get:Exclude
    @set:Exclude
    var date: LocalDate?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = birthDate?.let { LocalDate.parse(it) }
        set(value) {
            birthDate = value?.toString()
        }
}