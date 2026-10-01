package com.example.backend

import com.example.data.model.StreamPlatform

data class RTMPConfig(
    val rtmpUrl: String,
    val streamKey: String,
    val backupUrl: String = ""
) {
    companion object {
        fun getDefaultConfig(platform: StreamPlatform): RTMPConfig {
            return when (platform) {
                StreamPlatform.FACEBOOK -> RTMPConfig(
                    rtmpUrl = "rtmps://live-api-s.facebook.com:443/rtmp/",
                    streamKey = "FB-104928374-live_sec_token_9x"
                )
                StreamPlatform.YOUTUBE -> RTMPConfig(
                    rtmpUrl = "rtmp://a.rtmp.youtube.com/live2",
                    streamKey = "yt-live-prod-4b92-7f1a-8e3d"
                )
            }
        }
    }
}
