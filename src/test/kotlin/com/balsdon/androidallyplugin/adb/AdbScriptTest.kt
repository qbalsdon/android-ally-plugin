package com.balsdon.androidallyplugin.adb

import com.balsdon.androidallyplugin.adb.parameters.AdbKeyCode
import com.balsdon.androidallyplugin.adb.parameters.ColorCorrectionType
import com.balsdon.androidallyplugin.adb.parameters.InternalSettingType
import com.balsdon.androidallyplugin.adb.parameters.TalkBackAction
import com.balsdon.androidallyplugin.adb.parameters.TalkBackGranularity
import com.balsdon.androidallyplugin.adb.parameters.TalkBackSetting
import com.balsdon.androidallyplugin.adb.parameters.TalkBackVolumeSetting
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AdbScriptTest {
    @Test
    fun turn_talkback_on() {
        val result = talkBackService(true).asScript()
        assertThat(result).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" != *\"com.android.talkback4d/com.developer.talkback.TalkBackDevService\"* ]]; then if [[ -z \"\$CURRENT\" || \"\$CURRENT\" == \"null\" ]]; then settings put secure enabled_accessibility_services com.android.talkback4d/com.developer.talkback.TalkBackDevService; else settings put secure enabled_accessibility_services \$CURRENT:com.android.talkback4d/com.developer.talkback.TalkBackDevService; fi; settings put secure accessibility_enabled 1; settings put secure touch_exploration_enabled 1; fi")
    }

    @Test
    fun turn_talkback_off() {
        val result = talkBackService(false).asScript()
        assertThat(result).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" == *\"com.android.talkback4d/com.developer.talkback.TalkBackDevService\"* ]]; then SERVICE=com.android.talkback4d/com.developer.talkback.TalkBackDevService; FINAL_RESULT=\"\${CURRENT//:\$SERVICE/}\"; FINAL_RESULT=\"\${FINAL_RESULT//SERVICE:/}\"; FINAL_RESULT=\"\${FINAL_RESULT//\$SERVICE/}\"; settings put secure enabled_accessibility_services \"\$FINAL_RESULT\"; settings put secure accessibility_enabled 0; settings put secure touch_exploration_enabled 0; fi")
    }

    @Test
    fun turn_accessibility_scanner_on() {
        val result = accessibilityScannerService(true).asScript()
        assertThat(result).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" != *\"com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService\"* ]]; then if [[ -z \"\$CURRENT\" || \"\$CURRENT\" == \"null\" ]]; then settings put secure enabled_accessibility_services com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService; else settings put secure enabled_accessibility_services \$CURRENT:com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService; fi; fi")
    }

    @Test
    fun turn_accessibility_scanner_off() {
        val result = accessibilityScannerService(false).asScript()
        assertThat(result).isEqualTo("CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" == *\"com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService\"* ]]; then SERVICE=com.google.android.apps.accessibility.auditor/com.google.android.apps.accessibility.auditor.ScannerService; FINAL_RESULT=\"\${CURRENT//:\$SERVICE/}\"; FINAL_RESULT=\"\${FINAL_RESULT//SERVICE:/}\"; FINAL_RESULT=\"\${FINAL_RESULT//\$SERVICE/}\"; settings put secure enabled_accessibility_services \"\$FINAL_RESULT\"; fi")
    }

    @Test
    fun creates_an_anonymous_script() {
        val path = AdbScriptTest::class
            .java
            .getResource("/scripts/test.sh")?.file!!
        val result = AdbScript.FileScript(path).asScript()
        val expected = AdbScriptTest::class
            .java
            .getResource("/scripts/test.sh")?.readText()?.trim()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun creates_an_anonymous_script_with_parameters() {
        val path = AdbScriptTest::class
            .java
            .getResource("/scripts/testParam.sh")!!.path
        val result =
            AdbScript.FileScript(path, listOf("parameter1", "parameter2")).asScript()
        val expected = """
            #!/bin/sh

            TEST=parameter1

            echo "I passed the test with ${'$'}TEST"
            echo "I passed the test with parameter2"
        """.trimIndent()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun creates_a_tb4d_command_with_default_granularity() {
        val result = AdbScript.TalkBackUserAction(TalkBackAction.NEXT).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.next -e mode default")
    }

    @Test
    fun creates_a_tb4d_command_with_non_default_granularity() {
        val result = AdbScript.TalkBackUserAction(TalkBackAction.PREVIOUS, TalkBackGranularity.Headings).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.previous -e mode headings")
    }

    @Test
    fun creates_a_tb4d_command_tap() {
        val result = AdbScript.TalkBackUserAction(TalkBackAction.PERFORM_CLICK_ACTION).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.perform_click_action")
    }

    @Test
    fun creates_a_tb4d_command_long_tap() {
        val result = AdbScript.TalkBackUserAction(TalkBackAction.PERFORM_LONG_CLICK_ACTION).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.perform_long_click_action")
    }

    @Test
    fun creates_a_tb4d_command_open_actions() {
        val result = AdbScript.TalkBackUserAction(TalkBackAction.SHOW_CUSTOM_ACTIONS).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.show_custom_actions")
    }

    @Test
    fun creates_a_tb4d_command_menu() {
        val result = AdbScript.TalkBackUserAction(TalkBackAction.TALKBACK_BREAKOUT).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.talkback_breakout")
    }

    @Test
    fun creates_a_tb4d_command_to_turn_down_volume() {
        val result = AdbScript.TalkBackSetVolume(TalkBackVolumeSetting.VOLUME_MIN).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.${TalkBackVolumeSetting.VOLUME_MIN.name.lowercase()}")
    }

    @Test
    fun `creates a tb4d command to turn up volume`() {
        val result = AdbScript.TalkBackSetVolume(TalkBackVolumeSetting.VOLUME_MAX).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.${TalkBackVolumeSetting.VOLUME_MAX.name.lowercase()}")
    }

    @Test
    fun `creates a tb4d command to turn turn off toast messages`() {
        val result = AdbScript.TalkBackChangeSetting(TalkBackSetting.TOGGLE_SPEECH_OUTPUT, false).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.${TalkBackSetting.TOGGLE_SPEECH_OUTPUT.name.lowercase()} -e value false")
    }

    @Test
    fun creates_a_tb4d_command_to_turn_turn_on_toast_messages() {
        val result = AdbScript.TalkBackChangeSetting(TalkBackSetting.TOGGLE_SPEECH_OUTPUT, true).asScript()
        assertThat(result).isEqualTo("am broadcast -a com.a11y.adb.${TalkBackSetting.TOGGLE_SPEECH_OUTPUT.name.lowercase()} -e value true")
    }

    @Test
    fun home_key_press_to_adb_command() {
        val result = AdbScript.PressKeyAdb(AdbKeyCode.HOME).asScript()
        assertThat(result).isEqualTo("input keyevent ${AdbKeyCode.HOME.androidValue}")
    }

    @Test
    fun `back key press to adb command`() {
        val result = AdbScript.PressKeyAdb(AdbKeyCode.BACK).asScript()
        assertThat(result).isEqualTo("input keyevent ${AdbKeyCode.BACK.androidValue}")
    }

    @Test
    fun `font scale to adb command`() {
        val result1 = fontScale(1.0f).asScript()
        val result2 = fontScale(0.5f).asScript()
        val result3 = fontScale(2.5f).asScript()
        assertThat(result1).isEqualTo("settings put system font_scale 1.0")
        assertThat(result2).isEqualTo("settings put system font_scale 0.5")
        assertThat(result3).isEqualTo("settings put system font_scale 2.5")
    }

    @Test
    fun `dark mode on to adb command`() {
        val result = darkMode(true).asScript()
        assertThat(result).isEqualTo("cmd uimode night yes")
    }

    @Test
    fun `dark mode off to adb command`() {
        val result = darkMode(false).asScript()
        assertThat(result).isEqualTo("cmd uimode night no")
    }

    @Test
    fun `run raw adb command`() {
        val result1 = powerOff().asScript()
        val result2 = reboot().asScript()
        assertThat(result1).isEqualTo("reboot -p")
        assertThat(result2).isEqualTo("reboot")
    }

    @Test
    fun `run raw adb command with suffix`() {
        val result = typeText("hello world").asScript()
        assertThat(result).isEqualTo("input text \"hello\\ world\"")
    }

    @Test
    fun `color inversion off`() {
        val result = colorInversion(false).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_inversion_enabled 0")
    }

    @Test
    fun `color inversion on`() {
        val result = colorInversion(true).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_inversion_enabled 1")
    }

    @Test
    fun `color correction on greyscale`() {
        val result = colorCorrection(ColorCorrectionType.GREYSCALE).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_daltonizer_enabled 1; settings put secure accessibility_display_daltonizer 0")
    }

    @Test
    fun `color correction on tritanomoly`() {
        val result = colorCorrection(ColorCorrectionType.TRITANOMALY).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_daltonizer_enabled 1; settings put secure accessibility_display_daltonizer 13")
    }

    @Test
    fun `color correction on protanomaly`() {
        val result = colorCorrection(ColorCorrectionType.PROTANOMALY).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_daltonizer_enabled 1; settings put secure accessibility_display_daltonizer 11")
    }

    @Test
    fun `color correction on deuteranomoly`() {
        val result = colorCorrection(ColorCorrectionType.DEUTERANOMALY).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_daltonizer_enabled 1; settings put secure accessibility_display_daltonizer 12")
    }

    @Test
    fun `color correction off`() {
        val result = colorCorrection(ColorCorrectionType.OFF).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_display_daltonizer_enabled 0")
    }

    @Test
    fun `can set time to react`() {
        listOf(0, 10000, 30000, 60000, 120000).forEach { testValue ->
            val result = timeToReact(testValue).asScript()
            assertThat(result).isEqualTo("settings put secure accessibility_non_interactive_ui_timeout_ms $testValue")
        }
    }

    @Test
    fun `can set screen density to a value`() {
        listOf(
            180,
            248,
            284,
            318,
            340,
            356,
            375,
            460,
            478,
            500,
            520,
            540
        ).forEach { testValue ->
            val result = displayDensity(testValue).asScript()
            assertThat(result).isEqualTo("wm density \"$testValue\"")
        }
    }

    @Test
    fun `can reset screen density`() {
        val result = displayDensity().asScript()
        assertThat(result).isEqualTo("wm density reset")
    }

    @Test
    fun `can turn on layout bounds`() {
        val result = layoutBounds(true).asScript()
        assertThat(result).isEqualTo("setprop debug.layout 1; service call activity 1599295570 > /dev/null 2>&1")
    }

    @Test
    fun `can turn off layout bounds`() {
        val result = layoutBounds(false).asScript()
        assertThat(result).isEqualTo("setprop debug.layout 0; service call activity 1599295570 > /dev/null 2>&1")
    }

    @Test
    fun `can turn on animations`() {
        val result = animations(true).asScript()
        assertThat(result).isEqualTo("cmd settings put global animator_duration_scale 1.0; cmd settings put global transition_animation_scale 1.0; cmd settings put global window_animation_scale 1.0")
    }

    @Test
    fun `can turn off animations`() {
        val result = animations(false).asScript()
        assertThat(result).isEqualTo("cmd settings put global animator_duration_scale 0.0; cmd settings put global transition_animation_scale 0.0; cmd settings put global window_animation_scale 0.0")
    }

    @Test
    fun `can turn on captions`() {
        val result = captions(true).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_captioning_enabled 1")
    }

    @Test
    fun `can turn off captions`() {
        val result = captions(false).asScript()
        assertThat(result).isEqualTo("settings put secure accessibility_captioning_enabled 0")
    }

    @Test
    fun `can turn on audio description`() {
        val result = audioDescription(true).asScript()
        assertThat(result).isEqualTo("settings put secure enabled_accessibility_audio_description_by_default 1; settings put secure accessibility_audio_descriptions_enabled 1")
    }

    @Test
    fun `can turn off audio description`() {
        val result = audioDescription(false).asScript()
        assertThat(result).isEqualTo("settings put secure enabled_accessibility_audio_description_by_default 0; settings put secure accessibility_audio_descriptions_enabled 0")
    }

    @Test
    fun `can turn on show touches`() {
        val result = showTouches(true).asScript()
        assertThat(result).isEqualTo("settings put system show_touches 1")
    }

    @Test
    fun `can turn off show touches`() {
        val result = showTouches(false).asScript()
        assertThat(result).isEqualTo("settings put system show_touches 0")
    }

    @Test
    fun `text high contrast`() {
        assertThat(
            highTextContrast(true).asScript()
        ).isEqualTo("settings put system high_text_contrast_enabled 1")

        assertThat(
            highTextContrast(false).asScript()
        ).isEqualTo("settings put system high_text_contrast_enabled 0")
    }

    @Test
    fun `get system settings`() {
        val result = settings(InternalSettingType.SYSTEM).asScript()
        assertThat(result).isEqualTo("settings list system")
    }

    @Test
    fun `get global settings`() {
        val result = settings(InternalSettingType.GLOBAL).asScript()
        assertThat(result).isEqualTo("settings list global")
    }

    @Test
    fun `get secure settings`() {
        val result = settings(InternalSettingType.SECURE).asScript()
        assertThat(result).isEqualTo("settings list secure")
    }

    @Test
    fun `get all settings`() {
        val result = settings(InternalSettingType.ALL).asScript()
        assertThat(result).isEqualTo("settings list system | sed 's/^/system./';settings list secure | sed 's/^/secure./';settings list global | sed 's/^/global./';")
    }
//
//    @Test
//    fun `can open screen`() {
//        SettingsScreen.values().forEach { screen ->
//            val result = AdbScript2.DEVICE_SCREEN.fetchScript(ScriptParam.ScreenValue(screen))
//            assertThat(result).isEqualTo("am start -a ${screen.source}")
//        }
//    }
//
//    @Test
//    fun `cat window dump`() {
//        val result = AdbScript2.WINDOW_DUMP.fetchScript(ScriptParam.NONE)
//        assertThat(result).isEqualTo("uiautomator dump && cat /sdcard/window_dump.xml && sleep 1.0 && rm /sdcard/window_dump.xml")
//    }
}