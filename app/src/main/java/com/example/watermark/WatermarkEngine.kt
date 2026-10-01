package com.example.watermark

import android.graphics.Bitmap
import android.util.Log
import com.example.data.model.LocationData
import com.example.data.model.UserSettings

object WatermarkEngine {

    private const val TAG = "WatermarkEngine"

    fun applyWatermark(
        sourceBitmap: Bitmap,
        settings: UserSettings,
        location: LocationData,
        heading: Float,
        timestampMillis: Long = System.currentTimeMillis(),
        orientationDegrees: Int = 0
    ): Bitmap {
        Log.i(TAG, "applyWatermark called with template: ${settings.templateType}, orientation: $orientationDegrees")
        val request = StampRenderRequest(
            sourceBitmap = sourceBitmap,
            templateType = settings.templateType,
            settings = settings,
            location = location,
            heading = heading,
            timestampMillis = timestampMillis,
            orientationDegrees = orientationDegrees
        )
        return renderStamp(request)
    }

    fun renderStamp(request: StampRenderRequest): Bitmap {
        Log.i(TAG, "renderStamp delegating to StampRenderer for template: ${request.templateType}")
        return StampRenderer.render(request)
    }
}
