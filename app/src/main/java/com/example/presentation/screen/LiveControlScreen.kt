package com.example.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StreamMode
import com.example.data.model.StreamPlatform
import com.example.presentation.components.LiveCameraPreview
import com.example.presentation.viewmodel.LiveViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.LiveCyan
import com.example.ui.theme.LiveRed
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.YouTubeRed

@Composable
fun LiveControlScreen(
    viewModel: LiveViewModel,
    onStopLiveCompleted: () -> Unit
) {
    val engine = viewModel.streamEngine
    val platform by engine.currentPlatform.collectAsState()
    val mode by engine.currentMode.collectAsState()
    val title by engine.streamTitle.collectAsState()
    val stats by engine.stats.collectAsState()
    val comments by engine.comments.collectAsState()

    var hostMessageText by remember { mutableStateOf("") }
    var showEndStreamConfirmation by remember { mutableStateOf(false) }

    val commentsListState = rememberLazyListState()

    // Auto-scroll to latest comment when new comments arrive
    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) {
            commentsListState.animateScrollToItem(comments.size - 1)
        }
    }

    BackHandler {
        showEndStreamConfirmation = true
    }

    // Format duration to HH:MM:SS
    val formattedDuration = remember(stats.durationSeconds) {
        val hours = stats.durationSeconds / 3600
        val minutes = (stats.durationSeconds % 3600) / 60
        val seconds = stats.durationSeconds % 60
        if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // --- 1. Background Source Layer ---
        if (mode == StreamMode.CAMERA && !stats.isCameraOff) {
            LiveCameraPreview(
                isFrontCamera = stats.isUsingFrontCamera,
                modifier = Modifier.fillMaxSize()
            )
        } else if (mode == StreamMode.SCREEN) {
            // Simulated Screen Capture background with visualizer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF020617)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(2.dp, LiveCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ScreenShare,
                            contentDescription = "Screen Live",
                            tint = LiveCyan,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Screen Live Broadcast Active",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Capturing device display buffer @ 1080p 60fps",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )

                    if (stats.isPrivacyShieldActive) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = Color(0xFF7F1D1D),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Privacy Shield Active (Screen Paused)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // Camera Turned Off state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B0F19)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideocamOff,
                        contentDescription = "Camera Off",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Camera Preview Muted",
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // --- 2. Top Broadcast HUD (Live Duration, Connection Status, Viewers) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LIVE Badge & Duration
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = LiveRed,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "LIVE",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Text(
                            text = formattedDuration,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("live_duration_ticker")
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Platform Tag
                    Surface(
                        color = if (platform == StreamPlatform.FACEBOOK) FacebookBlue else YouTubeRed,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (platform == StreamPlatform.FACEBOOK) "FB" else "YT",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }

                // Stop Live Button (Prompt)
                Button(
                    onClick = { showEndStreamConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor = LiveRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("stop_live_button")
                ) {
                    Icon(imageVector = Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("End Live", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Connection Status & Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connection Status
                Surface(
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${stats.connectionQuality} (${stats.currentBitrateKbps} kbps • ${stats.currentFps} FPS)",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Viewers & Likes
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = LiveCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${stats.viewerCount}",
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("viewer_count")
                            )
                        }
                    }

                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = LiveRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${stats.likesCount}",
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Bottom Layer: Live Comments View & Host Controls ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            // Live Stream Comments View (Chat Overlay)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Color.Black.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp))
                    .padding(8.dp)
            ) {
                LazyColumn(
                    state = commentsListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("comments_list"),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(comments, key = { it.id }) { comment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (comment.isPinned) Color(0x4D06B6D4) else Color(0x26000000),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            if (comment.authorBadge != null) {
                                Surface(
                                    color = if (comment.isHost) LiveRed else LiveCyan,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = comment.authorBadge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            Column {
                                Text(
                                    text = comment.authorName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (comment.isHost) LiveRed else Color(0xFF93C5FD)
                                )
                                Text(
                                    text = comment.message,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Host Comment Input Field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = hostMessageText,
                    onValueChange = { hostMessageText = it },
                    placeholder = { Text("Comment as Host...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("host_comment_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Black.copy(alpha = 0.7f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.7f),
                        focusedBorderColor = LiveCyan,
                        unfocusedBorderColor = Color(0xFF475569),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (hostMessageText.isNotBlank()) {
                            engine.sendHostComment(hostMessageText.trim())
                            hostMessageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(LiveCyan)
                        .testTag("send_comment_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Hardware Control Bar: Mic, Camera, Flip, Privacy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Microphone Toggle
                IconButton(
                    onClick = { engine.toggleMic() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (stats.isMicMuted) LiveRed else Color.Black.copy(alpha = 0.75f))
                        .border(1.dp, Color(0xFF475569), CircleShape)
                        .testTag("toggle_mic_button")
                ) {
                    Icon(
                        imageVector = if (stats.isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Toggle Mic",
                        tint = Color.White
                    )
                }

                // 2. Camera On/Off Toggle
                IconButton(
                    onClick = { engine.toggleCamera() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (stats.isCameraOff) LiveRed else Color.Black.copy(alpha = 0.75f))
                        .border(1.dp, Color(0xFF475569), CircleShape)
                        .testTag("toggle_camera_button")
                ) {
                    Icon(
                        imageVector = if (stats.isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Toggle Camera",
                        tint = Color.White
                    )
                }

                // 3. Switch Front / Back Camera (or Privacy shield if in screen mode)
                if (mode == StreamMode.CAMERA) {
                    IconButton(
                        onClick = { engine.switchCameraLens() },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.75f))
                            .border(1.dp, Color(0xFF475569), CircleShape)
                            .testTag("switch_lens_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cameraswitch,
                            contentDescription = "Switch Camera",
                            tint = Color.White
                        )
                    }
                } else {
                    IconButton(
                        onClick = { engine.togglePrivacyShield() },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (stats.isPrivacyShieldActive) Color(0xFFB45309) else Color.Black.copy(alpha = 0.75f))
                            .border(1.dp, Color(0xFF475569), CircleShape)
                            .testTag("privacy_shield_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Privacy Shield",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    // Stop Live Confirmation Dialog
    if (showEndStreamConfirmation) {
        AlertDialog(
            onDismissRequest = { showEndStreamConfirmation = false },
            title = {
                Text(
                    text = "End Live Stream?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to end this live broadcast? Your stream duration, peak viewers, and performance metrics will be saved to your Live History.",
                    fontSize = 14.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEndStreamConfirmation = false
                        viewModel.stopCurrentLiveAndSave()
                        onStopLiveCompleted()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LiveRed),
                    modifier = Modifier.testTag("confirm_stop_live_btn")
                ) {
                    Text("End Broadcast", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndStreamConfirmation = false }) {
                    Text("Resume Stream", color = LiveCyan)
                }
            }
        )
    }
}
