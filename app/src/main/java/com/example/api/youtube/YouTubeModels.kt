package com.example.api.youtube

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeChannelResponse(
    @Json(name = "items") val items: List<YouTubeChannelItem>? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeChannelItem(
    @Json(name = "id") val id: String,
    @Json(name = "snippet") val snippet: YouTubeChannelSnippet? = null,
    @Json(name = "statistics") val statistics: YouTubeChannelStats? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeChannelSnippet(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "customUrl") val customUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeChannelStats(
    @Json(name = "subscriberCount") val subscriberCount: String? = null,
    @Json(name = "videoCount") val videoCount: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateBroadcastRequest(
    @Json(name = "snippet") val snippet: BroadcastSnippet,
    @Json(name = "status") val status: BroadcastStatus,
    @Json(name = "contentDetails") val contentDetails: BroadcastContentDetails? = null
)

@JsonClass(generateAdapter = true)
data class BroadcastSnippet(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "scheduledStartTime") val scheduledStartTime: String
)

@JsonClass(generateAdapter = true)
data class BroadcastStatus(
    @Json(name = "privacyStatus") val privacyStatus: String // "public", "private", "unlisted"
)

@JsonClass(generateAdapter = true)
data class BroadcastContentDetails(
    @Json(name = "enableAutoStart") val enableAutoStart: Boolean = true,
    @Json(name = "latencyPreference") val latencyPreference: String = "low" // "normal", "low", "ultraLow"
)

@JsonClass(generateAdapter = true)
data class LiveBroadcastResource(
    @Json(name = "id") val id: String,
    @Json(name = "snippet") val snippet: BroadcastSnippet? = null,
    @Json(name = "contentDetails") val contentDetails: Map<String, Any>? = null
)

@JsonClass(generateAdapter = true)
data class CreateStreamRequest(
    @Json(name = "snippet") val snippet: StreamSnippet,
    @Json(name = "cdn") val cdn: StreamCdn
)

@JsonClass(generateAdapter = true)
data class StreamSnippet(
    @Json(name = "title") val title: String
)

@JsonClass(generateAdapter = true)
data class StreamCdn(
    @Json(name = "frameRate") val frameRate: String = "60fps",
    @Json(name = "ingestionType") val ingestionType: String = "rtmp",
    @Json(name = "resolution") val resolution: String = "1080p"
)

@JsonClass(generateAdapter = true)
data class LiveStreamResource(
    @Json(name = "id") val id: String,
    @Json(name = "cdn") val cdn: IngestionCdnDetails? = null
)

@JsonClass(generateAdapter = true)
data class IngestionCdnDetails(
    @Json(name = "ingestionInfo") val ingestionInfo: IngestionInfo? = null
)

@JsonClass(generateAdapter = true)
data class IngestionInfo(
    @Json(name = "ingestionAddress") val ingestionAddress: String? = null,
    @Json(name = "streamName") val streamName: String? = null // This is the RTMP Stream Key
)
