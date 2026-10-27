package com.studyquest.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyquest.app.model.Buddy
import com.studyquest.app.model.Reward
import com.studyquest.app.model.Task
import com.studyquest.app.model.TaskStatus
import com.studyquest.app.model.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerUiState(
    val mode: String = "5min", // "5min" | "25min" | "break"
    val remainingSeconds: Int = 298, // 04:58 initial
    val totalSeconds: Int = 300,
    val isRunning: Boolean = false,
    val activeTaskTitle: String = "Viết dàn ý đề cương chi tiết",
    val activeTaskMeta: String = "Tư duy luận ĐC • 25m • +50 EXP",
    val promptMessage: String = "Bạn chỉ cần làm trong 5 phút, hết giờ, bạn hoàn toàn có quyền dừng lại mà không có tội gì."
)

class StudyQuestViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                id = 1L,
                title = "Tìm 3 tài liệu tiểu luận",
                subject = "Pháp luật ĐC",
                durationMinutes = 25,
                expReward = 50,
                status = TaskStatus.DONE,
                inTop3 = true
            ),
            Task(
                id = 2L,
                title = "Viết dàn ý đề cương chi tiết",
                subject = "Tư duy luận ĐC",
                durationMinutes = 25,
                expReward = 50,
                status = TaskStatus.DOING,
                inTop3 = true
            ),
            Task(
                id = 3L,
                title = "Đọc và tóm tắt chương 1",
                subject = "Kinh tế chính trị",
                durationMinutes = 25,
                expReward = 50,
                status = TaskStatus.TODO,
                inTop3 = true
            ),
            Task(
                id = 4L,
                title = "Viết 300 từ phần mở đầu",
                subject = "Pháp luật ĐC",
                durationMinutes = 25,
                expReward = 50,
                status = TaskStatus.TODO,
                inTop3 = false
            ),
            Task(
                id = 5L,
                title = "Rà soát lỗi chính tả & định dạng APA",
                subject = "Tư duy luận ĐC",
                durationMinutes = 25,
                expReward = 50,
                status = TaskStatus.TODO,
                inTop3 = false
            )
        )
    )
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _buddy = MutableStateFlow(Buddy())
    val buddy: StateFlow<Buddy> = _buddy.asStateFlow()

    private val _rewards = MutableStateFlow(
        listOf(
            Reward("game", "🎮", "Chơi 1 trận game", 20, 100),
            Reward("anime", "🎬", "Xem 1 tập phim anime", 25, 125),
            Reward("social", "☕", "Lướt MXH / Đi cà phê", 15, 75),
            Reward("music", "🎵", "Nghe nhạc thư giãn lofi", 10, 50)
        )
    )
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    private val _timerState = MutableStateFlow(TimerUiState())
    val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    fun start5MinuteRule(taskTitle: String? = null) {
        val title = taskTitle ?: _tasks.value.firstOrNull { it.status != TaskStatus.DONE }?.title ?: "Tập trung 5 phút khởi động"
        _timerState.value = TimerUiState(
            mode = "5min",
            remainingSeconds = 300,
            totalSeconds = 300,
            isRunning = true,
            activeTaskTitle = title,
            promptMessage = "Bạn chỉ cần làm trong 5 phút, hết giờ, bạn hoàn toàn có quyền dừng lại mà không có tội gì."
        )
        startTicking()
    }

    fun startPomodoro(taskTitle: String) {
        _timerState.value = TimerUiState(
            mode = "25min",
            remainingSeconds = 1500,
            totalSeconds = 1500,
            isRunning = true,
            activeTaskTitle = taskTitle,
            promptMessage = "Bạn đang trong trạng thái dòng chảy tập trung cao độ (+50 EXP)!"
        )
        startTicking()
    }

    fun toggleTimer() {
        if (_timerState.value.isRunning) {
            timerJob?.cancel()
            _timerState.update { it.copy(isRunning = false) }
        } else {
            _timerState.update { it.copy(isRunning = true) }
            startTicking()
        }
    }

    private fun startTicking() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000L)
                _timerState.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            if (_timerState.value.remainingSeconds <= 0) {
                onTimerFinished()
            }
        }
    }

    private fun onTimerFinished() {
        _timerState.update { it.copy(isRunning = false) }
        if (_timerState.value.mode == "25min") {
            awardExp(50)
        }
    }

    fun chooseRestOption() {
        timerJob?.cancel()
        _timerState.update { it.copy(isRunning = false) }
    }

    fun chooseContinueOption() {
        val currentTitle = _timerState.value.activeTaskTitle
        startPomodoro(currentTitle)
    }

    fun toggleTask(taskId: Long) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    val newStatus = if (task.status == TaskStatus.DONE) TaskStatus.TODO else TaskStatus.DONE
                    if (newStatus == TaskStatus.DONE) {
                        awardExp(task.expReward)
                    }
                    task.copy(status = newStatus)
                } else task
            }
        }
    }

    fun moveTask(taskId: Long, newStatus: TaskStatus) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    if (newStatus == TaskStatus.DONE && task.status != TaskStatus.DONE) {
                        awardExp(task.expReward)
                    }
                    task.copy(status = newStatus)
                } else task
            }
        }
    }

    fun redeemReward(reward: Reward): Boolean {
        if (_userProfile.value.availableExp >= reward.expCost) {
            _userProfile.update {
                it.copy(availableExp = it.availableExp - reward.expCost)
            }
            return true
        }
        return false
    }

    fun sendPing(message: String) {
        _buddy.update { it.copy(lastPingMessage = "Bạn: \"$message\"") }
        viewModelScope.launch {
            delay(2000L)
            _buddy.update {
                it.copy(lastPingMessage = "@Minh: \"Nhận được ping rồi! Đang cày deadline cùng bạn nè! 🔥\"")
            }
        }
    }

    private fun awardExp(amount: Int) {
        _userProfile.update { user ->
            val newCurrent = user.currentExp + amount
            val newAvailable = user.availableExp + amount
            if (newCurrent >= user.maxExp) {
                user.copy(
                    level = user.level + 1,
                    currentExp = newCurrent - user.maxExp,
                    availableExp = newAvailable,
                    levelTitle = "Chiến Thần Tập Trung"
                )
            } else {
                user.copy(
                    currentExp = newCurrent,
                    availableExp = newAvailable
                )
            }
        }
    }
}
