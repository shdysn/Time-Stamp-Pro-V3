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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
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
import com.example.database.StreamHistoryEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LiveHistoryScreen(
    viewModel: LiveViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val allHistory by viewModel.streamHistory.collectAsState()
    val filter by viewModel.historyFilter.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedStreamForDetails by remember { mutableStateOf<StreamHistoryEntity?>(null) }

    val filteredList = remember(allHistory, filter, searchQuery) {
        allHistory.filter { item ->
            val matchesFilter = when (filter) {
                "FACEBOOK" -> item.platform.equals("FACEBOOK", ignoreCase = true)
                "YOUTUBE" -> item.platform.equals("YOUTUBE", ignoreCase = true)
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.destination.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }

    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()) }

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
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("history_back_btn")
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
                            text = "Live History",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${allHistory.size} Broadcast Records",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("history_search_input"),
                placeholder = { Text("Search streams by title...", color = Color(0xFF64748B), fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LiveCyan,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Platform Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Pair("ALL", "All Platforms"),
                    Pair("FACEBOOK", "Facebook Live"),
                    Pair("YOUTUBE", "YouTube Live")
                ).forEach { (key, label) ->
                    val isSelected = filter == key
                    Surface(
                        color = if (isSelected) LiveCyan else DarkSurface,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) LiveCyan else DarkSurfaceBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setHistoryFilter(key) }
                            .testTag("filter_$key")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.Black else Color.White,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // History Stream List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No Live Streams Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "Complete a Facebook or YouTube live session to see it here.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("history_records_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.id }) { stream ->
                        val isFb = stream.platform.equals("FACEBOOK", ignoreCase = true)
                        val durationMinutes = stream.durationSeconds / 60
                        val durationSeconds = stream.durationSeconds % 60
                        val durationFormatted = "${durationMinutes}m ${durationSeconds}s"
                        val dateFormatted = remember(stream.startTimeMillis) {
                            dateFormatter.format(Date(stream.startTimeMillis))
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStreamForDetails = stream }
                                .testTag("stream_history_card_${stream.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // 1. Platform Name & Live Status
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = if (isFb) FacebookBlue else YouTubeRed,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (isFb) "Facebook Live" else "YouTube Live",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Surface(
                                        color = Color(0xFF132D22),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = stream.status,
                                            color = StatusGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // 2. Live Title
                                Text(
                                    text = stream.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = stream.destination,
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // 3. Live Date & Live Duration
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dateFormatted,
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = "Duration",
                                            tint = LiveCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = durationFormatted,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Analytics & Action footer
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text("${stream.peakViewers}", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text("${stream.totalComments}", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                                        }
                                        Surface(color = DarkSurfaceVariant, shape = RoundedCornerShape(4.dp)) {
                                            Text(stream.mode, fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteHistoryItem(stream.id) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Detailed Analytics Dialog
    selectedStreamForDetails?.let { stream ->
        val isFb = stream.platform.equals("FACEBOOK", ignoreCase = true)
        AlertDialog(
            onDismissRequest = { selectedStreamForDetails = null },
            title = {
                Text(text = "Broadcast Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text(text = stream.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Platform: ${if (isFb) "Facebook Live" else "YouTube Live"}", color = LiveCyan, fontSize = 13.sp)
                    Text(text = "Destination: ${stream.destination}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Duration: ${stream.durationSeconds / 60}m ${stream.durationSeconds % 60}s", color = Color.White, fontSize = 13.sp)
                    Text(text = "Peak Viewers: ${stream.peakViewers}", color = Color.White, fontSize = 13.sp)
                    Text(text = "Total Comments: ${stream.totalComments}", color = Color.White, fontSize = 13.sp)
                    Text(text = "Resolution: ${stream.resolution} • Mode: ${stream.mode}", color = Color.White, fontSize = 13.sp)
                    Text(text = "Avg Bitrate: ${stream.averageBitrateKbps} kbps", color = Color.White, fontSize = 13.sp)
                    Text(text = "Status: ${stream.status}", color = StatusGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedStreamForDetails = null },
                    colors = ButtonDefaults.buttonColors(containerColor = LiveCyan)
                ) {
                    Text("Close", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
