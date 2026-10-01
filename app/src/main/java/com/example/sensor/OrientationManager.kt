package com.example.sensor

import android.content.Context
import android.view.OrientationEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Monitors physical device orientation via hardware accelerometer / gyroscope
 * and resolves discrete 0°, 90°, 180°, 270° angles for camera timestamp alignment.
 */
class OrientationManager(context: Context) {

    private val _orientationDegrees = MutableStateFlow(0)
    val orientationDegrees: StateFlow<Int> = _orientationDegrees.asStateFlow()

    private val orientationEventListener = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            if (orientation == ORIENTATION_UNKNOWN) return

            // Map continuous orientation angle [0..359] to 4 discrete orientations:
            // 0: Portrait (phone held upright)
            // 90: Landscape Right (phone rotated 90° counter-clockwise, shutter on right)
            // 180: Reverse Portrait (phone upside down)
            // 270: Landscape Left (phone rotated 90° clockwise, shutter on left)
            val discrete = when (orientation) {
                in 45..134 -> 90
                in 135..224 -> 180
                in 225..314 -> 270
                else -> 0
            }

            if (_orientationDegrees.value != discrete) {
                _orientationDegrees.value = discrete
            }
        }
    }

    fun startListening() {
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable()
        }
    }

    fun stopListening() {
        orientationEventListener.disable()
    }
}
