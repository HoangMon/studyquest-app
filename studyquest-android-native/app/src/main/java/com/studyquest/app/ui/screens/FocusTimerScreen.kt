package com.studyquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyquest.app.ui.theme.BlueLight
import com.studyquest.app.ui.theme.BluePrimary
import com.studyquest.app.ui.theme.BorderSubtle
import com.studyquest.app.ui.theme.NavyPrimary
import com.studyquest.app.ui.theme.RedCoral
import com.studyquest.app.ui.theme.RedLight
import com.studyquest.app.ui.theme.SurfaceCard
import com.studyquest.app.ui.theme.TextMuted
import com.studyquest.app.viewmodel.TimerUiState

@Composable
fun FocusTimerScreen(
    timerState: TimerUiState,
    onToggleTimer: () -> Unit,
    onRestOption: () -> Unit,
    onContinueOption: () -> Unit,
    onExit: () -> Unit
) {
    val minutes = (timerState.remainingSeconds / 60).toString().padStart(2, '0')
    val seconds = (timerState.remainingSeconds % 60).toString().padStart(2, '0')
    val progress = (timerState.remainingSeconds.toFloat() / timerState.totalSeconds.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar with Close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onExit) {
                Text(
                    text = "✕ Thoát",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Target Objective Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFBAE6FD))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯 MỤC TIÊU HIỆN TẠI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BluePrimary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timerState.activeTaskTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Circular Timer Display
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background ring
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = BorderSubtle,
                strokeWidth = 12.dp
            )
            // Animated Progress ring
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = BluePrimary,
                strokeWidth = 12.dp
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$minutes:$seconds",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    color = NavyPrimary
                )
                Text(
                    text = if (timerState.mode == "5min") "QUY TẮC 5 PHÚT\nKHỞI ĐỘNG" else "POMODORO 25M\nTẬP TRUNG",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Friendly Note / Encouragement
        Text(
            text = timerState.promptMessage,
            fontSize = 12.sp,
            color = Color(0xFF0369A1),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F9FF))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Pause / Resume Button
        Button(
            onClick = onToggleTimer,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
        ) {
            Text(
                text = if (timerState.isRunning) "⏸ TẠM DỪNG" else "▶ TIẾP TỤC",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Two Decision Buttons (Nghỉ ngơi vs Làm tiếp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Nghỉ ngơi button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(RedLight)
                    .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(14.dp))
                    .clickable { onRestOption() }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔴", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Nghỉ ngơi", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFB91C1C))
                        Text("(Không có EXP)", fontSize = 10.sp, color = Color(0xFFB91C1C).copy(alpha = 0.8f))
                    }
                }
            }

            // Làm tiếp button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BluePrimary)
                    .clickable { onContinueOption() }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔥", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Làm tiếp", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                        Text("(Vào 25m • +50 EXP)", fontSize = 10.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }
        }
    }
}
