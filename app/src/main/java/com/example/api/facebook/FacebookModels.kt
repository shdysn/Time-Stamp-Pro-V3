package com.example.api.facebook

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FacebookUserResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String? = null
)

@JsonClass(generateAdapter = true)
data class FacebookPagesResponse(
    @Json(name = "data") val data: List<FacebookPageItem>? = null
)

@JsonClass(generateAdapter = true)
data class FacebookPageItem(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "access_token") val accessToken: String? = null,
    @Json(name = "category") val category: String? = null
)

@JsonClass(generateAdapter = true)
data class CreateFacebookLiveRequest(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "status") val status: String = "LIVE_NOW"
)

@JsonClass(generateAdapter = true)
data class FacebookLiveVideoResponse(
    @Json(name = "id") val id: String,
    @Json(name = "stream_url") val streamUrl: String? = null,
    @Json(name = "secure_stream_url") val secureStreamUrl: String? = null,
    @Json(name = "status") val status: String? = null
)

@JsonClass(generateAdapter = true)
data class FacebookCommentsResponse(
    @Json(name = "data") val data: List<FacebookCommentItem>? = null
)

@JsonClass(generateAdapter = true)
data class FacebookCommentItem(
    @Json(name = "id") val id: String,
    @Json(name = "message") val message: String,
    @Json(name = "from") val from: FacebookCommentAuthor? = null
)

@JsonClass(generateAdapter = true)
data class FacebookCommentAuthor(
    @Json(name = "name") val name: String,
    @Json(name = "id") val id: String? = null
)
