package com.example.chatter.hr.AI

import com.example.chatter.model.UserCV

data class MatchResult(
    val userCV: UserCV,
    val score: Double,
)
