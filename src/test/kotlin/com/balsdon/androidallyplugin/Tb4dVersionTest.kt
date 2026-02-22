package com.balsdon.androidallyplugin

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class Tb4dVersionTest {

    @Test
    fun returns_true_when_installed_version_matches_expected() {
        assertThat(isTb4dVersionMatching(TB4DExpectedVersion)).isTrue()
        assertThat(isTb4dVersionMatching("TfPu_release_15_1-2024_11_17_1446-TB4D_0_0_4")).isTrue()
    }

    @Test
    fun returns_false_when_installed_version_does_not_match() {
        assertThat(isTb4dVersionMatching("TfPu_release_15_1-2024_11_17_1446-TB4D_0_0_2")).isFalse()
        assertThat(isTb4dVersionMatching("other_version")).isFalse()
        assertThat(isTb4dVersionMatching("")).isFalse()
        assertThat(isTb4dVersionMatching(null)).isFalse()
    }
}
