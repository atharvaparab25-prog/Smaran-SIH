package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SmaranGreenContainer
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary

data class QuizQuestion(
    val prompt: String,
    val hint: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@Composable
fun OrientationQuizScreen(
    currentIndex: Int,
    score: Int,
    selectedOption: Int?,
    answerChecked: Boolean,
    isCompleted: Boolean,
    onAnswerSelected: (Int, Boolean) -> Unit,
    onNextQuestion: (Int) -> Unit,
    onRestartQuiz: () -> Unit,
    onBackToGames: () -> Unit
) {
    val questions = remember {
        listOf(
            QuizQuestion(
                prompt = "What day of the week is today?",
                hint = "Think about the middle of the week, right after Tuesday!",
                options = listOf("Monday", "Wednesday", "Saturday", "Sunday"),
                correctIndex = 1,
                explanation = "Today is Wednesday! Rahul took out the recyclables this morning."
            ),
            QuizQuestion(
                prompt = "Where are you enjoying your peaceful morning?",
                hint = "Look outside at the lovely pond and the fresh garden.",
                options = listOf("At home in Guwahati, near Dighalipukhuri", "In Mumbai airport", "In a hotel in Kolkata", "At a train station"),
                correctIndex = 0,
                explanation = "You are right at home in Guwahati, Assam, next to peaceful Dighalipukhuri."
            ),
            QuizQuestion(
                prompt = "Which season is it right now in Assam?",
                hint = "The festive autumn season with clear skies and pleasant breezes!",
                options = listOf("Monsoon rain", "Autumn (Xarat)", "Freezing Winter", "Blistering Summer"),
                correctIndex = 1,
                explanation = "It is Autumn (শৰৎকাল), the time for peaceful sunsets and fragrant flowers."
            ),
            QuizQuestion(
                prompt = "Who prepares your warm ginger tea every morning?",
                hint = "She lives with you and Rahul, taking wonderful care of our home.",
                options = listOf("A stranger", "Priya (your caring daughter-in-law)", "The mail carrier", "A neighbor"),
                correctIndex = 1,
                explanation = "Priya makes your ginger tea and keeps your medicine ready."
            ),
            QuizQuestion(
                prompt = "Do you recall the 3 words from this morning: Rhino, Tea Leaf, Brahmaputra?",
                hint = "All three are symbols of our beloved Assam!",
                options = listOf("Apple, Bicycle, House", "Rhino, Tea Leaf, Brahmaputra", "Shoe, Clock, Pencil", "River, Mountain, Cloud"),
                correctIndex = 1,
                explanation = "Excellent recall! Kaziranga Rhino, fresh Tea Leaf, and mighty Brahmaputra."
            )
        )
    }

    var showHint by remember { mutableStateOf(false) }

    if (isCompleted) {
        // Quiz Completion Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF8))
                .padding(24.dp)
                .testTag("orientation_quiz_completed_view"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFD1E7DD),
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🌟", fontSize = 42.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Orientation Check Complete!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = SmaranGreenPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Score: $score out of ${questions.size} Correct",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SmaranTextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Bhaben Deuta is well-oriented to home, family, and surroundings today. Your responses have been synced to the Caregiver Dashboard.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onRestartQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("quiz_retake_button"),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary)
            ) {
                Text("Take Again • পুনৰ চেষ্টা কৰক", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackToGames,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("quiz_back_to_games_button"),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Back to Games Menu", color = SmaranGreenPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
        return
    }

    val currentQ = questions[currentIndex]
    val progress = (currentIndex.toFloat() + 1f) / questions.size.toFloat()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
            .padding(18.dp)
            .testTag("orientation_quiz_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress Indicator
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SmaranGreenPrimary
                    )
                    Text(
                        text = "Current Score: $score",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SmaranGreenPrimary,
                    trackColor = Color(0xFFE5E7EB)
                )
            }
        }

        // Question Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = currentQ.prompt,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SmaranTextPrimary,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showHint = !showHint }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Hint",
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (showHint) "Hide Hint" else "Need a gentle hint?",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFF57C00)
                        )
                    }

                    if (showHint) {
                        Surface(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = "💡 ${currentQ.hint}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFB78103),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Options List
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                currentQ.options.forEachIndexed { idx, optionText ->
                    val isChosen = selectedOption == idx
                    val isCorrect = idx == currentQ.correctIndex

                    val cardBg = when {
                        !answerChecked -> if (isChosen) SmaranGreenContainer else Color.White
                        isChosen && isCorrect -> Color(0xFFD1E7DD)
                        isChosen && !isCorrect -> Color(0xFFFFCDD2)
                        isCorrect -> Color(0xFFE8F5E9)
                        else -> Color.White
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(enabled = !answerChecked) {
                                val correct = idx == currentQ.correctIndex
                                onAnswerSelected(idx, correct)
                            }
                            .testTag("quiz_option_$idx"),
                        color = cardBg,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isChosen) 2.dp else 1.dp,
                            color = if (isChosen) SmaranGreenPrimary else Color(0xFFE5E7EB)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                color = SmaranTextPrimary,
                                modifier = Modifier.weight(1f)
                            )

                            if (answerChecked && isCorrect) {
                                Icon(Icons.Default.Check, contentDescription = "Correct", tint = Color(0xFF0F5132))
                            } else if (answerChecked && isChosen && !isCorrect) {
                                Icon(Icons.Default.Close, contentDescription = "Incorrect", tint = Color(0xFFC62828))
                            }
                        }
                    }
                }
            }
        }

        // Next / Explanation
        if (answerChecked) {
            item {
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Insight:",
                            fontWeight = FontWeight.Bold,
                            color = SmaranGreenPrimary,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        showHint = false
                        onNextQuestion(questions.size)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("quiz_next_button"),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary)
                ) {
                    Text(
                        text = if (currentIndex + 1 < questions.size) "Next Question →" else "Finish Check →",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
