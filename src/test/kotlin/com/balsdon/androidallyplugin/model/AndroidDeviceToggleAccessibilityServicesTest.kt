package com.balsdon.androidallyplugin.model

import AndroidDeviceTestFake
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Tests the script sent to the device to toggle the service is correct.
 *
 * PLEASE NOTE:
 * 1. This plugin will only ever support TalkBack for Developers @see https://github.com/qbalsdon/talkback/tree/main
 *      and not any other screen reader. This includes Google's TalkBack, Samsung's TalkBack, Amazon VoiceView and
 *      CSR - unless they add ADB features, in which case they will be given a separate tab. Separation of concerns
 *      is the highest priority in this code.
 * 2. Not all devices need accessibility_enabled and touch_exploration_enabled, but some do
 * 3. There might be many opinions on how to turn services off, but the best way is to remove them from the colon
 *      separated list in secure enabled_accessibility_services. Putting a dummy service in its place can render
 *      some devices unusable.
 */
class AndroidDeviceToggleAccessibilityServicesTest {
    @Test
    fun turn_on_tb4d_services() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.turnOnTalkBack()
        assertThat(testSubject.executedCommands[0]).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" != *\"com.android.talkback4d/com.developer.talkback.TalkBackDevService\"* ]]; then if [[ -z \"\$CURRENT\" || \"\$CURRENT\" == \"null\" ]]; then settings put secure enabled_accessibility_services com.android.talkback4d/com.developer.talkback.TalkBackDevService; else settings put secure enabled_accessibility_services \$CURRENT:com.android.talkback4d/com.developer.talkback.TalkBackDevService; fi; settings put secure accessibility_enabled 1; settings put secure touch_exploration_enabled 1; fi")
    }

    @Test
    fun turn_off_tb4d_services() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        testSubject.device.turnOffTalkBack()
        assertThat(testSubject.executedCommands[0]).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" == *\"com.android.talkback4d/com.developer.talkback.TalkBackDevService\"* ]]; then SERVICE=com.android.talkback4d/com.developer.talkback.TalkBackDevService; FINAL_RESULT=\"\${CURRENT//:\$SERVICE/}\"; FINAL_RESULT=\"\${FINAL_RESULT//SERVICE:/}\"; FINAL_RESULT=\"\${FINAL_RESULT//\$SERVICE/}\"; settings put secure enabled_accessibility_services \"\$FINAL_RESULT\"; settings put secure accessibility_enabled 0; settings put secure touch_exploration_enabled 0; fi")
    }

    @Test
    fun turn_on_scanner_services() {
        val testSubject = AndroidDeviceTestFake()

        testSubject.device.turnOnAccessibilityScanner()
        assertThat(testSubject.executedCommands[0]).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" != *\"com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService\"* ]]; then if [[ -z \"\$CURRENT\" || \"\$CURRENT\" == \"null\" ]]; then settings put secure enabled_accessibility_services com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService; else settings put secure enabled_accessibility_services \$CURRENT:com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService; fi; fi")
    }

    @Test
    fun turn_off_scanner_services() {
        val testSubject = AndroidDeviceTestFake()

        testSubject.device.turnOffAccessibilityScanner()
        assertThat(testSubject.executedCommands[0]).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" == *\"com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService\"* ]]; then SERVICE=com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService; FINAL_RESULT=\"\${CURRENT//:\$SERVICE/}\"; FINAL_RESULT=\"\${FINAL_RESULT//SERVICE:/}\"; FINAL_RESULT=\"\${FINAL_RESULT//\$SERVICE/}\"; settings put secure enabled_accessibility_services \"\$FINAL_RESULT\"; fi")
    }
}