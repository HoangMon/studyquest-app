package com.studyquest.app.model

data class Reward(
    val id: String,
    val iconEmoji: String,
    val title: String,
    val durationMinutes: Int,
    val expCost: Int
)
