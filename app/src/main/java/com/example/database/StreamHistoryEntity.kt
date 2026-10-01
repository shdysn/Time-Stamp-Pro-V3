package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream_history")
data class StreamHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val platform: String, // "FACEBOOK", "YOUTUBE"
    val title: String,
    val description: String,
    val destination: String, // e.g. "Gaming Studio Page" or "Main Channel"
    val privacy: String,
    val mode: String, // "CAMERA", "SCREEN"
    val startTimeMillis: Long,
    val durationSeconds: Long,
    val peakViewers: Int,
    val totalComments: Int,
    val resolution: String,
    val averageBitrateKbps: Int,
    val status: String // "COMPLETED", "INTERRUPTED"
)
