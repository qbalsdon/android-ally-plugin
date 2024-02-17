package com.balsdon.androidallyplugin.model

import AndroidDeviceTestFake
import com.balsdon.androidallyplugin.values.TalkBackGranularity
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Tests the script sent to the device to navigate talkback for developers is correct.
 */
class AndroidDeviceNavigateTalkBackTest {
    @Test
    fun tb4d_next_default() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dNavigate(true)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.next")
    }

    @Test
    fun tb4d_next_default_with_parameter() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dNavigate(true, TalkBackGranularity.Default)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.next")
    }

    @Test
    fun tb4d_next_heading() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dNavigate(true, TalkBackGranularity.Headings)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.next -e mode headings")
    }

    @Test
    fun tb4d_previous_default() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dNavigate(false)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.previous")
    }

    @Test
    fun tb4d_previous_default_with_parameter() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dNavigate(false, TalkBackGranularity.Default)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.previous")
    }

    @Test
    fun tb4d_previous_control() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dNavigate(false, TalkBackGranularity.Controls)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.previous -e mode controls")
    }

    @Test
    fun tb4d_tap_control() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dActivate()
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.perform_click_action")
    }

    @Test
    fun tb4d_long_tap_control() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dActivate(true)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.perform_long_click_action")
    }

    @Test
    fun tb4d_show_menu() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dShowMenu()
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.talkback_breakout")
    }

    @Test
    fun tb4d_show_actions() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.tb4dShowMenu(true)
        assertThat(testSubject.executedCommands[0]).isEqualTo("am broadcast -a com.a11y.adb.show_custom_actions")
    }
}