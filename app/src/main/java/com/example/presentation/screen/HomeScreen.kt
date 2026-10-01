package com.example.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.StreamPlatform
import com.example.presentation.components.CustomAccountDialog
import com.example.presentation.components.EditProfileDialog
import com.example.presentation.viewmodel.LiveViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.LiveCyan
import com.example.ui.theme.LiveRed
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.YouTubeRed

@Composable
fun HomeScreen(
    viewModel: LiveViewModel,
    onNavigateFacebookLive: () -> Unit,
    onNavigateYouTubeLive: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val fbAccount by viewModel.accountManager.facebookAccount.collectAsState()
    val ytAccount by viewModel.accountManager.youtubeAccount.collectAsState()
    val historyList by viewModel.streamHistory.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editingPlatform by remember { mutableStateOf<StreamPlatform?>(null) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .border(1.5.dp, LiveRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.live_stream_studio_icon_1790841229670),
                            contentDescription = "Studio Icon",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "LiveCast Studio",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(StatusGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RTMP Server Online",
                                fontSize = 11.sp,
                                color = StatusGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row {
                    IconButton(
                        onClick = onNavigateSettings,
                        modifier = Modifier.testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("home_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color(0xFFEF4444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. User Profile Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(LiveCyan, LiveRed))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = remember(userSettings.streamerDisplayName) {
                                userSettings.streamerDisplayName.take(2).uppercase()
                            }
                            Text(
                                text = initials.ifBlank { "LS" },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userSettings.streamerDisplayName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = userSettings.streamerTagline,
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier.testTag("edit_profile_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = LiveCyan
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Connected Accounts Status Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = if (fbAccount.isConnected) Color(0xFF1E3A8A) else Color(0xFF334155),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { editingPlatform = StreamPlatform.FACEBOOK }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(FacebookBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("f", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = fbAccount.accountName.take(12),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }

                        Surface(
                            color = if (ytAccount.isConnected) Color(0xFF7F1D1D) else Color(0xFF334155),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { editingPlatform = StreamPlatform.YOUTUBE }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(YouTubeRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("▶", color = Color.White, fontSize = 9.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = ytAccount.accountName.take(12),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Choose Live Destination",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Facebook Live Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FacebookBlue.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateFacebookLive() }
                    .testTag("facebook_live_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(FacebookBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "f",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Facebook Live",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${fbAccount.accountName} • ${fbAccount.selectedDestination}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        IconButton(onClick = { editingPlatform = StreamPlatform.FACEBOOK }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Account", tint = LiveCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Broadcast to your profile, creator pages, or groups with camera or screen capture.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onNavigateFacebookLive,
                        colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("setup_facebook_live_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Configure Facebook Live", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. YouTube Live Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, YouTubeRed.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateYouTubeLive() }
                    .testTag("youtube_live_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(YouTubeRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "▶",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "YouTube Live",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${ytAccount.accountName} • ${ytAccount.selectedDestination}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        IconButton(onClick = { editingPlatform = StreamPlatform.YOUTUBE }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Account", tint = LiveCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Stream to your YouTube channel with low-latency chat, high-bitrate encoding, and custom stream key.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onNavigateYouTubeLive,
                        colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("setup_youtube_live_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Configure YouTube Live", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Studio Hub Shortcuts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live History Tile
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateHistory() }
                        .testTag("history_tile")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = LiveCyan,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Live History",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${historyList.size} Past Streams",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                // Settings Tile
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateSettings() }
                        .testTag("settings_tile")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = LiveRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Studio Settings",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "1080p • 60 FPS",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Edit Streamer Profile Dialog
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userSettings.streamerDisplayName,
            currentTagline = userSettings.streamerTagline,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, tagline ->
                viewModel.updateStreamerProfile(name, tagline)
                showEditProfileDialog = false
            }
        )
    }

    // Edit Custom Facebook / YouTube Account Dialog
    editingPlatform?.let { platform ->
        val acc = if (platform == StreamPlatform.FACEBOOK) fbAccount else ytAccount
        CustomAccountDialog(
            platform = platform,
            initialName = acc.accountName,
            initialHandle = acc.accountHandle,
            initialDestination = acc.selectedDestination,
            initialStreamKey = acc.streamKey,
            initialRtmpUrl = acc.rtmpServerUrl,
            onDismiss = { editingPlatform = null },
            onSave = { name, handle, dest, key, rtmp ->
                if (platform == StreamPlatform.FACEBOOK) {
                    viewModel.accountManager.saveCustomFacebookAccount(name, handle, dest, key, rtmp)
                    viewModel.updateFacebookForm { it.copy(selectedAccount = "$name ($handle)", selectedDestination = dest) }
                } else {
                    viewModel.accountManager.saveCustomYouTubeAccount(name, handle, dest, key, rtmp)
                    viewModel.updateYouTubeForm { it.copy(selectedChannel = dest) }
                }
                editingPlatform = null
            }
        )
    }
}
