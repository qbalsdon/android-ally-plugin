package com.balsdon.androidallyplugin

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class I18nSupportTest {
    @Test
    fun localises_correctly() {
        assertThat(localize("tb4d.install.success.title")).isEqualTo("Success")
    }

    @Test
    fun localises_with_arguments() {
        assertThat(localize("tb4d.install.success.message", "SERIAL_NO")).isEqualTo("TalkBack For Developers Installed on SERIAL_NO")
        assertThat(localize("tb4d.install.success.message", "SERIAL_NO_TEST_2")).isEqualTo("TalkBack For Developers Installed on SERIAL_NO_TEST_2")
    }
}