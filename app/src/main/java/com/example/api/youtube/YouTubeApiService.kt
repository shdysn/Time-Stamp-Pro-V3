package com.example.api.youtube

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("youtube/v3/channels")
    suspend fun getMyChannels(
        @Header("Authorization") authHeader: String,
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("mine") mine: Boolean = true
    ): Response<YouTubeChannelResponse>

    @POST("youtube/v3/liveBroadcasts")
    suspend fun createLiveBroadcast(
        @Header("Authorization") authHeader: String,
        @Query("part") part: String = "snippet,status,contentDetails",
        @Body body: CreateBroadcastRequest
    ): Response<LiveBroadcastResource>

    @POST("youtube/v3/liveStreams")
    suspend fun createLiveStream(
        @Header("Authorization") authHeader: String,
        @Query("part") part: String = "snippet,cdn",
        @Body body: CreateStreamRequest
    ): Response<LiveStreamResource>

    @POST("youtube/v3/liveBroadcasts/bind")
    suspend fun bindBroadcastToStream(
        @Header("Authorization") authHeader: String,
        @Query("id") broadcastId: String,
        @Query("part") part: String = "id,contentDetails",
        @Query("streamId") streamId: String
    ): Response<LiveBroadcastResource>

    @POST("youtube/v3/liveBroadcasts/transition")
    suspend fun transitionBroadcast(
        @Header("Authorization") authHeader: String,
        @Query("broadcastStatus") broadcastStatus: String, // "testing", "live", "complete"
        @Query("id") broadcastId: String,
        @Query("part") part: String = "status"
    ): Response<LiveBroadcastResource>
}
