package com.studyquest.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyquest.app.model.Task
import com.studyquest.app.model.TaskStatus
import com.studyquest.app.ui.theme.BlueLight
import com.studyquest.app.ui.theme.BluePrimary
import com.studyquest.app.ui.theme.BorderSubtle
import com.studyquest.app.ui.theme.GoldExp
import com.studyquest.app.ui.theme.GreenSuccess
import com.studyquest.app.ui.theme.NavyPrimary
import com.studyquest.app.ui.theme.SurfaceCard
import com.studyquest.app.ui.theme.TextMain
import com.studyquest.app.ui.theme.TextMuted

@Composable
fun KanbanScreen(
    tasks: List<Task>,
    onMoveTask: (Long, TaskStatus) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val statuses = listOf(TaskStatus.TODO, TaskStatus.DOING, TaskStatus.DONE)
    val tabTitles = listOf("Cần làm", "Đang làm", "Xong")

    val currentTasks = tasks.filter { it.status == statuses[selectedTab] }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BẢNG KANBAN",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = NavyPrimary
                )
                Text(
                    text = "Bẻ nhỏ task 25 phút • Nhận 50 EXP",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceCard,
            contentColor = BluePrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
        ) {
            tabTitles.forEachIndexed { index, title ->
                val count = tasks.count { it.status == statuses[index] }
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = "$title ($count)",
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Task Items in Selected Column
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(currentTasks) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextMain
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${task.subject} • 25m",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (task.status != TaskStatus.TODO) {
                                    OutlinedButton(
                                        onClick = { onMoveTask(task.id, TaskStatus.TODO) },
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text("◀ Cần", fontSize = 10.sp)
                                    }
                                }
                                if (task.status != TaskStatus.DOING) {
                                    OutlinedButton(
                                        onClick = { onMoveTask(task.id, TaskStatus.DOING) },
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text("⚡ Làm", fontSize = 10.sp)
                                    }
                                }
                                if (task.status != TaskStatus.DONE) {
                                    Button(
                                        onClick = { onMoveTask(task.id, TaskStatus.DONE) },
                                        modifier = Modifier.height(28.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text("✔ Xong", fontSize = 10.sp, color = Color.White)
                                    }
                                }
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
}
