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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyquest.app.model.Buddy
import com.studyquest.app.model.Reward
import com.studyquest.app.model.UserProfile
import com.studyquest.app.ui.theme.BlueLight
import com.studyquest.app.ui.theme.BluePrimary
import com.studyquest.app.ui.theme.BorderSubtle
import com.studyquest.app.ui.theme.GoldExp
import com.studyquest.app.ui.theme.GreenLight
import com.studyquest.app.ui.theme.GreenSuccess
import com.studyquest.app.ui.theme.NavyPrimary
import com.studyquest.app.ui.theme.SurfaceCard
import com.studyquest.app.ui.theme.TextMain
import com.studyquest.app.ui.theme.TextMuted

@Composable
fun BuddyShopScreen(
    userProfile: UserProfile,
    buddy: Buddy,
    rewards: List<Reward>,
    onSendPing: (String) -> Unit,
    onRedeemReward: (Reward) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Section 1: GÓC ĐỒNG HÀNH
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💙", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GÓC ĐỒNG HÀNH",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                        }

                        // Online badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(GreenLight)
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(GreenSuccess)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Đang học", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Bạn đồng hành: ${buddy.handle} - 🟢 Đã hoàn thành: ${buddy.completedTasks}/${buddy.totalTasks} task",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMain
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { buddy.completionFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GreenSuccess,
                        trackColor = BorderSubtle
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ping button
                    Button(
                        onClick = { onSendPing("Cố lên bạn ơi, sắp xong bài rồi! 🔥") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Text(
                            text = "[ Gửi Ping ]",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Message box
                    Text(
                        text = buddy.lastPingMessage,
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(8.dp)
                    )
                }
            }
        }

        // Section 2: QUẦY ĐỔI GIẢI TRÍ "KHÔNG TỘI LỖI"
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎮", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "QUẦY ĐỔI GIẢI TRÍ \"KHÔNG TỘI LỖI\"",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = NavyPrimary
                            )
                            Text(
                                text = "Chìm khử dụng: ${userProfile.availableExp} EXP",
                                fontSize = 11.sp,
                                color = GoldExp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Rewards List
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        rewards.forEach { reward ->
                            RewardItemRow(
                                reward = reward,
                                canAfford = userProfile.availableExp >= reward.expCost,
                                onRedeem = { onRedeemReward(reward) }
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

@Composable
private fun RewardItemRow(
    reward: Reward,
    canAfford: Boolean,
    onRedeem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(reward.iconEmoji, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${reward.title} / ${reward.durationMinutes} phút",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = NavyPrimary
                )
                Text(
                    text = "(Chi phí: ${reward.expCost} EXP)",
                    fontSize = 11.sp,
                    color = GoldExp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onRedeem,
                enabled = canAfford,
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(34.dp)
            ) {
                Text(
                    text = "[ ĐỔI NGAY ]",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (canAfford) BluePrimary else TextMuted
                )
            }
        }
    }
}
