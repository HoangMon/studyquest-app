package com.studyquest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.studyquest.app.ui.components.NavScreen
import com.studyquest.app.ui.components.StudyQuestBottomBar
import com.studyquest.app.ui.screens.BuddyShopScreen
import com.studyquest.app.ui.screens.FocusTimerScreen
import com.studyquest.app.ui.screens.HomeScreen
import com.studyquest.app.ui.screens.KanbanScreen
import com.studyquest.app.ui.theme.StudyQuestTheme
import com.studyquest.app.viewmodel.StudyQuestViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StudyQuestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyQuestTheme {
                StudyQuestApp(viewModel)
            }
        }
    }
}

@Composable
fun StudyQuestApp(viewModel: StudyQuestViewModel) {
    var currentScreen by remember { mutableStateOf(NavScreen.HOME) }

    val userProfile by viewModel.userProfile.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val buddy by viewModel.buddy.collectAsState()
    val rewards by viewModel.rewards.collectAsState()

    Scaffold(
        bottomBar = {
            StudyQuestBottomBar(
                currentScreen = currentScreen,
                onNavigate = { currentScreen = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                NavScreen.HOME -> {
                    HomeScreen(
                        userProfile = userProfile,
                        tasks = tasks,
                        onStart5MinRule = {
                            viewModel.start5MinuteRule()
                            currentScreen = NavScreen.FOCUS
                        },
                        onToggleTask = { viewModel.toggleTask(it) },
                        onStartTaskFocus = { taskTitle ->
                            viewModel.start5MinuteRule(taskTitle)
                            currentScreen = NavScreen.FOCUS
                        }
                    )
                }

                NavScreen.FOCUS -> {
                    FocusTimerScreen(
                        timerState = timerState,
                        onToggleTimer = { viewModel.toggleTimer() },
                        onRestOption = {
                            viewModel.chooseRestOption()
                            currentScreen = NavScreen.HOME
                        },
                        onContinueOption = {
                            viewModel.chooseContinueOption()
                        },
                        onExit = {
                            currentScreen = NavScreen.HOME
                        }
                    )
                }

                NavScreen.TASKS -> {
                    KanbanScreen(
                        tasks = tasks,
                        onMoveTask = { id, status -> viewModel.moveTask(id, status) }
                    )
                }

                NavScreen.TEAM_SHOP -> {
                    BuddyShopScreen(
                        userProfile = userProfile,
                        buddy = buddy,
                        rewards = rewards,
                        onSendPing = { viewModel.sendPing(it) },
                        onRedeemReward = { viewModel.redeemReward(it) }
                    )
                }
            }
        }
    }
}
