package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_reminders")
data class MedicineReminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val timeString: String,
    val mealTiming: String,
    val category: String, // "tablet", "drops", "walk"
    val isTaken: Boolean = false,
    val lastTakenTimestamp: Long? = null
)

@Entity(tableName = "orientation_quiz_results")
data class OrientationQuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val score: Int,
    val totalQuestions: Int = 5,
    val answersSummary: String = ""
)

@Entity(tableName = "memory_game_scores")
data class MemoryGameScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val difficulty: String,
    val moves: Int,
    val timeTakenSeconds: Int,
    val stars: Int
)

@Entity(tableName = "asha_visit_records")
data class AshaVisitRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val patientName: String = "Bhaben Baruah",
    val bpSystolic: Int = 128,
    val bpDiastolic: Int = 82,
    val pulse: Int = 72,
    val spo2: Int = 98,
    val bloodSugar: String = "110 mg/dL",
    val medicineSupplyDays: Int = 18,
    val gameAdherenceRating: String = "Good (3x/wk)",
    val caregiverFatigue: String = "Low",
    val nutritionStatus: String = "Healthy",
    val notes: String = "Patient calm, enjoys afternoon memory match.",
    val isSyncedToNhm: Boolean = true
)

@Entity(tableName = "mood_records")
data class MoodRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String, // "Peaceful", "Okay", "Need Comfort"
    val note: String = ""
)
