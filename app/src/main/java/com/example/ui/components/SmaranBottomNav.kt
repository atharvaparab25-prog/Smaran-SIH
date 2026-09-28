package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BottomNavItem
import com.example.model.ScreenDestination
import com.example.ui.theme.SmaranGreenPrimary

data class NavItemSpec(
    val item: BottomNavItem,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
)

@Composable
fun SmaranBottomNav(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit,
    onOpenNavigator: () -> Unit = {}
) {
    val items = listOf(
        NavItemSpec(BottomNavItem.HOME, Icons.Filled.Home, Icons.Outlined.Home, "Home"),
        NavItemSpec(BottomNavItem.GAMES, Icons.Filled.Psychology, Icons.Outlined.Psychology, "Games"),
        NavItemSpec(BottomNavItem.SMARAN_AI, Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, "Smaran AI"),
        NavItemSpec(BottomNavItem.REMINDERS, Icons.Filled.MedicalServices, Icons.Outlined.MedicalServices, "Reminders"),
        NavItemSpec(BottomNavItem.SETTINGS, Icons.Filled.Settings, Icons.Outlined.Settings, "Settings")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(thickness = 0.8.dp, color = Color(0xFFE5E7EB))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { spec ->
                    val isSelected = when (spec.item) {
                        BottomNavItem.HOME -> currentScreen == ScreenDestination.PATIENT_HOME
                        BottomNavItem.GAMES -> currentScreen == ScreenDestination.GAMES_MENU ||
                                currentScreen == ScreenDestination.MEMORY_MATCH ||
                                currentScreen == ScreenDestination.ORIENTATION_QUIZ
                        BottomNavItem.SMARAN_AI -> currentScreen == ScreenDestination.AI_CHAT
                        BottomNavItem.REMINDERS -> currentScreen == ScreenDestination.REMINDERS ||
                                currentScreen == ScreenDestination.MANAGE_REMINDERS
                        BottomNavItem.SETTINGS -> currentScreen == ScreenDestination.SETTINGS_API ||
                                currentScreen == ScreenDestination.LANGUAGE_ACCESS
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigate(spec.item.destination) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("bottom_nav_${spec.label.lowercase()}")
                    ) {
                        Icon(
                            imageVector = if (isSelected) spec.filledIcon else spec.outlinedIcon,
                            contentDescription = spec.label,
                            tint = if (isSelected) SmaranGreenPrimary else Color(0xFF6B7280),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = spec.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SmaranGreenPrimary else Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}
