package com.example.chatter.AI

import com.example.chatter.model.UserCV

data class MatchResult(
    val cvId: String,
    val jobId: String,
    val score: Double,
    val cv: UserCV
)
