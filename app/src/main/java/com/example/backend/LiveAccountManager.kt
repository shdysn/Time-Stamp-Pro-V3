package com.example.backend

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ConnectedAccount
import com.example.data.model.StreamPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LiveAccountManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("livecast_account_prefs", Context.MODE_PRIVATE)

    private val _facebookAccount = MutableStateFlow(loadFacebookAccount())
    val facebookAccount: StateFlow<ConnectedAccount> = _facebookAccount.asStateFlow()

    private val _youtubeAccount = MutableStateFlow(loadYouTubeAccount())
    val youtubeAccount: StateFlow<ConnectedAccount> = _youtubeAccount.asStateFlow()

    private fun loadFacebookAccount(): ConnectedAccount {
        val isConnected = prefs.getBoolean("fb_connected", true)
        val name = prefs.getString("fb_name", "My Facebook Account") ?: "My Facebook Account"
        val handle = prefs.getString("fb_handle", "@my_facebook_stream") ?: "@my_facebook_stream"
        val destination = prefs.getString("fb_destination", "My Creator Page") ?: "My Creator Page"
        val streamKey = prefs.getString("fb_key", "FB-live-9382104-sec") ?: "FB-live-9382104-sec"
        val rtmpUrl = prefs.getString("fb_rtmp", "rtmps://live-api-s.facebook.com:443/rtmp/") ?: "rtmps://live-api-s.facebook.com:443/rtmp/"

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
            tokenExpiry = "Active Live Token",
            availableDestinations = savedDestinations,
            selectedDestination = destination
        )
    }

    private fun loadYouTubeAccount(): ConnectedAccount {
        val isConnected = prefs.getBoolean("yt_connected", true)
        val name = prefs.getString("yt_name", "My YouTube Studio") ?: "My YouTube Studio"
        val handle = prefs.getString("yt_handle", "@MyYouTubeChannel") ?: "@MyYouTubeChannel"
        val destination = prefs.getString("yt_destination", "Main Live Stream Channel") ?: "Main Live Stream Channel"
        val streamKey = prefs.getString("yt_key", "yt-live-prod-8a21-99c") ?: "yt-live-prod-8a21-99c"
        val rtmpUrl = prefs.getString("yt_rtmp", "rtmp://a.rtmp.youtube.com/live2") ?: "rtmp://a.rtmp.youtube.com/live2"

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
            tokenExpiry = "Active (OAuth 2.0)",
            availableDestinations = savedDestinations,
            selectedDestination = destination
        )
    }

    fun saveCustomFacebookAccount(
        name: String,
        handle: String,
        destination: String,
        streamKey: String,
        rtmpUrl: String
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
            .putStringSet("fb_destinations", currentDests.toSet())
            .apply()

        _facebookAccount.update {
            it.copy(
                accountName = name.ifBlank { "My Facebook Account" },
                accountHandle = handle.ifBlank { "@facebook_user" },
                isConnected = true,
                streamKey = streamKey,
                rtmpServerUrl = rtmpUrl.ifBlank { "rtmps://live-api-s.facebook.com:443/rtmp/" },
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
        rtmpUrl: String
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
            .putStringSet("yt_destinations", currentDests.toSet())
            .apply()

        _youtubeAccount.update {
            it.copy(
                accountName = name.ifBlank { "My YouTube Studio" },
                accountHandle = handle.ifBlank { "@MyYouTubeChannel" },
                isConnected = true,
                streamKey = streamKey,
                rtmpServerUrl = rtmpUrl.ifBlank { "rtmp://a.rtmp.youtube.com/live2" },
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
        _facebookAccount.update { it.copy(isConnected = false) }
        _youtubeAccount.update { it.copy(isConnected = false) }
    }
}
