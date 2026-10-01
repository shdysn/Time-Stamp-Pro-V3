package com.example.backend

import android.content.Context
import android.content.SharedPreferences
import com.example.api.facebook.CreateFacebookLiveRequest
import com.example.api.facebook.FacebookApiClient
import com.example.api.youtube.BroadcastContentDetails
import com.example.api.youtube.BroadcastSnippet
import com.example.api.youtube.BroadcastStatus
import com.example.api.youtube.CreateBroadcastRequest
import com.example.api.youtube.CreateStreamRequest
import com.example.api.youtube.StreamCdn
import com.example.api.youtube.StreamSnippet
import com.example.api.youtube.YouTubeApiClient
import com.example.data.model.ConnectedAccount
import com.example.data.model.StreamPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LiveAccountManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("livecast_account_prefs", Context.MODE_PRIVATE)

    private val _facebookAccount = MutableStateFlow(loadFacebookAccount())
    val facebookAccount: StateFlow<ConnectedAccount> = _facebookAccount.asStateFlow()

    private val _youtubeAccount = MutableStateFlow(loadYouTubeAccount())
    val youtubeAccount: StateFlow<ConnectedAccount> = _youtubeAccount.asStateFlow()

    private fun loadFacebookAccount(): ConnectedAccount {
        val isConnected = prefs.getBoolean("fb_connected", true)
        val name = prefs.getString("fb_name", "Alex Facebook Live") ?: "Alex Facebook Live"
        val handle = prefs.getString("fb_handle", "@facebook_creator") ?: "@facebook_creator"
        val destination = prefs.getString("fb_destination", "Creator Page: Live Pro") ?: "Creator Page: Live Pro"
        val streamKey = prefs.getString("fb_key", "FB-live-9382104-sec") ?: "FB-live-9382104-sec"
        val rtmpUrl = prefs.getString("fb_rtmp", "rtmps://live-api-s.facebook.com:443/rtmp/") ?: "rtmps://live-api-s.facebook.com:443/rtmp/"
        val apiToken = prefs.getString("fb_token", "") ?: ""
        val isVerified = prefs.getBoolean("fb_verified", false)

        val savedDestinations = prefs.getStringSet("fb_destinations", null)?.toList() ?: listOf(
            destination,
            "Personal Profile Timeline",
            "Gaming Creator Page",
            "Dev Community Group"
        )

        return ConnectedAccount(
            platform = StreamPlatform.FACEBOOK,
            accountName = name,
            accountHandle = handle,
            isConnected = isConnected,
            streamKey = streamKey,
            rtmpServerUrl = rtmpUrl,
            apiAccessToken = apiToken,
            isApiVerified = isVerified,
            tokenExpiry = if (isVerified) "API Verified & Active" else "Standard Stream RTMP",
            availableDestinations = savedDestinations,
            selectedDestination = destination
        )
    }

    private fun loadYouTubeAccount(): ConnectedAccount {
        val isConnected = prefs.getBoolean("yt_connected", true)
        val name = prefs.getString("yt_name", "YouTube Creator Studio") ?: "YouTube Creator Studio"
        val handle = prefs.getString("yt_handle", "@YouTubeLiveStudio") ?: "@YouTubeLiveStudio"
        val destination = prefs.getString("yt_destination", "Main Live Stream Channel") ?: "Main Live Stream Channel"
        val streamKey = prefs.getString("yt_key", "yt-live-prod-8a21-99c") ?: "yt-live-prod-8a21-99c"
        val rtmpUrl = prefs.getString("yt_rtmp", "rtmp://a.rtmp.youtube.com/live2") ?: "rtmp://a.rtmp.youtube.com/live2"
        val apiToken = prefs.getString("yt_token", "") ?: ""
        val isVerified = prefs.getBoolean("yt_verified", false)

        val savedDestinations = prefs.getStringSet("yt_destinations", null)?.toList() ?: listOf(
            destination,
            "Secondary Gaming Channel",
            "Live Podcasts & Shorts"
        )

        return ConnectedAccount(
            platform = StreamPlatform.YOUTUBE,
            accountName = name,
            accountHandle = handle,
            isConnected = isConnected,
            streamKey = streamKey,
            rtmpServerUrl = rtmpUrl,
            apiAccessToken = apiToken,
            isApiVerified = isVerified,
            tokenExpiry = if (isVerified) "OAuth 2.0 API Verified" else "Standard Stream RTMP",
            availableDestinations = savedDestinations,
            selectedDestination = destination
        )
    }

    suspend fun verifyFacebookApiToken(token: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = FacebookApiClient.apiService.getMe(accessToken = token)
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                val newName = user.name

                // Also try fetching pages
                val pagesRes = try {
                    FacebookApiClient.apiService.getMyPages(accessToken = token)
                } catch (e: Exception) {
                    null
                }

                val pages = pagesRes?.body()?.data?.map { it.name } ?: emptyList()
                val updatedDestinations = (_facebookAccount.value.availableDestinations + pages).distinct()
                val selected = pages.firstOrNull() ?: _facebookAccount.value.selectedDestination

                prefs.edit()
                    .putBoolean("fb_connected", true)
                    .putBoolean("fb_verified", true)
                    .putString("fb_token", token)
                    .putString("fb_name", newName)
                    .putStringSet("fb_destinations", updatedDestinations.toSet())
                    .putString("fb_destination", selected)
                    .apply()

                _facebookAccount.update {
                    it.copy(
                        accountName = newName,
                        isConnected = true,
                        isApiVerified = true,
                        apiAccessToken = token,
                        tokenExpiry = "API Verified & Active",
                        availableDestinations = updatedDestinations,
                        selectedDestination = selected
                    )
                }
                Result.success("Connected to Facebook API as $newName with ${pages.size} pages")
            } else {
                Result.failure(Exception("Facebook API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyYouTubeApiToken(tokenOrKey: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val authHeader = if (tokenOrKey.startsWith("Bearer ")) tokenOrKey else "Bearer $tokenOrKey"
            val response = YouTubeApiClient.apiService.getMyChannels(authHeader = authHeader)
            if (response.isSuccessful && response.body() != null) {
                val channelItem = response.body()?.items?.firstOrNull()
                val channelTitle = channelItem?.snippet?.title ?: "My YouTube Channel"
                val subs = channelItem?.statistics?.subscriberCount ?: "N/A"
                val handle = channelItem?.snippet?.customUrl ?: "@$channelTitle"

                val updatedDestinations = (_youtubeAccount.value.availableDestinations + "$channelTitle ($subs subs)").distinct()

                prefs.edit()
                    .putBoolean("yt_connected", true)
                    .putBoolean("yt_verified", true)
                    .putString("yt_token", tokenOrKey)
                    .putString("yt_name", channelTitle)
                    .putString("yt_handle", handle)
                    .putStringSet("yt_destinations", updatedDestinations.toSet())
                    .putString("yt_destination", "$channelTitle ($subs subs)")
                    .apply()

                _youtubeAccount.update {
                    it.copy(
                        accountName = channelTitle,
                        accountHandle = handle,
                        isConnected = true,
                        isApiVerified = true,
                        apiAccessToken = tokenOrKey,
                        tokenExpiry = "Google OAuth 2.0 API Verified",
                        availableDestinations = updatedDestinations,
                        selectedDestination = "$channelTitle ($subs subs)"
                    )
                }
                Result.success("Connected to YouTube API: $channelTitle ($subs subscribers)")
            } else {
                Result.failure(Exception("YouTube API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRealFacebookLiveSession(
        title: String,
        description: String
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        val token = _facebookAccount.value.apiAccessToken
        if (token.isBlank()) {
            return@withContext Result.failure(Exception("Facebook API Token not configured"))
        }

        try {
            val response = FacebookApiClient.apiService.createLiveVideo(
                targetId = "me",
                accessToken = token,
                body = CreateFacebookLiveRequest(title = title, description = description)
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val secureUrl = body.secureStreamUrl ?: body.streamUrl ?: ""
                // Stream URL usually looks like: rtmps://live-api-s.facebook.com:443/rtmp/{STREAM_KEY}
                val parts = secureUrl.split("/")
                val streamKey = parts.lastOrNull() ?: ""
                val rtmpUrl = secureUrl.substringBeforeLast("/") + "/"

                Result.success(Pair(rtmpUrl, streamKey))
            } else {
                Result.failure(Exception("Facebook Live creation failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRealYouTubeLiveSession(
        title: String,
        description: String,
        privacy: String
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        val token = _youtubeAccount.value.apiAccessToken
        if (token.isBlank()) {
            return@withContext Result.failure(Exception("YouTube OAuth Token not configured"))
        }

        val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"

        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val scheduledTime = sdf.format(Date(System.currentTimeMillis() + 10_000L))

            // 1. Create Broadcast
            val broadcastRes = YouTubeApiClient.apiService.createLiveBroadcast(
                authHeader = authHeader,
                body = CreateBroadcastRequest(
                    snippet = BroadcastSnippet(title = title, description = description, scheduledStartTime = scheduledTime),
                    status = BroadcastStatus(privacyStatus = privacy.lowercase()),
                    contentDetails = BroadcastContentDetails(enableAutoStart = true, latencyPreference = "low")
                )
            )

            if (!broadcastRes.isSuccessful || broadcastRes.body() == null) {
                return@withContext Result.failure(Exception("Create Broadcast failed: ${broadcastRes.code()} ${broadcastRes.message()}"))
            }
            val broadcastId = broadcastRes.body()!!.id

            // 2. Create Live Stream (RTMP Ingestion)
            val streamRes = YouTubeApiClient.apiService.createLiveStream(
                authHeader = authHeader,
                body = CreateStreamRequest(
                    snippet = StreamSnippet(title = "LiveCast Stream for $title"),
                    cdn = StreamCdn(frameRate = "60fps", ingestionType = "rtmp", resolution = "1080p")
                )
            )

            if (!streamRes.isSuccessful || streamRes.body() == null) {
                return@withContext Result.failure(Exception("Create Stream failed: ${streamRes.code()} ${streamRes.message()}"))
            }

            val streamId = streamRes.body()!!.id
            val ingestionInfo = streamRes.body()!!.cdn?.ingestionInfo
            val rtmpAddress = ingestionInfo?.ingestionAddress ?: "rtmp://a.rtmp.youtube.com/live2"
            val streamKey = ingestionInfo?.streamName ?: ""

            // 3. Bind Broadcast to Stream
            YouTubeApiClient.apiService.bindBroadcastToStream(
                authHeader = authHeader,
                broadcastId = broadcastId,
                streamId = streamId
            )

            Result.success(Pair(rtmpAddress, streamKey))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun saveCustomFacebookAccount(
        name: String,
        handle: String,
        destination: String,
        streamKey: String,
        rtmpUrl: String,
        apiToken: String = ""
    ) {
        val currentDests = _facebookAccount.value.availableDestinations.toMutableList()
        if (destination.isNotBlank() && !currentDests.contains(destination)) {
            currentDests.add(0, destination)
        }

        prefs.edit()
            .putBoolean("fb_connected", true)
            .putString("fb_name", name.ifBlank { "My Facebook Account" })
            .putString("fb_handle", handle.ifBlank { "@facebook_user" })
            .putString("fb_destination", destination.ifBlank { "Timeline Profile" })
            .putString("fb_key", streamKey)
            .putString("fb_rtmp", rtmpUrl.ifBlank { "rtmps://live-api-s.facebook.com:443/rtmp/" })
            .putString("fb_token", apiToken)
            .putStringSet("fb_destinations", currentDests.toSet())
            .apply()

        _facebookAccount.update {
            it.copy(
                accountName = name.ifBlank { "My Facebook Account" },
                accountHandle = handle.ifBlank { "@facebook_user" },
                isConnected = true,
                streamKey = streamKey,
                rtmpServerUrl = rtmpUrl.ifBlank { "rtmps://live-api-s.facebook.com:443/rtmp/" },
                apiAccessToken = apiToken,
                availableDestinations = currentDests,
                selectedDestination = destination.ifBlank { "Timeline Profile" }
            )
        }
    }

    fun saveCustomYouTubeAccount(
        name: String,
        handle: String,
        destination: String,
        streamKey: String,
        rtmpUrl: String,
        apiToken: String = ""
    ) {
        val currentDests = _youtubeAccount.value.availableDestinations.toMutableList()
        if (destination.isNotBlank() && !currentDests.contains(destination)) {
            currentDests.add(0, destination)
        }

        prefs.edit()
            .putBoolean("yt_connected", true)
            .putString("yt_name", name.ifBlank { "My YouTube Studio" })
            .putString("yt_handle", handle.ifBlank { "@MyYouTubeChannel" })
            .putString("yt_destination", destination.ifBlank { "Main Live Stream Channel" })
            .putString("yt_key", streamKey)
            .putString("yt_rtmp", rtmpUrl.ifBlank { "rtmp://a.rtmp.youtube.com/live2" })
            .putString("yt_token", apiToken)
            .putStringSet("yt_destinations", currentDests.toSet())
            .apply()

        _youtubeAccount.update {
            it.copy(
                accountName = name.ifBlank { "My YouTube Studio" },
                accountHandle = handle.ifBlank { "@MyYouTubeChannel" },
                isConnected = true,
                streamKey = streamKey,
                rtmpServerUrl = rtmpUrl.ifBlank { "rtmp://a.rtmp.youtube.com/live2" },
                apiAccessToken = apiToken,
                availableDestinations = currentDests,
                selectedDestination = destination.ifBlank { "Main Live Stream Channel" }
            )
        }
    }

    fun updateFacebookDestination(destination: String) {
        prefs.edit().putString("fb_destination", destination).apply()
        _facebookAccount.update { it.copy(selectedDestination = destination) }
    }

    fun updateYouTubeDestination(destination: String) {
        prefs.edit().putString("yt_destination", destination).apply()
        _youtubeAccount.update { it.copy(selectedDestination = destination) }
    }

    fun toggleFacebookConnection() {
        val next = !_facebookAccount.value.isConnected
        prefs.edit().putBoolean("fb_connected", next).apply()
        _facebookAccount.update { it.copy(isConnected = next) }
    }

    fun toggleYouTubeConnection() {
        val next = !_youtubeAccount.value.isConnected
        prefs.edit().putBoolean("yt_connected", next).apply()
        _youtubeAccount.update { it.copy(isConnected = next) }
    }

    fun disconnectAll() {
        prefs.edit().clear().apply()
        _facebookAccount.update { it.copy(isConnected = false, isApiVerified = false, apiAccessToken = "") }
        _youtubeAccount.update { it.copy(isConnected = false, isApiVerified = false, apiAccessToken = "") }
    }
}
