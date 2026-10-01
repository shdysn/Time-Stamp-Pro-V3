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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun LoginScreen(
    viewModel: LiveViewModel,
    onLoginSuccess: () -> Unit
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.height(16.dp))

            // App Icon & Title
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .border(2.dp, LiveCyan, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.live_stream_studio_icon_1790841229670),
                    contentDescription = "LiveCast Studio Icon",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "LiveCast Studio",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Connect Accounts to Broadcast",
                fontSize = 14.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Policy Disclaimer Card (from user requirements)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
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
                            text = "Platform Login Notice",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LiveCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Facebook & Google logins connect separate platforms. Google connects YouTube Live; Facebook connects Pages & Profile. Comply with community guidelines to prevent account restrictions.",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Facebook Login Button
            Button(
                onClick = {
                    viewModel.loginWithFacebook()
                    onLoginSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("facebook_login_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FacebookBlue,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "f",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = FacebookBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (fbAccount.isConnected) "Connected as ${fbAccount.accountName}" else "Login with Facebook",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (fbAccount.isConnected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Connected",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google / YouTube Login Button
            Button(
                onClick = {
                    viewModel.loginWithGoogle()
                    onLoginSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("google_login_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YouTubeRed,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = YouTubeRed
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (ytAccount.isConnected) "Connected to YouTube" else "Login with Google (YouTube)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (ytAccount.isConnected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Connected",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Continue to Home Button
            OutlinedButton(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("continue_dashboard_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
            ) {
                Text(
                    text = "Enter Broadcast Studio",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

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
                Text(
                    text = " • ",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )
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

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text(text = "Privacy Policy", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "LiveCast Studio respects your privacy. When using Facebook Live or YouTube Live streaming:\n\n" +
                                "1. Token Storage: All authentication tokens and stream keys are saved locally on your device in secure app storage.\n" +
                                "2. Screen Sharing: During screen live mode, only user-approved window and audio streams are broadcast.\n" +
                                "3. Telemetry: We do not sell or transmit your personal livestream content to third parties.\n" +
                                "4. Revocation: You can disconnect accounts and wipe local credentials at any time in Settings.",
                        fontSize = 13.sp,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Understood", color = LiveCyan)
                }
            }
        )
    }

    // Terms and Conditions Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(text = "Terms and Conditions", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "By broadcasting with LiveCast Studio, you agree to:\n\n" +
                                "1. Content Compliance: You are solely responsible for content streamed to Facebook and YouTube, ensuring it conforms to Meta and Google Community Guidelines.\n" +
                                "2. Copyright: Do not broadcast copyrighted music, movies, or video feeds without appropriate broadcasting rights.\n" +
                                "3. Account Standing: Logging in through API does not waive platform enforcement. Infringing streams may result in platform penalties by Meta or YouTube directly.\n" +
                                "4. Safe Usage: Avoid streaming sensitive personal details, credit cards, or passwords while in Screen Share mode.",
                        fontSize = 13.sp,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("I Agree", color = LiveCyan)
                }
            }
        )
    }
}
