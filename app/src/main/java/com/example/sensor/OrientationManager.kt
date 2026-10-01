package com.example.sensor

import android.content.Context
import android.view.OrientationEventListener
import android.view.Surface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Monitors physical device orientation via hardware accelerometer / gyroscope
 * and resolves discrete CameraX Surface rotation and UI preview rotation angles.
 */
class OrientationManager(context: Context) {

    private val _surfaceRotation = MutableStateFlow(Surface.ROTATION_0)
    val surfaceRotation: StateFlow<Int> = _surfaceRotation.asStateFlow()

    private val _orientationDegrees = MutableStateFlow(0)
    val orientationDegrees: StateFlow<Int> = _orientationDegrees.asStateFlow()

    private val orientationEventListener = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            if (orientation == ORIENTATION_UNKNOWN) return

            // Standard Android CameraX orientation mapping:
            // - 225..314: Phone held in regular landscape (rotated 90° counter-clockwise, shutter on right).
            //             Surface rotation = ROTATION_90, UI HUD rotation = 90°
            // - 45..134:  Phone held in reverse landscape (rotated 90° clockwise, shutter on left).
            //             Surface rotation = ROTATION_270, UI HUD rotation = 270°
            // - 135..224: Phone held upside down.
            //             Surface rotation = ROTATION_180, UI HUD rotation = 180°
            // - else:     Phone held in upright portrait.
            //             Surface rotation = ROTATION_0, UI HUD rotation = 0°
            val (surfRot, uiDegrees) = when (orientation) {
                in 225..314 -> Surface.ROTATION_90 to 90
                in 45..134 -> Surface.ROTATION_270 to 270
                in 135..224 -> Surface.ROTATION_180 to 180
                else -> Surface.ROTATION_0 to 0
            }

            if (_surfaceRotation.value != surfRot) {
                _surfaceRotation.value = surfRot
            }
            if (_orientationDegrees.value != uiDegrees) {
                _orientationDegrees.value = uiDegrees
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
