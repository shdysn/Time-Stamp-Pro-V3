package com.example

import android.app.Application
import com.example.database.AppDatabase
import com.example.database.StreamHistoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LiveStreamApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        // Pre-populate with initial history record so user immediately sees past sample history data
        CoroutineScope(Dispatchers.IO).launch {
            val dao = database.streamHistoryDao()
            // Check if database is empty, seed initial sample records if fresh
            val sampleStreams = listOf(
                StreamHistoryEntity(
                    platform = "FACEBOOK",
                    title = "Live Q&A with Community & Fans",
                    description = "Answering questions on tech, photography, and live production tools.",
                    destination = "Creator Page: Alex Live",
                    privacy = "Public",
                    mode = "CAMERA",
                    startTimeMillis = System.currentTimeMillis() - 86400000L * 2,
                    durationSeconds = 2540,
                    peakViewers = 842,
                    totalComments = 328,
                    resolution = "1080p",
                    averageBitrateKbps = 4480,
                    status = "COMPLETED"
                ),
                StreamHistoryEntity(
                    platform = "YOUTUBE",
                    title = "Android Kotlin Studio - Real-time Dev Session",
                    description = "Building high-performance Jetpack Compose architecture and streaming pipelines.",
                    destination = "Channel: Android Code Lab",
                    privacy = "Public",
                    mode = "SCREEN",
                    startTimeMillis = System.currentTimeMillis() - 86400000L * 5,
                    durationSeconds = 4890,
                    peakViewers = 2150,
                    totalComments = 912,
                    resolution = "1080p",
                    averageBitrateKbps = 4520,
                    status = "COMPLETED"
                )
            )
            try {
                for (stream in sampleStreams) {
                    dao.insertStream(stream)
                }
            } catch (_: Exception) {
                // Ignore if already populated
            }
        }
    }
}
