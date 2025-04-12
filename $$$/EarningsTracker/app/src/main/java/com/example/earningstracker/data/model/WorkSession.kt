package com.example.earningstracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "work_sessions")
data class WorkSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTime: Date,
    val endTime: Date,
    val hourlyWage: Double,
    val durationSeconds: Long,
    val earnings: Double,
    val date: Date = startTime
) 