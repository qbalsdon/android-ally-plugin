package com.balsdon.androidallyplugin.model

import AndroidDeviceTestFake
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AndroidDeviceFriendlyNameTest {

    @Test
    fun friendly_name_for_real_device() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        assertThat(testSubject.device.friendlyName).isEqualTo("Brand model")
    }

    @Test
    fun friendly_name_for_real_device_model_unknown() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "",
                "ro.product.brand" to "Brand",
            )
        )

        assertThat(testSubject.device.friendlyName).isEqualTo("Brand")
    }

    @Test
    fun friendly_name_for_real_device_brand_unknown() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "",
            )
        )

        assertThat(testSubject.device.friendlyName).isEqualTo("Model")
    }

    @Test
    fun unknown_name_for_real_device() {
        val testSubject = AndroidDeviceTestFake(
            deviceProps = mapOf(
                "ro.product.model" to "",
                "ro.product.brand" to "",
            )
        )

        assertThat(testSubject.device.friendlyName).isEqualTo("Unknown device")
    }

    /**
     * TODO: Fix this test!
     *       Because the [avdData] is an extension prop it's hard to figure this one out
    @Test
    fun friendly_name_for_emulator() {
    val testSubject = createDeviceFake(
    isEmulator = true,
    avdName = "my_custom_emulator"
    )

    assertThat(testSubject.friendlyName).isEqualTo("My custom emulator")
    }
     */

    @Test
    fun unknown_name_for_emulator() {
        val testSubject = AndroidDeviceTestFake(
            isEmulator = true,
            avdName = "my_custom_emulator"
        )

        assertThat(testSubject.device.friendlyName).isEqualTo("Unknown device")
    }
}