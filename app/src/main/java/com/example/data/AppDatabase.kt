package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AshaDao
import com.example.data.dao.CognitiveDao
import com.example.data.dao.MedicineDao
import com.example.data.dao.MoodDao
import com.example.data.entity.AshaVisitRecord
import com.example.data.entity.MedicineReminder
import com.example.data.entity.MemoryGameScore
import com.example.data.entity.MoodRecord
import com.example.data.entity.OrientationQuizResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MedicineReminder::class,
        OrientationQuizResult::class,
        MemoryGameScore::class,
        AshaVisitRecord::class,
        MoodRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun cognitiveDao(): CognitiveDao
    abstract fun ashaDao(): AshaDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smaran_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            val medDao = db.medicineDao()
            val cogDao = db.cognitiveDao()
            val ashaDao = db.ashaDao()
            val moodDao = db.moodDao()

            // Pre-seed demo reminders
            medDao.insertReminders(
                listOf(
                    MedicineReminder(
                        name = "Morning Blood Pressure Tablet",
                        dosage = "Telmisartan 40mg • Take after breakfast",
                        timeString = "08:30 AM",
                        mealTiming = "After Breakfast",
                        category = "tablet",
                        isTaken = true,
                        lastTakenTimestamp = System.currentTimeMillis() - 3600000L
                    ),
                    MedicineReminder(
                        name = "Memory & Neuro Support Drops",
                        dosage = "B-Complex + Brahmi extract • 10 drops in water",
                        timeString = "01:30 PM",
                        mealTiming = "After Lunch",
                        category = "drops",
                        isTaken = false
                    ),
                    MedicineReminder(
                        name = "Evening Courtyard Walk & Tea",
                        dosage = "15 minutes gentle stroll in the garden",
                        timeString = "05:00 PM",
                        mealTiming = "Evening Snack",
                        category = "walk",
                        isTaken = false
                    )
                )
            )

            // Pre-seed 7-day cognitive performance trend records
            val now = System.currentTimeMillis()
            val day = 86400000L
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now - 6 * day, score = 4, answersSummary = "Good orientation"))
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now - 5 * day, score = 5, answersSummary = "Perfect recall"))
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now - 4 * day, score = 4, answersSummary = "Mild delay"))
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now - 3 * day, score = 5, answersSummary = "Excellent"))
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now - 2 * day, score = 4, answersSummary = "Recognized Guwahati home"))
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now - 1 * day, score = 5, answersSummary = "Bihu music stimulation"))
            cogDao.insertQuizResult(OrientationQuizResult(timestamp = now, score = 5, answersSummary = "Current day orientation correct"))

            cogDao.insertGameScore(MemoryGameScore(timestamp = now - 2 * day, difficulty = "Standard 2x3", moves = 10, timeTakenSeconds = 48, stars = 3))
            cogDao.insertGameScore(MemoryGameScore(timestamp = now - 1 * day, difficulty = "Standard 2x3", moves = 8, timeTakenSeconds = 38, stars = 3))
            cogDao.insertGameScore(MemoryGameScore(timestamp = now, difficulty = "Gentle 2x2", moves = 4, timeTakenSeconds = 18, stars = 3))

            // Pre-seed ASHA visit record
            ashaDao.insertVisit(
                AshaVisitRecord(
                    timestamp = now - 2 * day,
                    patientName = "Bhaben Baruah (74y, M)",
                    bpSystolic = 128,
                    bpDiastolic = 82,
                    pulse = 72,
                    spo2 = 98,
                    bloodSugar = "110 mg/dL",
                    medicineSupplyDays = 18,
                    gameAdherenceRating = "Regular (Daily)",
                    caregiverFatigue = "Low (Son Rahul supportive)",
                    nutritionStatus = "Adequate (Local Assamese diet)",
                    notes = "Patient cheerful, recognizes daughter-in-law Priya and grandkid Meera. Memory match helps.",
                    isSyncedToNhm = true
                )
            )

            // Pre-seed Mood
            moodDao.insertMood(MoodRecord(timestamp = now - day, mood = "Peaceful", note = "Morning listening to Bihu flute"))
            moodDao.insertMood(MoodRecord(timestamp = now, mood = "Peaceful", note = "Feeling calm after tea"))
        }
    }
}
