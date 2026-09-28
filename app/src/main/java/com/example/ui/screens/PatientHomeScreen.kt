package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.MedicineReminder
import com.example.model.AppLanguage
import com.example.ui.theme.SmaranGreenContainer
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary
import com.example.util.SmaranStrings
import android.widget.Toast
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun PatientHomeScreen(
    patientName: String = "Bhaben Baruah",
    currentLanguage: AppLanguage = AppLanguage.ASSAMESE,
    currentMood: String,
    reminders: List<MedicineReminder>,
    onSelectMood: (String) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit = {},
    onOpenLanguageSettings: () -> Unit = {},
    onOpenGames: () -> Unit,
    onOpenMedicines: () -> Unit,
    onOpenAiChat: () -> Unit,
    onOpenFamily: () -> Unit,
    onOpenSos: () -> Unit,
    onPlayMemoryGame: () -> Unit,
    onSpeakText: (String) -> Unit = {},
    onToggleMedicine: (Int, Boolean) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    var showWhereAmIDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Live Clock State for time orientation
    var currentTimeString by remember {
        mutableStateOf(SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date()))
    }
    var dayPhaseKey by remember { mutableStateOf("Morning") }
    var dayPhaseEmoji by remember { mutableStateOf("☀️") }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance()
            currentTimeString = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(now.time)
            val hour = now.get(Calendar.HOUR_OF_DAY)
            when {
                hour in 5..11 -> {
                    dayPhaseKey = "Morning"
                    dayPhaseEmoji = "☀️"
                }
                hour in 12..16 -> {
                    dayPhaseKey = "Afternoon"
                    dayPhaseEmoji = "🌤️"
                }
                hour in 17..20 -> {
                    dayPhaseKey = "Evening"
                    dayPhaseEmoji = "🌇"
                }
                else -> {
                    dayPhaseKey = "Night"
                    dayPhaseEmoji = "🌙"
                }
            }
            delay(15000L)
        }
    }

    val todayFormatted = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH).format(Date())
    val pendingMeds = reminders.filter { !it.isTaken }
    val nextMed = pendingMeds.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAF7))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("patient_home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 0. COMPACT LANGUAGE SELECTION & ACCESSIBILITY BAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language Selection Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFC8E6C9)),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { showLanguageDialog = true }
                        .testTag("btn_language_select")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌐", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${currentLanguage.nativeName} (${currentLanguage.displayName})",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Change Language",
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Full Accessibility / Font Settings Button ("previous way")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenLanguageSettings() }
                        .testTag("btn_language_accessibility")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🗣️", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Audio & Font",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }
        }

        // 1. REASSURANCE & TIME ORIENTATION HERO (Dementia Anchor)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .testTag("greeting_banner"),
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFC8E6C9)),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Time and Day Phase Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFF1B5E20),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = dayPhaseEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = SmaranStrings.getPhaseOfDay(currentLanguage, dayPhaseKey),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Big Friendly Clock
                        Text(
                            text = currentTimeString,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F5132)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = SmaranStrings.getDashboardGreeting(currentLanguage, patientName),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0D3B23)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Explicit Day, Date and Location
                    Text(
                        text = "📅 $todayFormatted",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = SmaranStrings.getDashboardSubtitle(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Spoken Audio Assistance Button for the entire banner
                    Button(
                        onClick = {
                            val speech = "${SmaranStrings.getDashboardGreeting(currentLanguage, patientName)}. $currentTimeString. ${SmaranStrings.getPhaseOfDay(currentLanguage, dayPhaseKey)}. ${SmaranStrings.getDashboardSubtitle(currentLanguage)}."
                            onSpeakText(speech)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_read_orientation")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read aloud",
                            tint = Color(0xFF0F5132),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = SmaranStrings.getReadOrientationButton(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F5132)
                        )
                    }
                }
            }
        }

        // 2. "I FEEL CONFUSED / WHERE AM I?" REASSURANCE BUTTON
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        showWhereAmIDialog = true
                        onSpeakText(SmaranStrings.getWhereAmIDialogText(currentLanguage))
                    }
                    .testTag("btn_where_am_i"),
                color = Color(0xFFFFF8E1),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD54F)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "❓", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = SmaranStrings.getConfusedPromptTitle(currentLanguage),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB78103)
                            )
                            Text(
                                text = SmaranStrings.getConfusedPromptSubtitle(currentLanguage),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF8D6E14)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color(0xFFB78103),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 3. SINGLE FOCUS: "WHAT SHOULD I DO RIGHT NOW?" (Prevents Dementia Task Overload)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👉", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = SmaranStrings.getRightNowTitle(currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = SmaranTextPrimary
                            )
                        }
                        if (pendingMeds.isNotEmpty()) {
                            Surface(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${pendingMeds.size} pending today",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (nextMed != null) {
                        Surface(
                            color = Color(0xFFF0FDF4),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "⏰ Next Scheduled: ${nextMed.timeString} • ${nextMed.mealTiming}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = nextMed.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F5132)
                                )
                                Text(
                                    text = nextMed.dosage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4B5563)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { onToggleMedicine(nextMed.id, nextMed.isTaken) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .testTag("btn_mark_next_med_taken")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("I Have Taken This ✓", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            onSpeakText("Deuta, this is your ${nextMed.name}. ${nextMed.dosage}. Please take it after meal with water.")
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.height(48.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = SmaranGreenPrimary)
                                    }
                                }
                            }
                        }
                    } else {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🎉", fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "All Medicines Done for Today!",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = "Wonderful job, Deuta. Enjoy your tea and memory games.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. FAMILY REASSURANCE CARD: SON RAHUL
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE0F2FE),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "👨‍💼", fontSize = 28.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Son Rahul Baruah",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SmaranTextPrimary
                                )
                                Text(
                                    text = "Returns home by 6:00 PM • Guwahati",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF0369A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:+919864012345")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Fallback
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_call_rahul_home")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Rahul", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                onSpeakText("Deuta, this is Rahul! I am in Guwahati office and will be home at 6 PM. Priya has made tea for you. Please relax!")
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_hear_rahul_home")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hear Voice Note", fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 5. CALMING MOOD CHECK-IN (High-contrast, minimum 60dp touch target)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = SmaranStrings.getMoodTitle(currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SmaranTextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val moods = listOf(
                            Triple("Peaceful", "🌸", Color(0xFFFCE4EC)),
                            Triple("Happy", "😊", Color(0xFFE8F5E9)),
                            Triple("Confused", "❓", Color(0xFFFFF3E0))
                        )

                        moods.forEach { (moodName, emoji, bgColor) ->
                            val isSelected = currentMood == moodName

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(84.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable { onSelectMood(moodName) }
                                    .testTag("mood_button_${moodName.lowercase().replace(" ", "_")}"),
                                color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF9FAFB),
                                shape = RoundedCornerShape(18.dp),
                                border = if (isSelected) {
                                    androidx.compose.foundation.BorderStroke(2.5.dp, SmaranGreenPrimary)
                                } else {
                                    androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFE5E7EB))
                                }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = emoji,
                                        fontSize = 28.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = SmaranStrings.getMoodLabel(currentLanguage, moodName),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) SmaranGreenPrimary else Color(0xFF374151),
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. FOUR BIG ACTIVITY TILES (Dementia Sensory Color Coding)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Row 1: Brain Exercise & My Medicines
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DementiaQuickTile(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_brain_exercise"),
                        emoji = "🧩",
                        cardBg = Color(0xFFFAF5FF),
                        borderColor = Color(0xFFE9D5FF),
                        title = SmaranStrings.getPlayGamesTitle(currentLanguage),
                        subtitle = "Kaziranga & Bihu Games",
                        onClick = onOpenGames
                    )

                    DementiaQuickTile(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_my_medicines"),
                        emoji = "💊",
                        cardBg = Color(0xFFF0F9FF),
                        borderColor = Color(0xFFBAE6FD),
                        title = SmaranStrings.getMedicineSectionTitle(currentLanguage),
                        subtitle = if (pendingMeds.isNotEmpty()) "${pendingMeds.size} Pill Pending" else "All Taken Today ✓",
                        onClick = onOpenMedicines
                    )
                }

                // Row 2: Talk to Smaran & My Family
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DementiaQuickTile(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_talk_to_smaran"),
                        emoji = "💬",
                        cardBg = Color(0xFFF0FDF4),
                        borderColor = Color(0xFFBBF7D0),
                        title = SmaranStrings.getAiCompanionTitle(currentLanguage),
                        subtitle = "Friendly Voice Companion",
                        onClick = onOpenAiChat
                    )

                    DementiaQuickTile(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tile_my_family"),
                        emoji = "👨‍👩‍👧",
                        cardBg = Color(0xFFFFFBEB),
                        borderColor = Color(0xFFFDE68A),
                        title = SmaranStrings.getFamilyTitle(currentLanguage),
                        subtitle = "Photos & Voice Notes",
                        onClick = onOpenFamily
                    )
                }
            }
        }

        // 7. PROMINENT EMERGENCY SOS (Unmissable, Reassuring)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onOpenSos() }
                    .testTag("emergency_sos_banner"),
                color = Color(0xFFC62828),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "🚨",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = SmaranStrings.getSosButtonTitle(currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Alerts Family & ASHA Worker with GPS",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFFEBEE)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Alert",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // 8. SCHEDULE GLANCE (Clear & Minimal)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Rest of Your Day",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SmaranTextPrimary,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Schedule Item 1: Morning Tea & Tablet
                ScheduleCard(
                    emoji = "☕",
                    title = "Morning Tea & Telmisartan",
                    statusText = "Completed at 8:42 AM ✓",
                    isCompleted = true,
                    actionButton = null
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Schedule Item 2: Kaziranga Memory Game
                ScheduleCard(
                    emoji = "🦏",
                    title = "Kaziranga Memory Game",
                    statusText = "Recommended afternoon exercise",
                    isCompleted = false,
                    actionButton = {
                        Button(
                            onClick = onPlayMemoryGame,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary),
                            modifier = Modifier.testTag("schedule_play_now_button")
                        ) {
                            Text(
                                text = "Play",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Schedule Item 3: Evening Stroll
                ScheduleCard(
                    emoji = "🚶",
                    title = "Courtyard Walk with Rahul",
                    statusText = "05:00 PM • Peaceful garden stroll",
                    isCompleted = false,
                    actionButton = null
                )
            }
        }
    }

    // Reassurance Dialog for "Where Am I?"
    if (showWhereAmIDialog) {
        AlertDialog(
            onDismissRequest = { showWhereAmIDialog = false },
            confirmButton = {
                Button(
                    onClick = { showWhereAmIDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("🌸 I Feel Better Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:+919864012345")
                        }
                        try { context.startActivity(intent) } catch (e: Exception) {}
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Call Son Rahul")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(SmaranStrings.getWhereAmILabel(currentLanguage), fontWeight = FontWeight.Bold, color = SmaranGreenPrimary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = SmaranStrings.getWhereAmIDialogText(currentLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = SmaranTextPrimary
                    )
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "• Daughter-in-law Priya is in the kitchen preparing ginger tea.",
                                fontSize = 13.sp,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "• Son Rahul is working in Guwahati and will return at 6:00 PM.",
                                fontSize = 13.sp,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "• Granddaughter Meera is at school and returns at 3:30 PM.",
                                fontSize = 13.sp,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }
                }
            }
        )
    }

    // Language Selection Modal Dialog
    if (showLanguageDialog) {
        var dialogSelectedLang by remember(currentLanguage) { mutableStateOf(currentLanguage) }

        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌐", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Choose Language",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SmaranTextPrimary
                        )
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "NER & National",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Select your preferred regional language:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )

                    AppLanguage.values().forEach { lang ->
                        val isSelected = dialogSelectedLang == lang
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { dialogSelectedLang = lang }
                                .testTag("dialog_lang_${lang.code}"),
                            color = if (isSelected) Color(0xFFDCFCE7) else Color(0xFFF9FAFB),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Color(0xFF15803D) else Color(0xFFE5E7EB)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${lang.nativeName} (${lang.displayName})",
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF15803D) else Color(0xFF1F2937)
                                    )
                                    Text(
                                        text = lang.region,
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color(0xFF15803D),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = {
                            showLanguageDialog = false
                            onOpenLanguageSettings()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("⚙️ Full Audio & Text Settings", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onLanguageChange(dialogSelectedLang)
                        showLanguageDialog = false
                        Toast.makeText(
                            context,
                            SmaranStrings.getLanguageSavedToast(dialogSelectedLang),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_confirm_language_dialog")
                ) {
                    Text(
                        text = SmaranStrings.getSaveLanguageButton(dialogSelectedLang),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLanguageDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancel", color = Color(0xFF4B5563))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun DementiaQuickTile(
    modifier: Modifier = Modifier,
    emoji: String,
    cardBg: Color,
    borderColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 28.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = SmaranTextPrimary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4B5563),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ScheduleCard(
    emoji: String,
    title: String,
    statusText: String,
    isCompleted: Boolean,
    actionButton: (@Composable () -> Unit)?
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    color = Color(0xFFF3F4F6)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = emoji, fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SmaranTextPrimary
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isCompleted) Color(0xFF2E7D32) else Color(0xFF6B7280),
                        fontWeight = if (isCompleted) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }

            if (actionButton != null) {
                actionButton()
            }
        }
    }
}

