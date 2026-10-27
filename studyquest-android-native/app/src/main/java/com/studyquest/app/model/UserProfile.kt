package com.studyquest.app.model

data class UserProfile(
    val name: String = "Minh",
    val level: Int = 2,
    val levelTitle: String = "Thợ Săn Deadline",
    val currentExp: Int = 150,
    val maxExp: Int = 300,
    val availableExp: Int = 200, // Quỹ giải trí khả dụng: 200 EXP = 40 phút
    val streakDays: Int = 4
) {
    val restFundMinutes: Int
        get() = availableExp / 5

    val progressFraction: Float
        get() = (currentExp.toFloat() / maxExp.toFloat()).coerceIn(0f, 1f)
}
