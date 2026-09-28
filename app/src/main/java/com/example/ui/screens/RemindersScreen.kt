package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.entity.MedicineReminder
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary

@Composable
fun RemindersScreen(
    reminders: List<MedicineReminder>,
    onToggleTaken: (Int, Boolean) -> Unit,
    onAddMedicine: (String, String, String, String, String) -> Unit,
    onDeleteMedicine: (Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
            .padding(16.dp)
            .testTag("reminders_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Row with Title and + Add
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Medicines",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = SmaranGreenPrimary
                    )
                    Text(
                        text = "Timely reminders with clear pill colors and audible chimes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4B5563),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                TextButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.testTag("btn_add_medicine_header")
                ) {
                    Text(
                        text = "+ Add",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                }
            }
        }

        // Reminders List
        items(reminders, key = { it.id }) { reminder ->
            MedicineReminderCard(
                reminder = reminder,
                onToggleTaken = { onToggleTaken(reminder.id, reminder.isTaken) },
                onDelete = { onDeleteMedicine(reminder.id) }
            )
        }

        if (reminders.isEmpty()) {
            item {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "💊", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No reminders scheduled",
                            fontWeight = FontWeight.Bold,
                            color = SmaranTextPrimary
                        )
                        Text(
                            text = "Tap '+ Add' above to schedule a daily medicine reminder.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMedicineDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, dosage, time, meal, cat ->
                onAddMedicine(name, dosage, time, meal, cat)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun MedicineReminderCard(
    reminder: MedicineReminder,
    onToggleTaken: () -> Unit,
    onDelete: () -> Unit
) {
    val iconEmoji = when (reminder.category.lowercase()) {
        "tablet" -> "💊"
        "drops" -> "🧪"
        "walk" -> "🚶"
        else -> "🍵"
    }

    val iconBgColor = when (reminder.category.lowercase()) {
        "tablet" -> Color(0xFFFDECEF)
        "drops" -> Color(0xFFE8F5E9)
        "walk" -> Color(0xFFFFF3E0)
        else -> Color(0xFFEDE7F6)
    }

    val cardBorderColor = if (reminder.isTaken) Color(0xFFC7E2D1) else Color(0xFFE5E7EB)
    val cardBg = if (reminder.isTaken) Color(0xFFF4FAF6) else Color.White

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(2.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, cardBorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("med_card_${reminder.id}")
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Timing Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${reminder.timeString} • ${reminder.mealTiming}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(iconBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = iconEmoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = reminder.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SmaranTextPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = reminder.dosage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Taken status button
                if (reminder.isTaken) {
                    Button(
                        onClick = onToggleTaken,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5AB68A)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("btn_taken_${reminder.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Taken",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onToggleTaken,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF135034)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("btn_mark_taken_${reminder.id}")
                    ) {
                        Text(
                            text = "Mark Taken",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF135034)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, dosage: String, time: String, meal: String, category: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("08:30 AM") }
    var meal by remember { mutableStateOf("After Breakfast") }
    var category by remember { mutableStateOf("tablet") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            name.trim(),
                            if (dosage.isBlank()) "Standard dose" else dosage.trim(),
                            time,
                            meal,
                            category
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SmaranGreenPrimary),
                modifier = Modifier.testTag("dialog_save_medicine_button")
            ) {
                Text("Save Reminder")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                text = "Add Medicine Reminder",
                fontWeight = FontWeight.Bold,
                color = SmaranGreenPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medicine Name (e.g. Telmisartan)") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_med_name")
                )

                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage / Instructions (e.g. 40mg with water)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_med_dosage")
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 08:30 AM)") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = meal,
                    onValueChange = { meal = it },
                    label = { Text("Timing (e.g. After Breakfast, After Lunch)") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("tablet" to "💊 Tablet", "drops" to "🧪 Drops", "walk" to "🚶 Walk")
                    categories.forEach { (catKey, catLabel) ->
                        val isSel = category == catKey
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { category = catKey },
                            color = if (isSel) SmaranGreenPrimary else Color(0xFFF3F4F6),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = catLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) Color.White else Color(0xFF374151),
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    )
}
