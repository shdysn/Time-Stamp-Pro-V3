package com.example.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StreamHistoryDao {
    @Query("SELECT * FROM stream_history ORDER BY startTimeMillis DESC")
    fun getAllHistory(): Flow<List<StreamHistoryEntity>>

    @Query("SELECT * FROM stream_history WHERE platform = :platform ORDER BY startTimeMillis DESC")
    fun getHistoryByPlatform(platform: String): Flow<List<StreamHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStream(history: StreamHistoryEntity): Long

    @Delete
    suspend fun deleteStream(history: StreamHistoryEntity)

    @Query("DELETE FROM stream_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM stream_history")
    suspend fun clearAllHistory()
}
