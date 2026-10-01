package com.example.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.LiveCyan

@Composable
fun EditProfileDialog(
    currentName: String,
    currentTagline: String,
    onDismiss: () -> Unit,
    onSave: (name: String, tagline: String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var tagline by remember { mutableStateOf(currentTagline) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Edit Streamer Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Display Name", fontSize = 12.sp, color = LiveCyan, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Your broadcaster name") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LiveCyan,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Broadcast Tagline", fontSize = 12.sp, color = LiveCyan, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = tagline,
                    onValueChange = { tagline = it },
                    placeholder = { Text("e.g. Pro Streamer / Gamer") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_tagline_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LiveCyan,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name.trim(), tagline.trim()) },
                colors = ButtonDefaults.buttonColors(containerColor = LiveCyan)
            ) {
                Text("Save Profile", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        }
    )
}
