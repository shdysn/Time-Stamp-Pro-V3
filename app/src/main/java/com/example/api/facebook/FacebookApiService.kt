package com.example.api.facebook

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FacebookApiService {

    @GET("v19.0/me")
    suspend fun getMe(
        @Query("access_token") accessToken: String,
        @Query("fields") fields: String = "id,name,email"
    ): Response<FacebookUserResponse>

    @GET("v19.0/me/accounts")
    suspend fun getMyPages(
        @Query("access_token") accessToken: String,
        @Query("fields") fields: String = "id,name,access_token,category"
    ): Response<FacebookPagesResponse>

    @POST("v19.0/{targetId}/live_videos")
    suspend fun createLiveVideo(
        @Path("targetId") targetId: String,
        @Query("access_token") accessToken: String,
        @Body body: CreateFacebookLiveRequest
    ): Response<FacebookLiveVideoResponse>

    @GET("v19.0/{liveVideoId}/comments")
    suspend fun getLiveComments(
        @Path("liveVideoId") liveVideoId: String,
        @Query("access_token") accessToken: String,
        @Query("fields") fields: String = "id,from,message"
    ): Response<FacebookCommentsResponse>

    @POST("v19.0/{liveVideoId}")
    suspend fun endLiveVideo(
        @Path("liveVideoId") liveVideoId: String,
        @Query("access_token") accessToken: String,
        @Query("end_live_video") endLiveVideo: Boolean = true
    ): Response<Map<String, Any>>
}
