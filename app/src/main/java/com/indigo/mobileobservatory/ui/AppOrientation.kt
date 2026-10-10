package com.indigo.mobileobservatory.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

/**
 * Imaging (camera / guide / player) stays landscape; star map and other tabs
 * prefer portrait. Manifest uses fullSensor so these runtime requests can take effect.
 */
enum class AppOrientationMode {
    PORTRAIT,
    LANDSCAPE,
    SENSOR
}

/** Window shortest side from which the shell treats the device as a large screen. */
const val LARGE_WINDOW_MIN_SIDE_DP = 600

/**
 * Orientation actually requested from the window.
 *
 * Phones keep the per-tab preference (imaging landscape, everything else
 * portrait). Large windows never force a rotation: a tablet already shows the
 * adaptive shell (side rail, wider panels) in either orientation, and forcing
 * one rotated the window under the user's hands — every tap on the bottom
 * navigation flipped the whole tablet.
 */
fun resolveOrientationMode(
    preferred: AppOrientationMode,
    windowWidthDp: Int,
    windowHeightDp: Int
): AppOrientationMode {
    val shortestSide = minOf(windowWidthDp, windowHeightDp)
    val largeWindow = shortestSide >= LARGE_WINDOW_MIN_SIDE_DP
    return if (largeWindow) AppOrientationMode.SENSOR else preferred
}

@Composable
fun RememberAppOrientation(mode: AppOrientationMode) {
    val configuration = LocalConfiguration.current
    val resolved = resolveOrientationMode(
        preferred = mode,
        windowWidthDp = configuration.screenWidthDp,
        windowHeightDp = configuration.screenHeightDp
    )
    val activity = LocalContext.current.findActivityOrNull()
    DisposableEffect(activity, resolved) {
        activity?.requestedOrientation = when (resolved) {
            AppOrientationMode.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            AppOrientationMode.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            AppOrientationMode.SENSOR -> ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
        onDispose { }
    }
}

tailrec fun Context.findActivityOrNull(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivityOrNull()
    else -> null
}
