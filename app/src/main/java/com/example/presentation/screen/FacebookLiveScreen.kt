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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.data.model.StreamMode
import com.example.data.model.StreamPlatform
import com.example.data.model.StreamPrivacy
import com.example.presentation.viewmodel.LiveViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.LiveCyan
import com.example.ui.theme.LiveRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacebookLiveScreen(
    viewModel: LiveViewModel,
    onBack: () -> Unit,
    onStartCameraLive: () -> Unit,
    onStartScreenLive: () -> Unit
) {
    BackHandler { onBack() }

    val formState by viewModel.facebookForm.collectAsState()
    val fbAccount by viewModel.accountManager.facebookAccount.collectAsState()

    var showDestinationMenu by remember { mutableStateOf(false) }

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
                    modifier = Modifier.testTag("fb_back_button")
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
                        .background(FacebookBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "f",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Facebook Live Studio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Configure Broadcast",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Facebook Account Selection Card
            Text(
                text = "1. Facebook Account",
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
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(FacebookBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("f", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fbAccount.accountName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = fbAccount.accountHandle,
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Text(
                        text = "Active",
                        fontSize = 11.sp,
                        color = LiveCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Page or Profile Selection
            Text(
                text = "2. Page or Profile Selection",
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
                        .clickable { showDestinationMenu = true }
                        .testTag("destination_selector_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Stream Destination",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = formState.selectedDestination,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = Color.White
                        )
                    }
                }

                DropdownMenu(
                    expanded = showDestinationMenu,
                    onDismissRequest = { showDestinationMenu = false },
                    modifier = Modifier.background(DarkSurfaceVariant)
                ) {
                    fbAccount.availableDestinations.forEach { destination ->
                        DropdownMenuItem(
                            text = { Text(destination, color = Color.White, fontSize = 13.sp) },
                            onClick = {
                                viewModel.updateFacebookForm { it.copy(selectedDestination = destination) }
                                showDestinationMenu = false
                            },
                            trailingIcon = {
                                if (formState.selectedDestination == destination) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = LiveCyan)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. Live Title Input
            Text(
                text = "3. Live Title",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = formState.title,
                onValueChange = { newTitle -> viewModel.updateFacebookForm { it.copy(title = newTitle) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fb_title_input"),
                placeholder = { Text("What is this live broadcast about?", color = Color(0xFF64748B)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FacebookBlue,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Live Description Input
            Text(
                text = "4. Live Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LiveCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = formState.description,
                onValueChange = { newDesc -> viewModel.updateFacebookForm { it.copy(description = newDesc) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fb_description_input"),
                placeholder = { Text("Add details, links, or guest info...", color = Color(0xFF64748B)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FacebookBlue,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 5. Privacy Selection
            Text(
                text = "5. Privacy Selection",
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
                    Triple(StreamPrivacy.FRIENDS, "Friends", Icons.Default.People),
                    Triple(StreamPrivacy.ONLY_ME, "Only Me", Icons.Default.Lock)
                ).forEach { (privacy, label, icon) ->
                    val isSelected = formState.privacy == privacy
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) FacebookBlue else DarkSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) FacebookBlue else DarkSurfaceBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateFacebookForm { it.copy(privacy = privacy) } }
                            .testTag("privacy_${label.lowercase()}")
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
                        containerColor = if (isCamera) Color(0xFF1E3A8A) else DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isCamera) FacebookBlue else DarkSurfaceBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateFacebookForm { it.copy(mode = StreamMode.CAMERA) } }
                        .testTag("camera_live_mode_btn")
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
                        containerColor = if (isScreen) Color(0xFF1E3A8A) else DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isScreen) FacebookBlue else DarkSurfaceBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateFacebookForm { it.copy(mode = StreamMode.SCREEN) } }
                        .testTag("screen_live_mode_btn")
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
                        viewModel.prepareScreenLive(StreamPlatform.FACEBOOK)
                        onStartScreenLive()
                    } else {
                        viewModel.startFacebookLive()
                        onStartCameraLive()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("start_fb_live_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FacebookBlue,
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
                    .testTag("cancel_fb_live_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
            ) {
                Text(text = "Stop / Cancel Setup", color = Color(0xFFCBD5E1), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
