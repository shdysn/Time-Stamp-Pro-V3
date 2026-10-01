package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.LiveStreamApplication
import com.example.backend.LiveAccountManager
import com.example.backend.LiveStreamEngine
import com.example.data.model.AudioQuality
import com.example.data.model.StreamLatency
import com.example.data.model.StreamMode
import com.example.data.model.StreamPlatform
import com.example.data.model.StreamPrivacy
import com.example.data.model.UserSettings
import com.example.data.model.VideoQuality
import com.example.database.StreamHistoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FacebookLiveFormState(
    val selectedAccount: String = "My Facebook Account",
    val selectedDestination: String = "My Creator Page",
    val title: String = "Live Community Showcase & Chat",
    val description: String = "Join us live! Feel free to ask any questions in the chat.",
    val privacy: StreamPrivacy = StreamPrivacy.PUBLIC,
    val mode: StreamMode = StreamMode.CAMERA,
    val videoQuality: VideoQuality = VideoQuality.FHD_1080P
)

data class YouTubeLiveFormState(
    val selectedChannel: String = "Main Live Stream Channel",
    val title: String = "Live Mobile App Development & Tech Talk",
    val description: String = "Streaming live with full HD camera and screen casting.",
    val privacy: StreamPrivacy = StreamPrivacy.PUBLIC,
    val latency: StreamLatency = StreamLatency.LOW,
    val category: String = "Science & Technology",
    val mode: StreamMode = StreamMode.CAMERA,
    val videoQuality: VideoQuality = VideoQuality.FHD_1080P
)

class LiveViewModel(application: Application) : AndroidViewModel(application) {

    private val db = (application as LiveStreamApplication).database
    private val historyDao = db.streamHistoryDao()

    val accountManager = LiveAccountManager(application.applicationContext)
    val streamEngine = LiveStreamEngine(viewModelScope)

    // User settings
    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    // Stream history from Room
    val streamHistory: StateFlow<List<StreamHistoryEntity>> = historyDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Facebook live draft setup form
    private val _facebookForm = MutableStateFlow(
        FacebookLiveFormState(
            selectedAccount = accountManager.facebookAccount.value.accountName,
            selectedDestination = accountManager.facebookAccount.value.selectedDestination
        )
    )
    val facebookForm: StateFlow<FacebookLiveFormState> = _facebookForm.asStateFlow()

    // YouTube live draft setup form
    private val _youtubeForm = MutableStateFlow(
        YouTubeLiveFormState(
            selectedChannel = accountManager.youtubeAccount.value.selectedDestination
        )
    )
    val youtubeForm: StateFlow<YouTubeLiveFormState> = _youtubeForm.asStateFlow()

    // Screen sharing permission draft state (needed when user selects Screen Live)
    private val _pendingScreenLivePlatform = MutableStateFlow<StreamPlatform?>(null)
    val pendingScreenLivePlatform: StateFlow<StreamPlatform?> = _pendingScreenLivePlatform.asStateFlow()

    // Login State
    private val _isUserLoggedIn = MutableStateFlow(true)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    // Active Tab in History (All, Facebook, YouTube)
    private val _historyFilter = MutableStateFlow<String>("ALL")
    val historyFilter: StateFlow<String> = _historyFilter.asStateFlow()

    fun updateStreamerProfile(name: String, tagline: String) {
        _userSettings.update {
            it.copy(
                streamerDisplayName = name.ifBlank { "My Stream Studio" },
                streamerTagline = tagline.ifBlank { "Primary Live Broadcaster" }
            )
        }
    }

    fun updateFacebookForm(transform: (FacebookLiveFormState) -> FacebookLiveFormState) {
        _facebookForm.update(transform)
    }

    fun updateYouTubeForm(transform: (YouTubeLiveFormState) -> YouTubeLiveFormState) {
        _youtubeForm.update(transform)
    }

    fun updateUserSettings(transform: (UserSettings) -> UserSettings) {
        _userSettings.update(transform)
    }

    fun setHistoryFilter(filter: String) {
        _historyFilter.value = filter
    }

    fun prepareScreenLive(platform: StreamPlatform) {
        _pendingScreenLivePlatform.value = platform
    }

    fun startFacebookLive(): Boolean {
        val form = _facebookForm.value
        streamEngine.startLive(
            platform = StreamPlatform.FACEBOOK,
            mode = form.mode,
            title = form.title,
            description = form.description,
            destination = form.selectedDestination,
            privacy = form.privacy
        )
        return true
    }

    fun startYouTubeLive(): Boolean {
        val form = _youtubeForm.value
        streamEngine.startLive(
            platform = StreamPlatform.YOUTUBE,
            mode = form.mode,
            title = form.title,
            description = form.description,
            destination = form.selectedChannel,
            privacy = form.privacy
        )
        return true
    }

    fun stopCurrentLiveAndSave() {
        val platform = streamEngine.currentPlatform.value
        val mode = streamEngine.currentMode.value
        val title = streamEngine.streamTitle.value
        val desc = streamEngine.streamDescription.value
        val dest = streamEngine.streamDestination.value
        val privacy = streamEngine.streamPrivacy.value
        val finalStats = streamEngine.stopLive()

        viewModelScope.launch {
            val record = StreamHistoryEntity(
                platform = platform.name,
                title = title,
                description = desc,
                destination = dest,
                privacy = privacy.displayName,
                mode = mode.name,
                startTimeMillis = System.currentTimeMillis() - (finalStats.durationSeconds * 1000L),
                durationSeconds = finalStats.durationSeconds,
                peakViewers = finalStats.viewerCount,
                totalComments = streamEngine.comments.value.size,
                resolution = "1080p",
                averageBitrateKbps = finalStats.currentBitrateKbps,
                status = "COMPLETED"
            )
            historyDao.insertStream(record)
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            historyDao.deleteById(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyDao.clearAllHistory()
        }
    }

    fun loginWithFacebook() {
        accountManager.toggleFacebookConnection()
        _isUserLoggedIn.value = true
    }

    fun loginWithGoogle() {
        accountManager.toggleYouTubeConnection()
        _isUserLoggedIn.value = true
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }

    fun deleteAccount() {
        accountManager.disconnectAll()
        clearAllHistory()
        _isUserLoggedIn.value = false
    }
}
