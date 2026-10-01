package com.example.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StreamLatency
import com.example.data.model.StreamMode
import com.example.data.model.StreamPlatform
import com.example.data.model.StreamPrivacy
import com.example.presentation.components.CustomAccountDialog
import com.example.presentation.viewmodel.LiveViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.LiveCyan
import com.example.ui.theme.LiveRed
import com.example.ui.theme.YouTubeRed

@Composable
fun YouTubeLiveScreen(
    viewModel: LiveViewModel,
    onBack: () -> Unit,
    onStartCameraLive: () -> Unit,
    onStartScreenLive: () -> Unit
) {
    BackHandler { onBack() }

    val formState by viewModel.youtubeForm.collectAsState()
    val ytAccount by viewModel.accountManager.youtubeAccount.collectAsState()

    var showChannelMenu by remember { mutableStateOf(false) }
    var showEditAccountDialog by remember { mutableStateOf(false) }

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
            // Top Navigation Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("yt_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(YouTubeRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "YouTube Live Studio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Broadcast Setup",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. YouTube Channel Selection
            Text(
                text = "1. YouTube Channel Selection",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showChannelMenu = true }
                        .testTag("yt_channel_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(YouTubeRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", color = Color.White, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Target Channel",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = formState.selectedChannel,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { showEditAccountDialog = true },
                                modifier = Modifier.testTag("edit_yt_account_on_live_screen")
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Channel", tint = LiveCyan)
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select",
                                tint = Color.White
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = showChannelMenu,
                    onDismissRequest = { showChannelMenu = false },
                    modifier = Modifier.background(DarkSurfaceVariant)
                ) {
                    ytAccount.availableDestinations.forEach { channel ->
                        DropdownMenuItem(
                            text = { Text(channel, color = Color.White, fontSize = 13.sp) },
                            onClick = {
                                viewModel.updateYouTubeForm { it.copy(selectedChannel = channel) }
                                showChannelMenu = false
                            },
                            trailingIcon = {
                                if (formState.selectedChannel == channel) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = LiveCyan)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Live Title Input
            Text(
                text = "2. Live Title",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = formState.title,
                onValueChange = { newTitle -> viewModel.updateYouTubeForm { it.copy(title = newTitle) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("yt_title_input"),
                placeholder = { Text("Stream title shown to YouTube audience", color = Color(0xFF64748B)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YouTubeRed,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Live Description Input
            Text(
                text = "3. Live Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = formState.description,
                onValueChange = { newDesc -> viewModel.updateYouTubeForm { it.copy(description = newDesc) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("yt_description_input"),
                placeholder = { Text("Tell viewers what your broadcast is about...", color = Color(0xFF64748B)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YouTubeRed,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Privacy Selection (Public, Unlisted, Private)
            Text(
                text = "4. Privacy Selection",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Triple(StreamPrivacy.PUBLIC, "Public", Icons.Default.Public),
                    Triple(StreamPrivacy.UNLISTED, "Unlisted", Icons.Default.Link),
                    Triple(StreamPrivacy.PRIVATE, "Private", Icons.Default.Lock)
                ).forEach { (privacy, label, icon) ->
                    val isSelected = formState.privacy == privacy
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) YouTubeRed else DarkSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) YouTubeRed else DarkSurfaceBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateYouTubeForm { it.copy(privacy = privacy) } }
                            .testTag("yt_privacy_${label.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Latency Selection
            Text(
                text = "5. Stream Latency Mode",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StreamLatency.entries.forEach { latency ->
                        val isSelected = formState.latency == latency
                        Surface(
                            color = if (isSelected) Color(0xFF7F1D1D) else DarkSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) YouTubeRed else Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.updateYouTubeForm { it.copy(latency = latency) } }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = latency.label.substringBefore(" "),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                                )
                                Text(
                                    text = "Latency",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Stream Mode Selection (Screen Live vs Camera Live)
            Text(
                text = "6. Stream Source Mode",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Camera Live Button
                val isCamera = formState.mode == StreamMode.CAMERA
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCamera) Color(0xFF7F1D1D) else DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isCamera) YouTubeRed else DarkSurfaceBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateYouTubeForm { it.copy(mode = StreamMode.CAMERA) } }
                        .testTag("yt_camera_mode_btn")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Camera Live",
                            tint = if (isCamera) Color.White else Color(0xFF94A3B8),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Camera Live",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Front/Back Camera",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                // Screen Live Button
                val isScreen = formState.mode == StreamMode.SCREEN
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isScreen) Color(0xFF7F1D1D) else DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isScreen) YouTubeRed else DarkSurfaceBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateYouTubeForm { it.copy(mode = StreamMode.SCREEN) } }
                        .testTag("yt_screen_mode_btn")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ScreenShare,
                            contentDescription = "Screen Live",
                            tint = if (isScreen) Color.White else Color(0xFF94A3B8),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Screen Live",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Game / App Casting",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 7. Start Live & Stop Live Buttons
            Button(
                onClick = {
                    if (formState.mode == StreamMode.SCREEN) {
                        viewModel.prepareScreenLive(StreamPlatform.YOUTUBE)
                        onStartScreenLive()
                    } else {
                        viewModel.startYouTubeLive()
                        onStartCameraLive()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("start_yt_live_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YouTubeRed,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (formState.mode == StreamMode.SCREEN) "Next: Grant Screen Share" else "Start Live Broadcast",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("cancel_yt_live_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
            ) {
                Text(text = "Stop / Cancel Setup", color = Color(0xFFCBD5E1), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEditAccountDialog) {
        CustomAccountDialog(
            platform = StreamPlatform.YOUTUBE,
            initialName = ytAccount.accountName,
            initialHandle = ytAccount.accountHandle,
            initialDestination = ytAccount.selectedDestination,
            initialStreamKey = ytAccount.streamKey,
            initialRtmpUrl = ytAccount.rtmpServerUrl,
            onDismiss = { showEditAccountDialog = false },
            onSave = { name, handle, dest, key, rtmp ->
                viewModel.accountManager.saveCustomYouTubeAccount(name, handle, dest, key, rtmp)
                viewModel.updateYouTubeForm { it.copy(selectedChannel = dest) }
                showEditAccountDialog = false
            }
        )
    }
}
