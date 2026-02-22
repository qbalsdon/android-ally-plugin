package com.balsdon.androidallyplugin.controller

import AndroidDeviceTestFake
import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.google.common.truth.Truth.assertThat
import com.intellij.diagnostic.ActivityCategory
import com.intellij.openapi.extensions.ExtensionsArea
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.MessageBus
import org.jetbrains.kotlin.backend.common.push
import org.junit.Assert
import org.junit.Test


class AndroidStudioPluginControllerTest {
    @Test
    fun adds_new_devices() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )
        val receivedUpdates = mutableListOf<Set<AndroidDevice>>()
        testSubject.addConnectedDevicesListener(ConnectedDevicesListener { receivedUpdates.add(it) })

        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")

        // when devices are connected
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)

        // ensure that we have the correct device list (initial empty + 2 updates)
        assertThat(receivedUpdates.size).isEqualTo(3)
        assertThat(receivedUpdates[0].size).isEqualTo(0)
        assertThat(receivedUpdates[1].size).isEqualTo(1)
        assertThat(receivedUpdates[2].size).isEqualTo(2)

        receivedUpdates[1].forEach { value ->
            assertThat(value.serial).isEqualTo("1111")
        }

        receivedUpdates[2].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(if (index == 0) "1111" else "2222")
        }
    }

    @Test
    fun updates_device_when_same_is_added() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )
        val receivedUpdates = mutableListOf<Set<AndroidDevice>>()
        testSubject.addConnectedDevicesListener(ConnectedDevicesListener { receivedUpdates.add(it) })

        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")

        // when devices are connected
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)

        // ensure that we have the correct device list
        assertThat(receivedUpdates.size).isEqualTo(4)
        assertThat(receivedUpdates[1].size).isEqualTo(1)
        assertThat(receivedUpdates[2].size).isEqualTo(2)
        assertThat(receivedUpdates[3].size).isEqualTo(2)

        receivedUpdates[1].forEach { value ->
            assertThat(value.serial).isEqualTo("1111")
        }

        receivedUpdates[2].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(if (index == 0) "1111" else "2222")
        }

        receivedUpdates[3].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(if (index == 0) "1111" else "2222")
        }
    }

    @Test
    fun does_not_update_device_when_device_changed() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )
        val receivedUpdates = mutableListOf<Set<AndroidDevice>>()
        testSubject.addConnectedDevicesListener(ConnectedDevicesListener { receivedUpdates.add(it) })

        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")

        // when devices are connected and then device changed (no list update)
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        adbProviderFake.testDeviceChanged(testDevice2.rawIDevice)

        // ensure that we have the correct device list (no extra update for deviceChanged)
        assertThat(receivedUpdates.size).isEqualTo(3)
        assertThat(receivedUpdates[1].size).isEqualTo(1)
        assertThat(receivedUpdates[2].size).isEqualTo(2)

        receivedUpdates[1].forEach { value ->
            assertThat(value.serial).isEqualTo("1111")
        }

        receivedUpdates[2].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(if (index == 0) "1111" else "2222")
        }
    }

    @Test
    fun function_run_when_no_devices_selected_one_device_connected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        //    and no devices selected
        testSubject.selectedDeviceSerialList.clear()

        // when
        //    runOnAllValidSelectedDevices called
        var deviceCount = 0
        try {
            testSubject.runOnAllValidSelectedDevices { _ -> deviceCount += 1 }
        } catch (e: NullPointerException) {
            assertThat(e.stackTrace.count { element -> element.methodName == "showNoSelectedDevicesNotification" }).isEqualTo(
                1
            )
        }
        // then the resulting devices acted on is 0
        assertThat(deviceCount).isEqualTo(1)
    }

    @Test
    fun function_run_when_no_connected_devices_selected_other_device_connected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        //    and one devices selected
        testSubject.selectedDeviceSerialList.clear()
        testSubject.selectedDeviceSerialList.add("2222")
        //    and then the second disconnected
        adbProviderFake.testDeviceDisconnected(testDevice2.rawIDevice)
        // when
        //    runOnAllValidSelectedDevices called
        val executionCounter = mutableMapOf<String, Int>()

        try {
            testSubject.runOnAllValidSelectedDevices { device ->
                executionCounter[device.serial] = (executionCounter[device.serial] ?: 0) + 1
            }
        } catch (e: NullPointerException) {
            assertThat(e.stackTrace.count { element -> element.methodName == "showNoSelectedDevicesNotification" }).isEqualTo(
                1
            )
        }
        // then the resulting devices acted on is 0
        assertThat(executionCounter.size).isEqualTo(1)
        assertThat(executionCounter.containsKey("1111")).isTrue()
        assertThat(executionCounter.containsKey("2222")).isFalse()
        assertThat(executionCounter["1111"]).isEqualTo(1)
    }

    @Test
    fun function_not_run_when_no_devices_selected_two_device_connected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        //    and no devices selected
        testSubject.selectedDeviceSerialList.clear()

        // when
        //    runOnAllValidSelectedDevices called
        var deviceCount = 0
        try {
            testSubject.runOnAllValidSelectedDevices { _ -> deviceCount += 1 }
        } catch (e: NullPointerException) {
            assertThat(e.stackTrace.count { element -> element.methodName == "showNoSelectedDevicesNotification" }).isEqualTo(
                1
            )
        }
        // then the resulting devices acted on is 0
        assertThat(deviceCount).isEqualTo(0)
    }

    @Test
    fun function_not_run_when_no_devices_selected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        //    and no devices selected
        testSubject.selectedDeviceSerialList.clear()

        // when
        //    runOnAllValidSelectedDevices called
        var deviceCount = 0
        try {
            testSubject.runOnAllValidSelectedDevices { _ -> deviceCount += 1 }
        } catch (e: NullPointerException) {
            assertThat(e.stackTrace.count { element -> element.methodName == "showNoSelectedDevicesNotification" }).isEqualTo(
                1
            )
        }
        // then the resulting devices acted on is 0
        assertThat(deviceCount).isEqualTo(0)
    }

    @Test
    fun function_run_when_one_device_selected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        //    and no devices selected
        testSubject.selectedDeviceSerialList.add(testDevice1.rawIDevice.serialNumber)

        // when
        //    runOnAllValidSelectedDevices called
        val devicesActedUpon = mutableListOf<AndroidDevice>()
        testSubject.runOnAllValidSelectedDevices { device -> devicesActedUpon.push(device) }

        // then the resulting devices acted on is 1
        assertThat(devicesActedUpon.size).isEqualTo(1)
        assertThat(devicesActedUpon[0].serial).isEqualTo("1111")
    }

    @Test
    fun function_run_when_two_devices_selected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        val testDevice3 = AndroidDeviceTestFake(serial = "3333")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice3.rawIDevice)
        //    and no devices selected
        testSubject.selectedDeviceSerialList.add(testDevice1.rawIDevice.serialNumber)
        testSubject.selectedDeviceSerialList.add(testDevice3.rawIDevice.serialNumber)

        // when
        //    runOnAllValidSelectedDevices called
        val devicesActedUpon = mutableListOf<AndroidDevice>()
        testSubject.runOnAllValidSelectedDevices { device -> devicesActedUpon.push(device) }

        // then the resulting devices acted on is 2
        assertThat(devicesActedUpon.size).isEqualTo(2)
        assertThat(devicesActedUpon[0].serial).isEqualTo("1111")
        assertThat(devicesActedUpon[1].serial).isEqualTo("3333")
    }

    @Test
    fun function_run_when_two_devices_selected_only_one_connected() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )

        //    with connected devices
        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        val testDevice3 = AndroidDeviceTestFake(serial = "3333")
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice3.rawIDevice)
        adbProviderFake.testDeviceDisconnected(testDevice1.rawIDevice)
        //    and no devices selected
        testSubject.selectedDeviceSerialList.add(testDevice1.rawIDevice.serialNumber)
        testSubject.selectedDeviceSerialList.add(testDevice3.rawIDevice.serialNumber)

        // when
        //    runOnAllValidSelectedDevices called
        val devicesActedUpon = mutableListOf<AndroidDevice>()
        testSubject.runOnAllValidSelectedDevices { device -> devicesActedUpon.push(device) }

        // then the resulting devices acted on is 1
        assertThat(devicesActedUpon.size).isEqualTo(1)
        assertThat(devicesActedUpon[0].serial).isEqualTo("3333")
    }

    @Test
    fun removes_devices() {
        // given a controller
        val testSubject = AndroidStudioPluginController(
            projectFake,
            adbProviderFake
        )
        val receivedUpdates = mutableListOf<Set<AndroidDevice>>()
        testSubject.addConnectedDevicesListener(ConnectedDevicesListener { receivedUpdates.add(it) })

        val testDevice1 = AndroidDeviceTestFake(serial = "1111")
        val testDevice2 = AndroidDeviceTestFake(serial = "2222")
        val testDevice3 = AndroidDeviceTestFake(serial = "3333")

        // when devices are connected
        adbProviderFake.testDeviceConnected(testDevice1.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice2.rawIDevice)
        adbProviderFake.testDeviceConnected(testDevice3.rawIDevice)

        // when devices are removed
        adbProviderFake.testDeviceDisconnected(testDevice2.rawIDevice)

        // ensure that we have the correct device list
        assertThat(receivedUpdates.size).isEqualTo(5)
        assertThat(receivedUpdates[1].size).isEqualTo(1)
        assertThat(receivedUpdates[2].size).isEqualTo(2)
        assertThat(receivedUpdates[3].size).isEqualTo(3)
        assertThat(receivedUpdates[4].size).isEqualTo(2)

        receivedUpdates[1].forEach { value ->
            assertThat(value.serial).isEqualTo("1111")
        }

        receivedUpdates[2].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(if (index == 0) "1111" else "2222")
        }

        receivedUpdates[3].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(
                when (index) {
                    0 -> "1111"
                    1 -> "2222"
                    2 -> "3333"
                    else -> Assert.fail()
                }
            )
        }

        receivedUpdates[4].forEachIndexed { index, value ->
            assertThat(value.serial).isEqualTo(
                when (index) {
                    0 -> "1111"
                    1 -> "3333"
                    else -> Assert.fail()
                }
            )
        }
    }

    private val adbProviderFake = object : AdbProvider {
        private val listeners = mutableListOf<AndroidDebugBridge.IDeviceChangeListener>()
        override fun addDeviceChangeListener(listener: AndroidDebugBridge.IDeviceChangeListener) {
            listeners.add(listener)
        }

        override fun refreshAdb() = Unit

        fun testDeviceConnected(device: IDevice) {
            listeners.forEach {
                it.deviceConnected(device)
            }
        }

        fun testDeviceDisconnected(device: IDevice) {
            listeners.forEach {
                it.deviceDisconnected(device)
            }
        }

        fun testDeviceChanged(device: IDevice) {
            listeners.forEach {
                it.deviceChanged(device, 0)
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private val projectFake = object : Project {
        override fun <T : Any?> getUserData(p0: Key<T>): T? {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> putUserData(p0: Key<T>, p1: T?) {
            TODO("Not yet implemented")
        }

        override fun dispose() {
            TODO("Not yet implemented")
        }

        override fun getExtensionArea(): ExtensionsArea {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> getComponent(p0: Class<T>): T {
            TODO("Not yet implemented")
        }

        override fun hasComponent(p0: Class<*>): Boolean {
            TODO("Not yet implemented")
        }

        override fun isInjectionForExtensionSupported(): Boolean {
            TODO("Not yet implemented")
        }

        override fun getMessageBus(): MessageBus {
            TODO("Not yet implemented")
        }

        override fun isDisposed(): Boolean {
            TODO("Not yet implemented")
        }

        override fun getDisposed(): Condition<*> {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> getService(p0: Class<T>): T {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> instantiateClass(p0: Class<T>, p1: PluginId): T {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> instantiateClass(p0: String, p1: PluginDescriptor): T & Any {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> instantiateClassWithConstructorInjection(p0: Class<T>, p1: Any, p2: PluginId): T {
            TODO("Not yet implemented")
        }

        override fun createError(p0: Throwable, p1: PluginId): RuntimeException {
            TODO("Not yet implemented")
        }

        override fun createError(p0: String, p1: PluginId): RuntimeException {
            TODO("Not yet implemented")
        }

        override fun createError(
            p0: String,
            p1: Throwable?,
            p2: PluginId,
            p3: MutableMap<String, String>?
        ): RuntimeException {
            TODO("Not yet implemented")
        }

        override fun <T : Any?> loadClass(p0: String, p1: PluginDescriptor): Class<T> {
            TODO("Not yet implemented")
        }

        override fun getActivityCategory(p0: Boolean): ActivityCategory {
            TODO("Not yet implemented")
        }

        override fun getName(): String {
            TODO("Not yet implemented")
        }

        @Deprecated("Deprecated in Java")
        override fun getBaseDir(): VirtualFile {
            TODO("Not yet implemented")
        }

        override fun getBasePath(): String? {
            TODO("Not yet implemented")
        }

        override fun getProjectFile(): VirtualFile? {
            TODO("Not yet implemented")
        }

        override fun getProjectFilePath(): String? {
            TODO("Not yet implemented")
        }

        override fun getWorkspaceFile(): VirtualFile? {
            TODO("Not yet implemented")
        }

        override fun getLocationHash(): String {
            TODO("Not yet implemented")
        }

        override fun save() {
            TODO("Not yet implemented")
        }

        override fun isOpen(): Boolean {
            TODO("Not yet implemented")
        }

        override fun isInitialized(): Boolean {
            TODO("Not yet implemented")
        }
    }
}