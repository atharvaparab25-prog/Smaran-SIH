package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.AshaVisitRecord
import com.example.data.entity.MedicineReminder
import com.example.data.entity.MemoryGameScore
import com.example.data.entity.MoodRecord
import com.example.data.entity.OrientationQuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicine_reminders ORDER BY id ASC")
    fun getAllReminders(): Flow<List<MedicineReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: MedicineReminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<MedicineReminder>)

    @Update
    suspend fun updateReminder(reminder: MedicineReminder)

    @Query("UPDATE medicine_reminders SET isTaken = :isTaken, lastTakenTimestamp = :timestamp WHERE id = :id")
    suspend fun updateTakenStatus(id: Int, isTaken: Boolean, timestamp: Long?)

    @Delete
    suspend fun deleteReminder(reminder: MedicineReminder)

    @Query("DELETE FROM medicine_reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
}

@Dao
interface CognitiveDao {
    @Query("SELECT * FROM orientation_quiz_results ORDER BY timestamp DESC")
    fun getAllQuizResults(): Flow<List<OrientationQuizResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: OrientationQuizResult)

    @Query("SELECT * FROM memory_game_scores ORDER BY timestamp DESC")
    fun getAllGameScores(): Flow<List<MemoryGameScore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameScore(score: MemoryGameScore)
}

@Dao
interface AshaDao {
    @Query("SELECT * FROM asha_visit_records ORDER BY timestamp DESC")
    fun getAllVisits(): Flow<List<AshaVisitRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(record: AshaVisitRecord)

    @Update
    suspend fun updateVisit(record: AshaVisitRecord)

    @Query("UPDATE asha_visit_records SET isSyncedToNhm = 1 WHERE id = :id")
    suspend fun markSynced(id: Int)
}

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_records ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(record: MoodRecord)
}
