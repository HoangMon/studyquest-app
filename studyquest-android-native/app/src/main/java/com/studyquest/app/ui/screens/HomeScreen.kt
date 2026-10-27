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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyquest.app.model.Task
import com.studyquest.app.model.TaskStatus
import com.studyquest.app.model.UserProfile
import com.studyquest.app.ui.theme.BlueLight
import com.studyquest.app.ui.theme.BluePrimary
import com.studyquest.app.ui.theme.BorderSubtle
import com.studyquest.app.ui.theme.GoldExp
import com.studyquest.app.ui.theme.GreenSuccess
import com.studyquest.app.ui.theme.NavyDeep
import com.studyquest.app.ui.theme.NavyPrimary
import com.studyquest.app.ui.theme.SurfaceCard
import com.studyquest.app.ui.theme.TextMain
import com.studyquest.app.ui.theme.TextMuted

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    tasks: List<Task>,
    onStart5MinRule: () -> Unit,
    onToggleTask: (Long) -> Unit,
    onStartTaskFocus: (String) -> Unit
) {
    val top3Tasks = tasks.filter { it.inTop3 }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Header: STUDY QUEST & Profile Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(BluePrimary, NavyPrimary))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "STUDY QUEST",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyPrimary
                        )
                        Text(
                            text = "Anti-Procrastination & Focus",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Profile Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(SurfaceCard)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(BlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("M", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(text = userProfile.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyPrimary)
                        Text(text = "${userProfile.levelTitle} (Lv. ${userProfile.level})", fontSize = 10.sp, color = BluePrimary)
                    }
                }
            }
        }

        // EXP Progress Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TIẾN ĐỘ EXP",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = NavyPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${userProfile.currentExp}/${userProfile.maxExp} EXP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = BluePrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { userProfile.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = BluePrimary,
                        trackColor = BorderSubtle,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quỹ nghỉ: ${userProfile.restFundMinutes} phút (${userProfile.availableExp} EXP)",
                            fontSize = 12.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "🔥 ${userProfile.streakDays} ngày",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldExp
                        )
                    }
                }
            }
        }

        // Hero 5-Minute Rule CTA Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0E3B64), Color(0xFF0369A1), Color(0xFF0284C7))
                        )
                    )
                    .clickable { onStart5MinRule() }
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⏱️", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "QUY TẮC 5 PHÚT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White.copy(alpha = 0.85f),
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "BẮT ĐẦU 5 PHÚT NGAY",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "(Chỉ cần mở file và viết 1 dòng)",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Top 3 Focus Tasks Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📌 HÔM NAY (TOP 3 FOCUS TASKS)",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = NavyPrimary
                )
                Text(
                    text = "${top3Tasks.count { it.status == TaskStatus.DONE }}/${top3Tasks.size} Task",
                    fontSize = 11.sp,
                    color = BluePrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Task Items
        items(top3Tasks) { task ->
            val isDone = task.status == TaskStatus.DONE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDone) Color(0xFFF8FAFC) else SurfaceCard
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Checkbox
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isDone) GreenSuccess else Color.Transparent)
                            .border(2.dp, if (isDone) GreenSuccess else Color(0xFF94A3B8), RoundedCornerShape(6.dp))
                            .clickable { onToggleTask(task.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDone) TextMuted else TextMain,
                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "(${task.subject} - ${task.durationMinutes}m - +${task.expReward} EXP)",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    if (!isDone) {
                        IconButton(
                            onClick = { onStartTaskFocus(task.title) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(BlueLight)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start",
                                tint = BluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
