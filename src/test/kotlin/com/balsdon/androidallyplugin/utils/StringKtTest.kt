package com.balsdon.androidallyplugin.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringKtTest{
    @Test
    fun checks_string_is_number() {
        (0..1000).forEach { number ->
            assertThat(number.toString().isNumeric()).isTrue()
        }
    }

    @Test
    fun checks_string_is_number_negative() {
        (0..1000).forEach { number ->
            assertThat((number*-1).toString().isNumeric()).isTrue()
        }
    }

    @Test
    fun checks_string_is_number_fails() {
        assertThat("1f0".isNumeric()).isFalse()
        assertThat(" 10".isNumeric()).isFalse()
        assertThat("10-".isNumeric()).isFalse()
        assertThat("1 0".isNumeric()).isFalse()
        assertThat("10 ".isNumeric()).isFalse()
        assertThat("".isNumeric()).isFalse()
        assertThat(" ".isNumeric()).isFalse()
        assertThat("words".isNumeric()).isFalse()
        assertThat("null".isNumeric()).isFalse()
    }

    @Test
    fun checks_string_is_number_can_have_symbols() {
        assertThat("-1".isNumeric()).isTrue()
        assertThat("-9912".isNumeric()).isTrue()
        assertThat("+4627".isNumeric()).isTrue()
    }
}