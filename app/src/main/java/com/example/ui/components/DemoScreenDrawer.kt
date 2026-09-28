package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary

data class ScreenMenuItem(
    val destination: ScreenDestination,
    val emoji: String,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreenDrawer(
    isOpen: Boolean,
    currentScreen: ScreenDestination,
    onSelectScreen: (ScreenDestination) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFFBFBF9),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ALL APP SCREENS NAVIGATOR",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF758A7E),
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Smaran Screen Navigator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SmaranGreenPrimary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_navigator_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close navigator",
                        tint = SmaranTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val sections = listOf(
                "ONBOARDING" to listOf(
                    ScreenMenuItem(ScreenDestination.SPLASH, "🌿", "Splash Screen"),
                    ScreenMenuItem(ScreenDestination.LANGUAGE_ACCESS, "🌐", "Language & Access"),
                    ScreenMenuItem(ScreenDestination.LOGIN_OTP, "🔐", "Login / OTP")
                ),
                "PATIENT APP" to listOf(
                    ScreenMenuItem(ScreenDestination.PATIENT_HOME, "🏠", "Patient Home"),
                    ScreenMenuItem(ScreenDestination.GAMES_MENU, "🧩", "Games Menu"),
                    ScreenMenuItem(ScreenDestination.MEMORY_MATCH, "🎴", "Memory Match (play)"),
                    ScreenMenuItem(ScreenDestination.ORIENTATION_QUIZ, "📝", "Daily Orientation Quiz"),
                    ScreenMenuItem(ScreenDestination.REMINDERS, "💊", "Reminders"),
                    ScreenMenuItem(ScreenDestination.AI_CHAT, "💬", "AI Companion Chat"),
                    ScreenMenuItem(ScreenDestination.MY_FAMILY, "👨‍👩‍👧", "My Family"),
                    ScreenMenuItem(ScreenDestination.SOS_ALERT, "🆘", "SOS Alert")
                ),
                "CAREGIVER / ASHA APP" to listOf(
                    ScreenMenuItem(ScreenDestination.CAREGIVER_DASHBOARD, "📊", "Caregiver Dashboard"),
                    ScreenMenuItem(ScreenDestination.COGNITIVE_TREND, "📈", "Cognitive Trend"),
                    ScreenMenuItem(ScreenDestination.MANAGE_REMINDERS, "✏️", "Manage Reminders"),
                    ScreenMenuItem(ScreenDestination.ASHA_FIELD_VISIT, "🩺", "ASHA Field Visit")
                ),
                "SHARED" to listOf(
                    ScreenMenuItem(ScreenDestination.SETTINGS_API, "⚙️", "Settings & API")
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(460.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                sections.forEach { (sectionHeader, itemsList) ->
                    item {
                        Text(
                            text = sectionHeader,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    items(itemsList) { item ->
                        val isSelected = currentScreen == item.destination

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onSelectScreen(item.destination)
                                }
                                .testTag("nav_item_${item.destination.name}"),
                            color = if (isSelected) Color(0xFF0F5132) else Color.White,
                            shape = RoundedCornerShape(12.dp),
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.emoji,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF1F2937),
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Text(
                                        text = "Active",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFA7F3D0),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
