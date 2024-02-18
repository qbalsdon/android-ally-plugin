package com.balsdon.androidallyplugin.model

import AndroidDeviceTestFake
import com.balsdon.androidallyplugin.adb.darkMode
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AndroidExecuteScriptTest {
    @Test
    fun run_script() {
        // Given a device
        val testSubject = AndroidDeviceTestFake(
            isEmulator = false,
            avdName = "my_custom_device"
        )

        // when a script is executed
        testSubject.device.executeScript(
            darkMode(true).asScript()
        )

        // then the script is run on the device
        assertThat(testSubject.executedCommands.size).isEqualTo(1)
        assertThat(testSubject.executedCommands[0]).isEqualTo("cmd uimode night yes")
    }
}