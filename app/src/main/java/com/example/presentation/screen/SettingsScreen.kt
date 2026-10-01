package com.example.presentation.screen

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioQuality
import com.example.data.model.StreamOrientation
import com.example.data.model.StreamPlatform
import com.example.data.model.VideoQuality
import com.example.presentation.components.CustomAccountDialog
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
fun SettingsScreen(
    viewModel: LiveViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    BackHandler { onBack() }

    val userSettings by viewModel.userSettings.collectAsState()
    val fbAccount by viewModel.accountManager.facebookAccount.collectAsState()
    val ytAccount by viewModel.accountManager.youtubeAccount.collectAsState()

    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
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
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("settings_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Broadcast & Account Preferences",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Video Quality Section
            Text(
                text = "1. Video Quality",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VideoQuality.entries.forEach { quality ->
                            val isSelected = userSettings.videoQuality == quality
                            Surface(
                                color = if (isSelected) LiveCyan else DarkSurfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateUserSettings { it.copy(videoQuality = quality) } }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = quality.label,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else Color.White
                                    )
                                    Text(
                                        text = "${quality.targetBitrateKbps}k",
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Broadcast Frame Rate", color = Color.White, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(30, 60).forEach { fps ->
                                val isSelected = userSettings.frameRate == fps
                                Surface(
                                    color = if (isSelected) LiveRed else DarkSurfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable {
                                        viewModel.updateUserSettings { it.copy(frameRate = fps) }
                                    }
                                ) {
                                    Text(
                                        text = "$fps FPS",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Audio Quality Section
            Text(
                text = "2. Audio Quality",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    AudioQuality.entries.forEach { audio ->
                        val isSelected = userSettings.audioQuality == audio
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateUserSettings { it.copy(audioQuality = audio) } }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = audio.label, fontSize = 13.sp, color = Color.White)
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = LiveCyan, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Noise Cancellation Filter", color = Color.White, fontSize = 13.sp)
                        Switch(
                            checked = userSettings.noiseCancellation,
                            onCheckedChange = { isChecked ->
                                viewModel.updateUserSettings { it.copy(noiseCancellation = isChecked) }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = LiveCyan)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Screen Orientation Section
            Text(
                text = "3. Screen Orientation",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    StreamOrientation.entries.forEach { orientation ->
                        val isSelected = userSettings.streamOrientation == orientation
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateUserSettings { it.copy(streamOrientation = orientation) } }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = orientation.label, fontSize = 13.sp, color = Color.White)
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = LiveCyan, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Notification Settings Section
            Text(
                text = "4. Notification Settings",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Stream Status Alerts", color = Color.White, fontSize = 13.sp)
                        Switch(
                            checked = userSettings.streamNotifications,
                            onCheckedChange = { isChecked ->
                                viewModel.updateUserSettings { it.copy(streamNotifications = isChecked) }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = LiveCyan)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Viewer Milestone Alerts", color = Color.White, fontSize = 13.sp)
                        Switch(
                            checked = userSettings.milestoneAlerts,
                            onCheckedChange = { isChecked ->
                                viewModel.updateUserSettings { it.copy(milestoneAlerts = isChecked) }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = LiveCyan)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 5. Connected Accounts Section
            Text(
                text = "5. Connected Accounts",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Facebook Connection Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(FacebookBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("f", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Facebook Live", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(
                                    text = if (fbAccount.isConnected) fbAccount.accountName else "Disconnected",
                                    fontSize = 11.sp,
                                    color = if (fbAccount.isConnected) StatusGreen else Color(0xFF94A3B8)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { editingPlatform = StreamPlatform.FACEBOOK }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Facebook", tint = LiveCyan)
                            }
                            TextButton(onClick = { viewModel.accountManager.toggleFacebookConnection() }) {
                                Text(
                                    text = if (fbAccount.isConnected) "Disconnect" else "Connect",
                                    color = if (fbAccount.isConnected) Color(0xFFEF4444) else FacebookBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // YouTube Connection Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(YouTubeRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", color = Color.White, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Google / YouTube", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(
                                    text = if (ytAccount.isConnected) ytAccount.accountName else "Disconnected",
                                    fontSize = 11.sp,
                                    color = if (ytAccount.isConnected) StatusGreen else Color(0xFF94A3B8)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { editingPlatform = StreamPlatform.YOUTUBE }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit YouTube", tint = LiveCyan)
                            }
                            TextButton(onClick = { viewModel.accountManager.toggleYouTubeConnection() }) {
                                Text(
                                    text = if (ytAccount.isConnected) "Disconnect" else "Connect",
                                    color = if (ytAccount.isConnected) Color(0xFFEF4444) else YouTubeRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 6. Privacy Policy Button
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPrivacyDialog = true }
                    .testTag("settings_privacy_policy_row")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = LiveCyan)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Privacy Policy & Platform Compliance", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                    Text("Read", color = LiveCyan, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7. Delete Account Button
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF7F1D1D).copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDeleteAccountDialog = true }
                    .testTag("settings_delete_account_row")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Delete Account & Wipe Data", fontSize = 14.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.SemiBold)
                    }
                    Text("Clear", color = Color(0xFFEF4444), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 8. Logout Button
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("settings_logout_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B), contentColor = Color.White)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Logout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text(text = "Privacy Policy & Platform Compliance", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "LiveCast Studio complies with Facebook and YouTube Live API policies:\n\n" +
                            "• User authentication tokens are stored locally on your device.\n" +
                            "• Real-time video and audio streams are broadcast directly to your selected endpoint.\n" +
                            "• Screen capture mode streams device visuals only while you are actively broadcasting.\n" +
                            "• Follow platform community rules regarding copyright and safety to avoid restrictions.",
                    fontSize = 13.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Close", color = LiveCyan)
                }
            }
        )
    }

    // Delete Account Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text(text = "Delete Account & Wipe Local Data?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "This will disconnect Facebook and Google tokens, permanently clear your Live History from local storage, and reset settings.",
                    fontSize = 13.sp,
                    color = Color(0xFFCBD5E1)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountDialog = false
                        viewModel.deleteAccount()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Delete & Wipe", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel", color = LiveCyan)
                }
            }
        )
    }

    // Custom Account Editor Modal Dialog
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
