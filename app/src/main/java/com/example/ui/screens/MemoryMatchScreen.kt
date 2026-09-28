package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SmaranGreenContainer
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary
import com.example.ui.viewmodel.MemoryCard

@Composable
fun MemoryMatchScreen(
    cards: List<MemoryCard>,
    moves: Int,
    matchedPairsCount: Int,
    elapsedSeconds: Int,
    difficulty: String,
    isCompleted: Boolean,
    onCardClicked: (Int) -> Unit,
    onSelectDifficulty: (String) -> Unit,
    onRestartGame: () -> Unit,
    onBackToGames: () -> Unit
) {
    val totalPairs = cards.size / 2
    val columns = when (difficulty) {
        "Gentle 2x2" -> 2
        "Standard 2x3" -> 3
        else -> 3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
            .padding(16.dp)
            .testTag("memory_match_screen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Difficulty Tabs & Stats
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val levels = listOf("Gentle 2x2", "Standard 2x3", "Full 3x4")
                levels.forEach { level ->
                    val isSelected = difficulty == level
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectDifficulty(level) }
                            .testTag("diff_tab_${level.replace(" ", "_")}"),
                        color = if (isSelected) SmaranGreenPrimary else Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = level,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF4B5563),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Score Tracker Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Moves", fontSize = 11.sp, color = Color.Gray)
                        Text("$moves", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SmaranTextPrimary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Pairs", fontSize = 11.sp, color = Color.Gray)
                        Text("$matchedPairsCount / $totalPairs", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SmaranGreenPrimary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Time", fontSize = 11.sp, color = Color.Gray)
                        val mins = elapsedSeconds / 60
                        val secs = elapsedSeconds % 60
                        Text(String.format("%02d:%02d", mins, secs), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SmaranTextPrimary)
                    }

                    IconButton(
                        onClick = onRestartGame,
                        modifier = Modifier.testTag("restart_memory_game_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = SmaranGreenPrimary)
                    }
                }
            }
        }

        // Cards Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                FlipCardItem(
                    card = card,
                    onClick = { onCardClicked(index) },
                    testTag = "card_$index"
                )
            }
        }

        // Bottom status note
        Text(
            text = "Gentle Tip: Take your time, Baruah Deuta. Pairing familiar Assam symbols exercises the hippocampus.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )
    }

    // Celebratory Dialog
    if (isCompleted) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(
                    onClick = onRestartGame,
                    colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary),
                    modifier = Modifier.testTag("play_again_button")
                ) {
                    Text("Play Again • আকৌ খেলক")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onBackToGames,
                    modifier = Modifier.testTag("finish_game_button")
                ) {
                    Text("Games Menu")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎉 Xabaax Deuta! (Great Job!)", fontWeight = FontWeight.Bold, color = SmaranGreenPrimary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "You paired all $totalPairs NER heritage symbols in $moves moves and ${elapsedSeconds}s.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(28.dp))
                        }
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ Performance score saved to Son Rahul's Caregiver Dashboard.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1B5E20),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun FlipCardItem(
    card: MemoryCard,
    onClick: () -> Unit,
    testTag: String
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 350)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = !card.isFlipped && !card.isMatched) { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (card.isMatched) Color(0xFFD1E7DD) else if (card.isFlipped) Color.White else Color(0xFF1B5E20)
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Card Back (Assam Gamusa Pattern / Leaf motif)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🍃", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "স্মৰণ",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFA5D6A7),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Card Front (Flipped)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = card.iconEmoji,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SmaranTextPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                    Text(
                        text = card.subtext,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        color = Color(0xFF2E7D32),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
