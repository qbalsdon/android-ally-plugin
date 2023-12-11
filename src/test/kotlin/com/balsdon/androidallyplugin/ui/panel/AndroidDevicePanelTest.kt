package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.controller.NotificationPayload
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class AndroidDevicePanelTest {

    private fun createControllerFake(selectedDeviceSerial: String = ""): Controller = object : Controller {
        override fun <NPT : NotificationPayload> showNotification(notificationPayload: NPT) = Unit
        override val deviceChangeNotifier: Observable<List<AndroidDevice>> = Observable.empty()
        override var selectedDeviceSerial: String = selectedDeviceSerial
    }

    @Test
    fun zero_devices_returns_only_empty_result() {
        // given: testSubject + controllerFake
        val testSubject = AndroidDevicePanel(createControllerFake())
        // when
        val result = testSubject.updateDeviceList(emptyList())
        // then
        assertThat(result.selectedDeviceIndex).isEqualTo(0)
        assertThat(result.labels.size).isEqualTo(1)
        assertThat(result.labels[0]).isEqualTo("No devices")
    }

    @Test
    fun more_than_one_device_returns_many_labels() {
        // given: testSubject + controllerFake
        val testSubject = AndroidDevicePanel(createControllerFake())
        // when
        val result = testSubject.updateDeviceList(listOf(AndroidDevice("1111"), AndroidDevice("2222")))
        // then
        assertThat(result.selectedDeviceIndex).isEqualTo(0)
        assertThat(result.labels.size).isEqualTo(3)
        assertThat(result.labels[0]).isEqualTo("Select a device")
        assertThat(result.labels[1]).isEqualTo("1111")
        assertThat(result.labels[2]).isEqualTo("2222")
    }

    @Test
    fun more_than_one_device_maintain_selection_index() {
        // given: testSubject + controllerFake
        val controllerFake = createControllerFake(selectedDeviceSerial = "2222")
        val testSubject = AndroidDevicePanel(controllerFake)
        // when
        val result = testSubject.updateDeviceList(listOf(AndroidDevice("1111"), AndroidDevice("2222"), AndroidDevice("3333")))
        // then
        assertThat(controllerFake.selectedDeviceSerial).isEqualTo("2222")
        assertThat(result.selectedDeviceIndex).isEqualTo(2)
        assertThat(result.labels.size).isEqualTo(4)
        assertThat(result.labels[0]).isEqualTo("Select a device")
        assertThat(result.labels[1]).isEqualTo("1111")
        assertThat(result.labels[2]).isEqualTo("2222")
        assertThat(result.labels[3]).isEqualTo("3333")
    }

    @Test
    fun more_than_one_device_reset_selection_index_for_lost_device() {
        // given: testSubject + controllerFake
        val controllerFake = createControllerFake(selectedDeviceSerial = "2222")
        val testSubject = AndroidDevicePanel(controllerFake)
        // when
        val result = testSubject.updateDeviceList(listOf(AndroidDevice("1111"), AndroidDevice("3333")))
        // then
        assertThat(controllerFake.selectedDeviceSerial.isBlank()).isFalse()
        assertThat(result.selectedDeviceIndex).isEqualTo(0)
        assertThat(result.labels[0]).isEqualTo("Select a device")
        assertThat(result.labels[1]).isEqualTo("1111")
        assertThat(result.labels[2]).isEqualTo("3333")
    }

    @Test
    fun update_with_preferred_means_that_preferred_is_selected() {
        // given: testSubject + controllerFake
        val controllerFake = createControllerFake(selectedDeviceSerial = "2222")
        val testSubject = AndroidDevicePanel(controllerFake)
        // when
        val result = testSubject.updateDeviceList(listOf(AndroidDevice("1111"), AndroidDevice("2222")))
        // then
        assertThat(controllerFake.selectedDeviceSerial.isBlank()).isFalse()
        assertThat(result.selectedDeviceIndex).isEqualTo(2)
        assertThat(result.labels[0]).isEqualTo("Select a device")
        assertThat(result.labels[1]).isEqualTo("1111")
        assertThat(result.labels[2]).isEqualTo("2222")
    }
}