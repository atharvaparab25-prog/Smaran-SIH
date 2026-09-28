package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary

@Composable
fun GamesMenuScreen(
    onPlayMemoryMatch: () -> Unit,
    onStartOrientationQuiz: () -> Unit,
    onOpenFamilyFaces: () -> Unit,
    onPlayBihuAudio: () -> Unit
) {
    var showBihuDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
            .padding(16.dp)
            .testTag("games_menu_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Pleasant brain exercises designed to strengthen memory and focus.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF374151),
                lineHeight = 22.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Card 1: Memory Match: NER Heritage
        item {
            GameMenuCard(
                emoji = "🦏",
                iconBg = Color(0xFFFCE4EC),
                title = "Memory Match:\nNER Heritage",
                description = "Flip and pair familiar symbols of Assam, Kaziranga, and home.",
                buttonText = "PLAY",
                buttonTag = "btn_play_memory_match",
                onClick = onPlayMemoryMatch
            )
        }

        // Card 2: Daily Orientation Quiz
        item {
            GameMenuCard(
                emoji = "🗓️",
                iconBg = Color(0xFFFFF9C4),
                title = "Daily Orientation\nQuiz",
                description = "Gentle Mini-Cog check: Day, season, and peaceful surroundings.",
                buttonText = "START",
                buttonTag = "btn_start_orientation_quiz",
                onClick = onStartOrientationQuiz
            )
        }

        // Card 3: Bihu Melody & Rhyme Recall
        item {
            GameMenuCard(
                emoji = "🎵",
                iconBg = Color(0xFFEDE7F6),
                title = "Bihu Melody &\nRhyme Recall",
                description = "Hum along with traditional folk rhythms to stimulate memory.",
                buttonText = "LISTEN",
                buttonTag = "btn_listen_bihu",
                onClick = {
                    onPlayBihuAudio()
                    showBihuDialog = true
                }
            )
        }

        // Card 4: Family Face Recognition
        item {
            GameMenuCard(
                emoji = "🖼️",
                iconBg = Color(0xFFE8F5E9),
                title = "Family Face\nRecognition",
                description = "Match names and pleasant memories with your children and grandchildren.",
                buttonText = "VIEW",
                buttonTag = "btn_view_family_faces",
                onClick = onOpenFamilyFaces
            )
        }
    }

    if (showBihuDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showBihuDialog = false },
            confirmButton = {
                Button(
                    onClick = { showBihuDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary)
                ) {
                    Text("Close with Smiles")
                }
            },
            title = {
                Text(
                    text = "🪕 Bihu Melody & Folk Rhythm",
                    fontWeight = FontWeight.Bold,
                    color = SmaranGreenPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Music directly stimulates deep autobiographical memory in dementia patients.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Surface(
                        color = Color(0xFFF1F8E9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "‘অ’ মোৰ আপোনাৰ দেশ...’\n(O Mur Apunar Desh)",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Rhythm: Dhol and Pepa harmonic hum.\nNotice the feelings of warmth and pride in Bhaben Deuta's eyes.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun GameMenuCard(
    emoji: String,
    iconBg: Color,
    title: String,
    description: String,
    buttonText: String,
    buttonTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SmaranTextPrimary,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F4E3B)),
                shape = RoundedCornerShape(18.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                modifier = Modifier.testTag(buttonTag)
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}
