package com.studyquest.app.model

enum class TaskStatus {
    TODO,
    DOING,
    DONE
}

data class Task(
    val id: Long,
    val title: String,
    val subject: String,
    val durationMinutes: Int = 25,
    val expReward: Int = 50,
    val status: TaskStatus = TaskStatus.TODO,
    val inTop3: Boolean = false
)
