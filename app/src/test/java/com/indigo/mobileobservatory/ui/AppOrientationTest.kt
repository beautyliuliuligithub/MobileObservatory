package com.indigo.mobileobservatory.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class AppOrientationTest {
    @Test
    fun largeWindowsFollowTheDeviceInsteadOfForcingARotation() {
        // Tablet landscape/portrait and large foldables: the adaptive shell
        // already lays out for either orientation.
        assertEquals(
            AppOrientationMode.SENSOR,
            resolveOrientationMode(AppOrientationMode.PORTRAIT, 1280, 800)
        )
        assertEquals(
            AppOrientationMode.SENSOR,
            resolveOrientationMode(AppOrientationMode.LANDSCAPE, 800, 1280)
        )
        assertEquals(
            AppOrientationMode.SENSOR,
            resolveOrientationMode(AppOrientationMode.LANDSCAPE, 1000, 620)
        )
        // The settings screen must not rotate a tablet either.
        assertEquals(
            AppOrientationMode.SENSOR,
            resolveOrientationMode(AppOrientationMode.PORTRAIT, 1000, 700)
        )
    }

    @Test
    fun phonesKeepThePerTabPreference() {
        assertEquals(
            AppOrientationMode.LANDSCAPE,
            resolveOrientationMode(AppOrientationMode.LANDSCAPE, 852, 393)
        )
        assertEquals(
            AppOrientationMode.PORTRAIT,
            resolveOrientationMode(AppOrientationMode.PORTRAIT, 393, 852)
        )
        assertEquals(
            AppOrientationMode.SENSOR,
            resolveOrientationMode(AppOrientationMode.SENSOR, 393, 852)
        )
    }

    @Test
    fun largeSquareWindowsCountAsLargeScreens() {
        assertEquals(
            AppOrientationMode.SENSOR,
            resolveOrientationMode(AppOrientationMode.PORTRAIT, 600, 600)
        )
        assertEquals(
            AppOrientationMode.PORTRAIT,
            resolveOrientationMode(AppOrientationMode.PORTRAIT, 599, 900)
        )
    }
}
