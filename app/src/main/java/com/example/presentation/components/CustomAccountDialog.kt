package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StreamPlatform
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.LiveCyan
import com.example.ui.theme.YouTubeRed

@Composable
fun CustomAccountDialog(
    platform: StreamPlatform,
    initialName: String,
    initialHandle: String,
    initialDestination: String,
    initialStreamKey: String,
    initialRtmpUrl: String,
    onDismiss: () -> Unit,
    onSave: (name: String, handle: String, destination: String, streamKey: String, rtmpUrl: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var handle by remember { mutableStateOf(initialHandle) }
    var destination by remember { mutableStateOf(initialDestination) }
    var streamKey by remember { mutableStateOf(initialStreamKey) }
    var rtmpUrl by remember {
        mutableStateOf(
            if (initialRtmpUrl.isNotBlank()) initialRtmpUrl
            else if (platform == StreamPlatform.FACEBOOK) "rtmps://live-api-s.facebook.com:443/rtmp/"
            else "rtmp://a.rtmp.youtube.com/live2"
        )
    }
    var showStreamKey by remember { mutableStateOf(false) }

    val isFacebook = platform == StreamPlatform.FACEBOOK
    val brandColor = if (isFacebook) FacebookBlue else YouTubeRed

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(brandColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isFacebook) "f" else "▶",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isFacebook) "Add Your Facebook Login" else "Add Your YouTube Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Enter your personal account or channel details and stream key to broadcast live directly.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Account Name
                Text(
                    text = if (isFacebook) "Your Facebook Name" else "Your Channel Name",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LiveCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(if (isFacebook) "e.g. John Doe" else "e.g. My Gaming Channel", color = Color(0xFF64748B)) },
                    modifier = Modifier.fillMaxWidth().testTag("custom_account_name_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = brandColor,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Username / Handle
                Text(
                    text = "Handle / Username",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LiveCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    placeholder = { Text("e.g. @your_username", color = Color(0xFF64748B)) },
                    modifier = Modifier.fillMaxWidth().testTag("custom_account_handle_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = brandColor,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Page or Destination
                Text(
                    text = if (isFacebook) "Page or Timeline Destination" else "Primary Broadcast Destination",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LiveCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    placeholder = { Text(if (isFacebook) "e.g. My Official Creator Page" else "e.g. Main YouTube Channel", color = Color(0xFF64748B)) },
                    modifier = Modifier.fillMaxWidth().testTag("custom_destination_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = brandColor,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stream Key
                Text(
                    text = "Live Stream Key (RTMP)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LiveCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = streamKey,
                    onValueChange = { streamKey = it },
                    placeholder = { Text("Paste stream key from Producer/Studio", color = Color(0xFF64748B)) },
                    modifier = Modifier.fillMaxWidth().testTag("custom_stream_key_input"),
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (showStreamKey) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showStreamKey = !showStreamKey }) {
                            Icon(
                                imageVector = if (showStreamKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Visibility",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = brandColor,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Help text
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = LiveCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isFacebook)
                                "Get your stream key at facebook.com/live/producer"
                            else
                                "Get your stream key at studio.youtube.com (Live Dashboard)",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name.trim(), handle.trim(), destination.trim(), streamKey.trim(), rtmpUrl.trim())
                },
                colors = ButtonDefaults.buttonColors(containerColor = brandColor),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("save_custom_account_btn")
            ) {
                Text("Save & Connect", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        }
    )
}
