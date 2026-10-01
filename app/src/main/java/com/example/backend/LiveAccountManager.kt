package com.example.backend

import com.example.data.model.ConnectedAccount
import com.example.data.model.StreamPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LiveAccountManager {

    private val _facebookAccount = MutableStateFlow(
        ConnectedAccount(
            platform = StreamPlatform.FACEBOOK,
            accountName = "Alex Rivera",
            accountHandle = "@alexlive_official",
            isConnected = true,
            tokenExpiry = "Expires in 58 days",
            availableDestinations = listOf(
                "Personal Profile (Alex Rivera)",
                "Creator Page: Alex Live Tech",
                "Gaming Page: Pixel Play Studio",
                "Community Group: Mobile Developers"
            ),
            selectedDestination = "Creator Page: Alex Live Tech"
        )
    )
    val facebookAccount: StateFlow<ConnectedAccount> = _facebookAccount.asStateFlow()

    private val _youtubeAccount = MutableStateFlow(
        ConnectedAccount(
            platform = StreamPlatform.YOUTUBE,
            accountName = "Alex Tech Studio",
            accountHandle = "@AlexTechLive",
            isConnected = true,
            tokenExpiry = "Active (Google OAuth 2.0)",
            availableDestinations = listOf(
                "Alex Tech Studio (48.2K Subscribers)",
                "Gaming Live Stream Lab (12.5K Subs)",
                "Vlog & Shorts Live (3.4K Subs)"
            ),
            selectedDestination = "Alex Tech Studio (48.2K Subscribers)"
        )
    )
    val youtubeAccount: StateFlow<ConnectedAccount> = _youtubeAccount.asStateFlow()

    fun updateFacebookDestination(destination: String) {
        _facebookAccount.update { it.copy(selectedDestination = destination) }
    }

    fun updateYouTubeDestination(destination: String) {
        _youtubeAccount.update { it.copy(selectedDestination = destination) }
    }

    fun toggleFacebookConnection() {
        _facebookAccount.update {
            val nextState = !it.isConnected
            it.copy(
                isConnected = nextState,
                accountName = if (nextState) "Alex Rivera" else "Not Connected",
                accountHandle = if (nextState) "@alexlive_official" else ""
            )
        }
    }

    fun toggleYouTubeConnection() {
        _youtubeAccount.update {
            val nextState = !it.isConnected
            it.copy(
                isConnected = nextState,
                accountName = if (nextState) "Alex Tech Studio" else "Not Connected",
                accountHandle = if (nextState) "@AlexTechLive" else ""
            )
        }
    }

    fun disconnectAll() {
        _facebookAccount.update { it.copy(isConnected = false) }
        _youtubeAccount.update { it.copy(isConnected = false) }
    }
}
