package com.example.data.repository

import com.example.data.AppDatabase
import com.example.data.entity.AshaVisitRecord
import com.example.data.entity.MedicineReminder
import com.example.data.entity.MemoryGameScore
import com.example.data.entity.MoodRecord
import com.example.data.entity.OrientationQuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SmaranRepository(private val database: AppDatabase) {

    private val medicineDao = database.medicineDao()
    private val cognitiveDao = database.cognitiveDao()
    private val ashaDao = database.ashaDao()
    private val moodDao = database.moodDao()

    val allReminders: Flow<List<MedicineReminder>> = medicineDao.getAllReminders()
    val allQuizResults: Flow<List<OrientationQuizResult>> = cognitiveDao.getAllQuizResults()
    val allGameScores: Flow<List<MemoryGameScore>> = cognitiveDao.getAllGameScores()
    val allVisits: Flow<List<AshaVisitRecord>> = ashaDao.getAllVisits()
    val allMoods: Flow<List<MoodRecord>> = moodDao.getAllMoods()

    suspend fun toggleMedicineTaken(id: Int, currentTaken: Boolean) {
        val newTaken = !currentTaken
        val timestamp = if (newTaken) System.currentTimeMillis() else null
        medicineDao.updateTakenStatus(id, newTaken, timestamp)
    }

    suspend fun addReminder(reminder: MedicineReminder) {
        medicineDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: MedicineReminder) {
        medicineDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(id: Int) {
        medicineDao.deleteReminderById(id)
    }

    suspend fun saveQuizResult(score: Int, total: Int, summary: String) {
        cognitiveDao.insertQuizResult(
            OrientationQuizResult(
                score = score,
                totalQuestions = total,
                answersSummary = summary
            )
        )
    }

    suspend fun saveGameScore(difficulty: String, moves: Int, seconds: Int, stars: Int) {
        cognitiveDao.insertGameScore(
            MemoryGameScore(
                difficulty = difficulty,
                moves = moves,
                timeTakenSeconds = seconds,
                stars = stars
            )
        )
    }

    suspend fun addAshaVisit(visit: AshaVisitRecord) {
        ashaDao.insertVisit(visit)
    }

    suspend fun markVisitSynced(id: Int) {
        ashaDao.markSynced(id)
    }

    suspend fun recordMood(mood: String, note: String = "") {
        moodDao.insertMood(MoodRecord(mood = mood, note = note))
    }

    suspend fun ensureDefaultDataSeeded() {
        val reminders = medicineDao.getAllReminders().first()
        if (reminders.isEmpty()) {
            AppDatabase.populateInitialData(database)
        }
    }
}
