package com.example.earningstracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.earningstracker.data.model.WorkSession
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkSessionDao {
    @Insert
    suspend fun insertWorkSession(workSession: WorkSession): Long

    @Delete
    suspend fun deleteWorkSession(workSession: WorkSession)

    @Query("SELECT * FROM work_sessions ORDER BY date DESC")
    fun getAllWorkSessions(): Flow<List<WorkSession>>

    @Query("SELECT * FROM work_sessions WHERE id = :sessionId")
    suspend fun getWorkSessionById(sessionId: Int): WorkSession?

    @Query("DELETE FROM work_sessions")
    suspend fun deleteAllSessions()
} 