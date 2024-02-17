package com.balsdon.androidallyplugin.model

import AndroidDeviceTestFake
import com.balsdon.androidallyplugin.values.AdbKeyCode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Tests the script sent to the device for navigation is correct.
 */
class AndroidDeviceNavigateTest {
    @Test
    fun adb_back() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.press(AdbKeyCode.BACK)
        assertThat(testSubject.executedCommands[0]).isEqualTo("input keyevent KEYCODE_BACK")
    }

    @Test
    fun adb_home() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.press(AdbKeyCode.HOME)
        assertThat(testSubject.executedCommands[0]).isEqualTo("input keyevent KEYCODE_HOME")
    }
}