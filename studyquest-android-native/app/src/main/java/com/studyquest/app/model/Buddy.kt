package com.studyquest.app.model

data class Buddy(
    val handle: String = "@Minh",
    val isOnline: Boolean = true,
    val completedTasks: Int = 2,
    val totalTasks: Int = 3,
    val currentFocusTask: String = "Viết dàn ý đề cương chi tiết (25m)",
    val lastPingMessage: String = "Mình vừa giải quyết xong bài tập lớn Pháp luật ĐC rồi nhé, cố lên!"
) {
    val completionFraction: Float
        get() = (completedTasks.toFloat() / totalTasks.toFloat()).coerceIn(0f, 1f)
}
