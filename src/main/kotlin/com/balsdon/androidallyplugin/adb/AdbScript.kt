/**
 * Copyright © 2024 Quintin Balsdon
 *
 * Licensed under the MIT License (https://opensource.org/licenses/MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.balsdon.androidallyplugin.adb

import com.balsdon.androidallyplugin.adb.parameters.AdbKeyCode
import com.balsdon.androidallyplugin.adb.parameters.AdbParam
import com.balsdon.androidallyplugin.adb.parameters.InternalSettingType
import com.balsdon.androidallyplugin.adb.parameters.TalkBackAction
import com.balsdon.androidallyplugin.adb.parameters.TalkBackGranularity
import com.balsdon.androidallyplugin.adb.parameters.TalkBackSetting
import com.balsdon.androidallyplugin.adb.parameters.SliderMode
import com.balsdon.androidallyplugin.adb.parameters.TalkBackVolumeSetting
import java.io.BufferedReader
import java.io.File

/**
 * The aim of this class is to be able to generate a script that
 * is executed on the android device's shell, to run
 *
 * This file has been modified from the Android Ally desktop
 * version, and de-coupled from a menuing system that used it,
 * so optimisations are welcome!
 *
 * The fundamental philosophy is that I never do query-response
 * between the application and the device. Reading settings
 * creates problems: Having many devices connected and also with
 * regard to timing.
 *
 * If an instruction needs to read a value, rather make a bash
 * script file and test it there. The ADB connection is
 * unreliable at best, and time spent waiting for a response is
 * just not going to give a great UX. This needs to be fire and
 * forget.
 *
 * This is also why I'm reluctant to do "toggling" anymore -
 * plus devices may be in many states and if the user is doing
 * multi device testing they probably want to be certain of the
 * device state and send an atomic command, rather than a toggle
 *
 * TODO: Check if `cmd settings` can be used for those that
 *       normally don't toggle well or invoke screen refresh
 */

@Suppress("TooManyFunctions")
sealed class AdbScript {
    data class AccessibilityService(
        val on: Boolean,
        val requiresTouch: Boolean,
        val packageName: String,
        val serviceName: String
    ) : AdbScript()

    data class FileScript(
        val path: String,
        val params: List<String> = emptyList()
    ) : AdbScript()

    data class TalkBackUserAction(
        val action: TalkBackAction,
        val granularity: TalkBackGranularity = TalkBackGranularity.Default
    ) : AdbScript()

    data class TalkBackSetVolume(
        val level: TalkBackVolumeSetting
    ) : AdbScript()

    data class TalkBackSlider(
        val mode: SliderMode
    ) : AdbScript()

    data class TalkBackChangeSetting(
        val setting: TalkBackSetting,
        val enabled: Boolean
    ) : AdbScript()

    data class PressKeyAdb(val keyCode: AdbKeyCode) : AdbScript()
    data class InternalSetting<T : AdbParam>(
        val name: String,
        val internalSettingType: InternalSettingType,
        val settingValue: T
    ) : AdbScript()

    data class Command<T : AdbParam>(
        val commandScript: String,
        val params: List<T> = emptyList()
    ) : AdbScript()

    data class CombinationCommand(
        val commandList: List<AdbScript>
    ) : AdbScript()

    fun asScript(): String =
        when (this) {
            is AccessibilityService -> createAccessibilityServiceScript()
            is FileScript -> createScriptFromFile()
            is TalkBackUserAction -> createTalkBackAdbScript()
            is TalkBackSetVolume -> createVolumeChangeScript()
            is TalkBackSlider -> createSliderScript()
            is TalkBackChangeSetting -> createTalkBackChangeSettingScript()
            is PressKeyAdb -> createKeyPressScript()
            is InternalSetting<*> -> createInternalSettingScript()
            is Command<*> -> createCommandScript()
            is CombinationCommand -> createCombinedCommand()
        }

    private fun createAccessibilityServiceScript(): String {
        require(this is AccessibilityService)
        val completeName = "$packageName/$serviceName"
        val touchSetting = if (requiresTouch) {
            val value = if (on) "1" else "0"
            " settings put secure accessibility_enabled $value; settings put secure touch_exploration_enabled $value;"
        } else {
            ""
        }
        return if (on) {
            "CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" != *\"$completeName\"* ]]; then if [[ -z \"\$CURRENT\" || \"\$CURRENT\" == \"null\" ]]; then settings put secure enabled_accessibility_services $completeName; else settings put secure enabled_accessibility_services \$CURRENT:$completeName; fi;$touchSetting fi"
        } else {
            "CURRENT=\$(settings get secure enabled_accessibility_services); if [[ \"\$CURRENT\" == *\"$completeName\"* ]]; then SERVICE=$completeName; FINAL_RESULT=\"\${CURRENT//:\$SERVICE/}\"; FINAL_RESULT=\"\${FINAL_RESULT//SERVICE:/}\"; FINAL_RESULT=\"\${FINAL_RESULT//\$SERVICE/}\"; settings put secure enabled_accessibility_services \"\$FINAL_RESULT\";$touchSetting fi"
        }
    }

    private fun createScriptFromFile(): String {
        require(this is FileScript)
        val bufferedReader: BufferedReader = File(path).bufferedReader()
        var resultScript = bufferedReader.use { it.readText() }.trim()
        params.forEachIndexed { index, param ->
            resultScript = resultScript.replace("$${index + 1}", param)
        }

        return resultScript
    }

    private fun createTalkBackAdbScript(): String {
        require(this is TalkBackUserAction)
        return StringBuilder().apply {
            append(adbBroadcast)
            append(" ")
            append(adbTB4DBroadcastPackage)
            append(".")
            append(action.name.lowercase())

            if (action in listOf(TalkBackAction.NEXT, TalkBackAction.PREVIOUS)) {
                append(granularity.getGranularityCommandSuffix())
            }

        }.toString()
    }

    private fun createVolumeChangeScript(): String {
        require(this is TalkBackSetVolume)
        return StringBuilder().apply {
            append(adbBroadcast)
            append(" ")
            append(adbTB4DBroadcastPackage)
            append(".")
            append(level.name.lowercase())
        }.toString()
    }

    private fun createSliderScript(): String {
        require(this is TalkBackSlider)
        return StringBuilder().apply {
            append(adbBroadcast)
            append(" ")
            append(adbTB4DBroadcastPackage)
            append(".")
            append(TalkBackAction.SLIDER.name.lowercase())
            append(" ")
            append(adbBroadcastParam)
            append(" ")
            append(mode.name.lowercase())
        }.toString()
    }

    private fun createTalkBackChangeSettingScript(): String {
        require(this is TalkBackChangeSetting)

        return StringBuilder().apply {
            append(adbBroadcast)
            append(" ")
            append(adbTB4DBroadcastPackage)
            append(".")
            append(setting.name.lowercase())
            append(" -e value $enabled")
        }.toString()
    }

    private fun createKeyPressScript(): String {
        require(this is PressKeyAdb)
        return "input keyevent ${keyCode.androidValue}"
    }

    private fun createInternalSettingScript(): String {
        require(this is InternalSetting<*>)
        return "settings put ${internalSettingType.name.lowercase()} $name ${settingValue.toAdbValue()}"
    }

    private fun createCommandScript(): String {
        require(this is Command<*>)
        var resultScript = commandScript
        params.forEachIndexed { index, param ->
            resultScript = resultScript.replace("$${index + 1}", param.toAdbValue())
        }
        return resultScript
    }

    private fun createCombinedCommand(): String {
        require(this is CombinationCommand)
        var delimiter = ""
        return StringBuilder().apply {
            commandList.forEach { command ->
                append(delimiter)
                append(command.asScript())
                delimiter = "; "
            }
        }.toString()
    }

    private fun TalkBackGranularity.getGranularityCommandSuffix()
            : String {
        return StringBuilder("").apply {
            append(" ")
            append(adbBroadcastParam)
            append(" ")
            append(this@getGranularityCommandSuffix.talkBackValue)
        }.toString()
    }

    private val adbBroadcast = "am broadcast -a"
    private val adbBroadcastParam = "-e mode"
    private val adbTB4DBroadcastPackage = "com.a11y.adb"
}