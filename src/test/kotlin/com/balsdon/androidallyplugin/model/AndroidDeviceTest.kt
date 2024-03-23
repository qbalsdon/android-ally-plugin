package com.balsdon.androidallyplugin.model

import AndroidDeviceTestFake
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AndroidDeviceTest {
    @Test
    fun can_determine_if_device_is_not_watch() {
        listOf(
            AndroidDeviceTestFake(
                deviceProps = mapOf(
                    "ro.product.model" to "model",
                    "ro.product.brand" to "brand",
                )
            ),
            AndroidDeviceTestFake(
                deviceProps = mapOf(
                    "ro.product.model" to "model",
                    "ro.product.brand" to "brand",
                    "ro.build.characteristics" to ""
                )
            ),
            AndroidDeviceTestFake(
                deviceProps = mapOf(
                    "ro.product.model" to "model",
                    "ro.product.brand" to "brand",
                    "ro.build.characteristics" to "nosdcard"
                )
            ),
            AndroidDeviceTestFake(
                deviceProps = mapOf(
                    "ro.product.model" to "model",
                    "ro.product.brand" to "brand",
                    "ro.build.characteristics" to "default"
                )
            )
        ).forEach { testSubject ->
            assertThat(testSubject.device.isWatch).isFalse()
        }
    }

    @Test
    fun can_determine_if_device_is_watch() {
        listOf(
            AndroidDeviceTestFake(
                deviceProps = mapOf(
                    "ro.product.model" to "model",
                    "ro.product.brand" to "brand",
                    "ro.build.characteristics" to "watch"
                )
            ),
            AndroidDeviceTestFake(
                deviceProps = mapOf(
                    "ro.product.model" to "model",
                    "ro.product.brand" to "brand",
                    "ro.build.characteristics" to "nosdcard,watch"
                )
            )
        ).forEach { testSubject ->
            assertThat(testSubject.device.isWatch).isTrue()
        }
    }
}