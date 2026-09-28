package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.UserRole
import com.example.ui.theme.SmaranGreenContainer
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary

@Composable
fun SmaranTopBar(
    title: String,
    currentScreen: ScreenDestination,
    currentRole: UserRole,
    voiceReadingEnabled: Boolean,
    onBack: (() -> Unit)?,
    onToggleVoice: () -> Unit,
    onOpenNavigator: () -> Unit,
    onRoleClick: () -> Unit,
    rightCustomAction: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF7FAF7),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = SmaranTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SmaranGreenPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "NER Elders Care • Memory Companion",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (rightCustomAction != null) {
                    rightCustomAction()
                }

                // Voice reading quick toggle
                IconButton(
                    onClick = onToggleVoice,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (voiceReadingEnabled) SmaranGreenContainer else Color(0xFFEEEEEE))
                        .testTag("toggle_voice_reading_button")
                ) {
                    Icon(
                        imageVector = if (voiceReadingEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = "Toggle Voice Reading",
                        tint = if (voiceReadingEnabled) SmaranGreenPrimary else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Role badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (currentRole) {
                        UserRole.PATIENT -> Color(0xFFE8F5E9)
                        UserRole.CAREGIVER -> Color(0xFFE1F5FE)
                        UserRole.ASHA_WORKER -> Color(0xFFFFF3E0)
                    },
                    modifier = Modifier
                        .clickable { onRoleClick() }
                        .testTag("current_role_badge")
                ) {
                    Text(
                        text = currentRole.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (currentRole) {
                            UserRole.PATIENT -> Color(0xFF2E7D32)
                            UserRole.CAREGIVER -> Color(0xFF0277BD)
                            UserRole.ASHA_WORKER -> Color(0xFFE65100)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }

                // Screen Selector button (Image 1 navigator)
                IconButton(
                    onClick = onOpenNavigator,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E7E3))
                        .testTag("open_screen_navigator_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "All Screens Navigator",
                        tint = SmaranGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
