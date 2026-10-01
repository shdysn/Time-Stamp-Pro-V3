package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.YouTubeRed

enum class StreamPlatform(val displayName: String, val brandColor: Color) {
    FACEBOOK("Facebook Live", FacebookBlue),
    YOUTUBE("YouTube Live", YouTubeRed)
}

enum class StreamMode(val displayName: String, val iconDesc: String) {
    CAMERA("Camera Live", "Stream using device cameras"),
    SCREEN("Screen Live", "Share your screen or gameplay")
}

enum class StreamPrivacy(val displayName: String, val description: String) {
    PUBLIC("Public", "Anyone can search and watch"),
    FRIENDS("Friends", "Visible only to your friends"),
    UNLISTED("Unlisted", "Anyone with the link can view"),
    ONLY_ME("Only Me", "Private test stream for host only"),
    PRIVATE("Private", "Only specified viewers can watch")
}

enum class VideoQuality(val label: String, val resolution: String, val targetBitrateKbps: Int) {
    SD_480P("480p SD", "854x480", 1500),
    HD_720P("720p HD", "1280x720", 3000),
    FHD_1080P("1080p FHD", "1920x1080", 4500)
}

enum class AudioQuality(val label: String, val bitrateKbps: Int) {
    STANDARD_128("128 kbps (Standard)", 128),
    HIGH_192("192 kbps (High Quality)", 192),
    STUDIO_320("320 kbps (Studio Audio)", 320)
}

enum class StreamOrientation(val label: String) {
    PORTRAIT("Portrait (9:16)"),
    LANDSCAPE("Landscape (16:9)"),
    AUTO("Auto-Detect Device")
}

enum class StreamLatency(val label: String, val description: String) {
    NORMAL("Normal Latency", "Best video quality & DVR buffer"),
    LOW("Low Latency", "Near real-time chat interactions"),
    ULTRA_LOW("Ultra-Low Latency", "Immediate real-time reactions (~1s)")
}

data class StreamComment(
    val id: String,
    val authorName: String,
    val authorBadge: String? = null,
    val message: String,
    val timestampSecondsAgo: Int = 0,
    val isPinned: Boolean = false,
    val isHost: Boolean = false
)

data class StreamStats(
    val durationSeconds: Long = 0,
    val currentFps: Int = 60,
    val currentBitrateKbps: Int = 4500,
    val viewerCount: Int = 0,
    val likesCount: Int = 0,
    val connectionQuality: String = "EXCELLENT",
    val isMicMuted: Boolean = false,
    val isCameraOff: Boolean = false,
    val isPrivacyShieldActive: Boolean = false,
    val isUsingFrontCamera: Boolean = true
)

data class UserSettings(
    val streamerDisplayName: String = "My Stream Studio",
    val streamerTagline: String = "Primary Live Broadcaster",
    val videoQuality: VideoQuality = VideoQuality.FHD_1080P,
    val frameRate: Int = 60,
    val audioQuality: AudioQuality = AudioQuality.HIGH_192,
    val audioSource: String = "Internal Audio + Mic",
    val noiseCancellation: Boolean = true,
    val streamOrientation: StreamOrientation = StreamOrientation.PORTRAIT,
    val floatingBubble: Boolean = true,
    val streamNotifications: Boolean = true,
    val milestoneAlerts: Boolean = true,
    val chatTts: Boolean = false
)

data class ConnectedAccount(
    val platform: StreamPlatform,
    val accountName: String,
    val accountHandle: String,
    val isConnected: Boolean,
    val streamKey: String = "",
    val rtmpServerUrl: String = "",
    val apiAccessToken: String = "",
    val isApiVerified: Boolean = false,
    val tokenExpiry: String = "Active",
    val availableDestinations: List<String> = emptyList(),
    val selectedDestination: String = ""
)
