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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.StreamPlatform
import com.example.presentation.components.CustomAccountDialog
import com.example.presentation.viewmodel.LiveViewModel
import com.example.ui.theme.DarkBg
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.LiveCyan
import com.example.ui.theme.YouTubeRed

@Composable
fun LoginScreen(
    viewModel: LiveViewModel,
    onLoginSuccess: () -> Unit
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var editingPlatform by remember { mutableStateOf<StreamPlatform?>(null) }

    val fbAccount by viewModel.accountManager.facebookAccount.collectAsState()
    val ytAccount by viewModel.accountManager.youtubeAccount.collectAsState()

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
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // App Icon & Title
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, LiveCyan, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.live_stream_studio_icon_1790841229670),
                    contentDescription = "LiveCast Studio Icon",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "LiveCast Studio",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Connect or Enter Your Personal Logins",
                fontSize = 13.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Policy Disclaimer Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Information",
                        tint = LiveCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Add Your Own Accounts & Stream Keys",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LiveCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You can enter your own real Facebook Page/Profile and YouTube Channel stream keys below, or use auto-login. Both platforms operate independently.",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Facebook Account Card with Quick Login & Edit
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FacebookBlue.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(FacebookBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("f", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
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
                        }

                        IconButton(
                            onClick = { editingPlatform = StreamPlatform.FACEBOOK },
                            modifier = Modifier.testTag("edit_fb_account_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Account", tint = LiveCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.loginWithFacebook()
                            onLoginSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("facebook_login_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Login with Facebook", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            if (fbAccount.isConnected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. YouTube Account Card with Quick Login & Edit
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, YouTubeRed.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(YouTubeRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = ytAccount.accountName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = ytAccount.accountHandle,
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        IconButton(
                            onClick = { editingPlatform = StreamPlatform.YOUTUBE },
                            modifier = Modifier.testTag("edit_yt_account_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Account", tint = LiveCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.loginWithGoogle()
                            onLoginSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("google_login_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Login with Google (YouTube)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            if (ytAccount.isConnected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Enter Studio Directly Button
            OutlinedButton(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("continue_dashboard_button"),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
            ) {
                Text(text = "Enter Broadcast Studio", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Privacy Policy & Terms Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Privacy Policy",
                    fontSize = 13.sp,
                    color = LiveCyan,
                    modifier = Modifier
                        .clickable { showPrivacyDialog = true }
                        .padding(8.dp)
                        .testTag("privacy_policy_link")
                )
                Text(text = " • ", color = Color(0xFF64748B), fontSize = 14.sp)
                Text(
                    text = "Terms and Conditions",
                    fontSize = 13.sp,
                    color = LiveCyan,
                    modifier = Modifier
                        .clickable { showTermsDialog = true }
                        .padding(8.dp)
                        .testTag("terms_link")
                )
            }
        }
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
            initialApiToken = acc.apiAccessToken,
            accountManager = viewModel.accountManager,
            onDismiss = { editingPlatform = null },
            onSave = { name, handle, dest, key, rtmp, apiToken ->
                if (platform == StreamPlatform.FACEBOOK) {
                    viewModel.accountManager.saveCustomFacebookAccount(name, handle, dest, key, rtmp, apiToken)
                    viewModel.updateFacebookForm { it.copy(selectedAccount = "$name ($handle)", selectedDestination = dest) }
                } else {
                    viewModel.accountManager.saveCustomYouTubeAccount(name, handle, dest, key, rtmp, apiToken)
                    viewModel.updateYouTubeForm { it.copy(selectedChannel = dest) }
                }
                editingPlatform = null
            }
        )
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text(text = "Privacy Policy", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "LiveCast Studio saves your stream keys and account names locally on your Android device. Your stream credentials are never transmitted to third-party tracking servers.",
                    fontSize = 13.sp,
                    color = Color(0xFFE2E8F0)
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) { Text("OK", color = LiveCyan) }
            }
        )
    }

    // Terms Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text(text = "Terms and Conditions", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Ensure all broadcasts comply with Meta Facebook and Google YouTube terms of service. Avoid broadcasting copyrighted audio or protected visuals without authorization.",
                    fontSize = 13.sp,
                    color = Color(0xFFE2E8F0)
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) { Text("I Agree", color = LiveCyan) }
            }
        )
    }
}
