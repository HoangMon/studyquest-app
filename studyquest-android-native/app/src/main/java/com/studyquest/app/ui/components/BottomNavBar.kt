package com.studyquest.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studyquest.app.ui.theme.BluePrimary
import com.studyquest.app.ui.theme.NavyPrimary
import com.studyquest.app.ui.theme.SurfaceCard
import com.studyquest.app.ui.theme.TextMuted

enum class NavScreen {
    HOME,
    FOCUS,
    TASKS,
    TEAM_SHOP
}

@Composable
fun StudyQuestBottomBar(
    currentScreen: NavScreen,
    onNavigate: (NavScreen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(SurfaceCard)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentScreen == NavScreen.HOME,
                onClick = { onNavigate(NavScreen.HOME) }
            )

            // Centered highlighted 5-Minute Focus button
            Box(contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = { onNavigate(NavScreen.FOCUS) },
                    modifier = Modifier
                        .offset(y = (-10).dp)
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(BluePrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "5 Phút",
                        tint = Color.White
                    )
                }
            }

            NavBarItem(
                icon = Icons.Default.DateRange,
                label = "Tasks",
                selected = currentScreen == NavScreen.TASKS,
                onClick = { onNavigate(NavScreen.TASKS) }
            )

            NavBarItem(
                icon = Icons.Default.Group,
                label = "Team / Shop",
                selected = currentScreen == NavScreen.TEAM_SHOP,
                onClick = { onNavigate(NavScreen.TEAM_SHOP) }
            )
        }
    }
}

@Composable
private fun NavBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) BluePrimary else TextMuted,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                color = if (selected) BluePrimary else TextMuted,
                fontSize = 11.sp
            )
        }
    }
}
