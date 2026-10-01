package com.example.backend

import com.example.data.model.StreamComment
import com.example.data.model.StreamMode
import com.example.data.model.StreamPlatform
import com.example.data.model.StreamPrivacy
import com.example.data.model.StreamStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class LiveStreamEngine(private val scope: CoroutineScope) {

    private val _isLive = MutableStateFlow(false)
    val isLive: StateFlow<Boolean> = _isLive.asStateFlow()

    private val _currentPlatform = MutableStateFlow(StreamPlatform.FACEBOOK)
    val currentPlatform: StateFlow<StreamPlatform> = _currentPlatform.asStateFlow()

    private val _currentMode = MutableStateFlow(StreamMode.CAMERA)
    val currentMode: StateFlow<StreamMode> = _currentMode.asStateFlow()

    private val _streamTitle = MutableStateFlow("Live Stream Broadcast")
    val streamTitle: StateFlow<String> = _streamTitle.asStateFlow()

    private val _streamDescription = MutableStateFlow("")
    val streamDescription: StateFlow<String> = _streamDescription.asStateFlow()

    private val _streamDestination = MutableStateFlow("Personal Profile")
    val streamDestination: StateFlow<String> = _streamDestination.asStateFlow()

    private val _streamPrivacy = MutableStateFlow(StreamPrivacy.PUBLIC)
    val streamPrivacy: StateFlow<StreamPrivacy> = _streamPrivacy.asStateFlow()

    private val _stats = MutableStateFlow(StreamStats())
    val stats: StateFlow<StreamStats> = _stats.asStateFlow()

    private val _comments = MutableStateFlow<List<StreamComment>>(emptyList())
    val comments: StateFlow<List<StreamComment>> = _comments.asStateFlow()

    private var streamTickerJob: Job? = null
    private var streamSessionStartTime = 0L

    private val sampleViewerNames = listOf(
        "Sarah Chen", "David Miller", "Elena Rostova", "Marcus Brody",
        "Amira Khan", "Kenji Sato", "Carlos Mendez", "Fatima Al-Sayed",
        "Lucas Silva", "Chloe Bennett", "Zubair Ahmad", "Priya Sharma"
    )

    private val sampleViewerComments = listOf(
        "Audio is crystal clear today! 🔥",
        "Great video quality! What bitrate are you running?",
        "Watching from Toronto! 👋",
        "Can you show the camera angle setup?",
        "Subscribed and shared the stream! Let's go!",
        "Greetings from Berlin! Amazing stream!",
        "Is this 60fps? The motion is super smooth!",
        "Love the commentary, keep it up!",
        "Can you test the screen share next?",
        "Awesome stream setup bro!"
    )

    fun startLive(
        platform: StreamPlatform,
        mode: StreamMode,
        title: String,
        description: String,
        destination: String,
        privacy: StreamPrivacy
    ) {
        _currentPlatform.value = platform
        _currentMode.value = mode
        _streamTitle.value = title.ifBlank { "Live Broadcast" }
        _streamDescription.value = description
        _streamDestination.value = destination
        _streamPrivacy.value = privacy

        _isLive.value = true
        streamSessionStartTime = System.currentTimeMillis()

        _stats.value = StreamStats(
            durationSeconds = 0,
            currentFps = 60,
            currentBitrateKbps = 4520,
            viewerCount = Random.nextInt(120, 240),
            likesCount = Random.nextInt(25, 60),
            connectionQuality = "EXCELLENT"
        )

        // Seed initial welcoming comment
        _comments.value = listOf(
            StreamComment(
                id = UUID.randomUUID().toString(),
                authorName = "Stream Bot",
                authorBadge = "MOD",
                message = "Stream started on ${platform.displayName}! Welcome everyone 👋",
                timestampSecondsAgo = 0,
                isPinned = true
            )
        )

        startStreamingLoop()
    }

    private fun startStreamingLoop() {
        streamTickerJob?.cancel()
        streamTickerJob = scope.launch(Dispatchers.Default) {
            var tick = 0
            while (isActive && _isLive.value) {
                delay(1000)
                tick++

                // Update Duration, FPS, Bitrate jitter, viewer counts
                _stats.update { prev ->
                    val jitterBitrate = (4500 + Random.nextInt(-180, 220)).coerceIn(3800, 4800)
                    val jitterFps = if (Random.nextFloat() > 0.85f) 59 else 60
                    val viewerGrowth = if (tick % 3 == 0) Random.nextInt(2, 14) else 0
                    val likesGrowth = if (tick % 4 == 0) Random.nextInt(1, 5) else 0

                    prev.copy(
                        durationSeconds = prev.durationSeconds + 1,
                        currentBitrateKbps = jitterBitrate,
                        currentFps = jitterFps,
                        viewerCount = prev.viewerCount + viewerGrowth,
                        likesCount = prev.likesCount + likesGrowth
                    )
                }

                // Add random realistic viewer comment every 4-7 seconds
                if (tick % 5 == 0 && _comments.value.size < 50) {
                    val randomUser = sampleViewerNames.random()
                    val randomText = sampleViewerComments.random()
                    val newComment = StreamComment(
                        id = UUID.randomUUID().toString(),
                        authorName = randomUser,
                        message = randomText,
                        timestampSecondsAgo = 0
                    )
                    _comments.update { current ->
                        (current + newComment).takeLast(30)
                    }
                }
            }
        }
    }

    fun stopLive(): StreamStats {
        val finalStats = _stats.value
        _isLive.value = false
        streamTickerJob?.cancel()
        streamTickerJob = null
        return finalStats
    }

    fun toggleMic() {
        _stats.update { it.copy(isMicMuted = !it.isMicMuted) }
    }

    fun toggleCamera() {
        _stats.update { it.copy(isCameraOff = !it.isCameraOff) }
    }

    fun switchCameraLens() {
        _stats.update { it.copy(isUsingFrontCamera = !it.isUsingFrontCamera) }
    }

    fun togglePrivacyShield() {
        _stats.update { it.copy(isPrivacyShieldActive = !it.isPrivacyShieldActive) }
    }

    fun sendHostComment(message: String) {
        if (message.isBlank()) return
        val hostComment = StreamComment(
            id = UUID.randomUUID().toString(),
            authorName = "Host (You)",
            authorBadge = "HOST",
            message = message,
            timestampSecondsAgo = 0,
            isHost = true
        )
        _comments.update { it + hostComment }
    }
}
