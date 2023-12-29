package com.balsdon.androidallyplugin.model

import com.android.ddmlib.*
import com.android.ddmlib.log.LogReceiver
import com.android.sdklib.AndroidVersion
import com.google.common.truth.Truth.assertThat
import com.google.common.util.concurrent.ListenableFuture
import org.junit.Test
import java.io.File
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class AndroidDeviceFriendlyNameTest {

    @Test
    fun friendly_name_for_real_device() {
        val testSubject = createDeviceFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "brand",
            )
        )

        assertThat(testSubject.friendlyName).isEqualTo("Brand model")
    }

    @Test
    fun friendly_name_for_real_device_model_unknown() {
        val testSubject = createDeviceFake(
            deviceProps = mapOf(
                "ro.product.model" to "",
                "ro.product.brand" to "Brand",
            )
        )

        assertThat(testSubject.friendlyName).isEqualTo("Brand")
    }

    @Test
    fun friendly_name_for_real_device_brand_unknown() {
        val testSubject = createDeviceFake(
            deviceProps = mapOf(
                "ro.product.model" to "model",
                "ro.product.brand" to "",
            )
        )

        assertThat(testSubject.friendlyName).isEqualTo("Model")
    }

    @Test
    fun unknown_name_for_real_device() {
        val testSubject = createDeviceFake(
            deviceProps = mapOf(
                "ro.product.model" to "",
                "ro.product.brand" to "",
            )
        )

        assertThat(testSubject.friendlyName).isEqualTo("Unknown device")
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
        val testSubject = createDeviceFake(
            isEmulator = true,
            avdName = "my_custom_emulator"
        )

        assertThat(testSubject.friendlyName).isEqualTo("Unknown device")
    }

    private fun createDeviceFake(
        isEmulator: Boolean = false,
        isOnline: Boolean = true,
        avdName: String = "",
        deviceProps: Map<String, String> = emptyMap()
    ) = AndroidDevice(
        object : IDevice {
            override fun getName(): String {
                TODO("Not yet implemented")
            }

            override fun executeShellCommand(
                command: String?,
                receiver: IShellOutputReceiver?,
                maxTimeToOutputResponse: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun executeShellCommand(command: String?, receiver: IShellOutputReceiver?) {
                TODO("Not yet implemented")
            }

            override fun executeShellCommand(
                command: String?,
                receiver: IShellOutputReceiver?,
                maxTimeToOutputResponse: Long,
                maxTimeUnits: TimeUnit?
            ) {
                TODO("Not yet implemented")
            }

            override fun executeShellCommand(
                command: String?,
                receiver: IShellOutputReceiver?,
                maxTimeout: Long,
                maxTimeToOutputResponse: Long,
                maxTimeUnits: TimeUnit?
            ) {
                TODO("Not yet implemented")
            }

            override fun getSystemProperty(name: String?): ListenableFuture<String> {
                TODO("Not yet implemented")
            }

            override fun getSerialNumber(): String {
                TODO("Not yet implemented")
            }

            @Deprecated("Deprecated in Java", ReplaceWith("avdName"))
            override fun getAvdName(): String = throw Exception("Deprecated method use")

            @Deprecated("Deprecated in Java")
            override fun getAvdPath(): String {
                TODO("Not yet implemented")
            }

            override fun getState(): IDevice.DeviceState {
                TODO("Not yet implemented")
            }

            @Deprecated("Deprecated in Java")
            override fun getProperties(): MutableMap<String, String> {
                TODO("Not yet implemented")
            }


            @Deprecated("Deprecated in Java", ReplaceWith("deviceProps.size"))
            override fun getPropertyCount(): Int = deviceProps.size

            override fun getProperty(name: String?): String =
                if (name in deviceProps.keys) {
                    deviceProps[name]!!
                } else {
                    throw Exception("createDeviceFake(...).getProperty exception: [$name] not found")
                }

            override fun arePropertiesSet(): Boolean {
                TODO("Not yet implemented")
            }

            @Deprecated("Deprecated in Java")
            override fun getPropertySync(name: String?): String {
                TODO("Not yet implemented")
            }

            @Deprecated("Deprecated in Java")
            override fun getPropertyCacheOrSync(name: String?): String {
                TODO("Not yet implemented")
            }

            override fun supportsFeature(feature: IDevice.Feature?): Boolean {
                TODO("Not yet implemented")
            }

            override fun supportsFeature(feature: IDevice.HardwareFeature?): Boolean {
                TODO("Not yet implemented")
            }

            override fun services(): MutableMap<String, ServiceInfo> {
                TODO("Not yet implemented")
            }

            override fun getMountPoint(name: String?): String {
                TODO("Not yet implemented")
            }

            override fun isOnline(): Boolean = isOnline

            override fun isEmulator(): Boolean = isEmulator

            override fun isOffline(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isBootLoader(): Boolean {
                TODO("Not yet implemented")
            }

            override fun hasClients(): Boolean {
                TODO("Not yet implemented")
            }

            override fun getClients(): Array<Client> {
                TODO("Not yet implemented")
            }

            override fun getClient(applicationName: String?): Client {
                TODO("Not yet implemented")
            }

            override fun getSyncService(): SyncService {
                TODO("Not yet implemented")
            }

            override fun getFileListingService(): FileListingService {
                TODO("Not yet implemented")
            }

            override fun getScreenshot(): RawImage {
                TODO("Not yet implemented")
            }

            override fun getScreenshot(timeout: Long, unit: TimeUnit?): RawImage {
                TODO("Not yet implemented")
            }

            override fun startScreenRecorder(
                remoteFilePath: String?,
                options: ScreenRecorderOptions?,
                receiver: IShellOutputReceiver?
            ) {
                TODO("Not yet implemented")
            }

            override fun runEventLogService(receiver: LogReceiver?) {
                TODO("Not yet implemented")
            }

            override fun runLogService(logname: String?, receiver: LogReceiver?) {
                TODO("Not yet implemented")
            }

            override fun createForward(localPort: Int, remotePort: Int) {
                TODO("Not yet implemented")
            }

            override fun createForward(
                localPort: Int,
                remoteSocketName: String?,
                namespace: IDevice.DeviceUnixSocketNamespace?
            ) {
                TODO("Not yet implemented")
            }

            override fun getClientName(pid: Int): String {
                TODO("Not yet implemented")
            }

            override fun pushFile(local: String?, remote: String?) {
                TODO("Not yet implemented")
            }

            override fun pullFile(remote: String?, local: String?) {
                TODO("Not yet implemented")
            }

            override fun installPackage(packageFilePath: String?, reinstall: Boolean, vararg extraArgs: String?) {
                TODO("Not yet implemented")
            }

            override fun installPackage(
                packageFilePath: String?,
                reinstall: Boolean,
                receiver: InstallReceiver?,
                vararg extraArgs: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun installPackage(
                packageFilePath: String?,
                reinstall: Boolean,
                receiver: InstallReceiver?,
                maxTimeout: Long,
                maxTimeToOutputResponse: Long,
                maxTimeUnits: TimeUnit?,
                vararg extraArgs: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun installPackages(
                apks: MutableList<File>?,
                reinstall: Boolean,
                installOptions: MutableList<String>?,
                timeout: Long,
                timeoutUnit: TimeUnit?
            ) {
                TODO("Not yet implemented")
            }

            override fun syncPackageToDevice(localFilePath: String?): String {
                TODO("Not yet implemented")
            }

            override fun installRemotePackage(remoteFilePath: String?, reinstall: Boolean, vararg extraArgs: String?) {
                TODO("Not yet implemented")
            }

            override fun installRemotePackage(
                remoteFilePath: String?,
                reinstall: Boolean,
                receiver: InstallReceiver?,
                vararg extraArgs: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun installRemotePackage(
                remoteFilePath: String?,
                reinstall: Boolean,
                receiver: InstallReceiver?,
                maxTimeout: Long,
                maxTimeToOutputResponse: Long,
                maxTimeUnits: TimeUnit?,
                vararg extraArgs: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun removeRemotePackage(remoteFilePath: String?) {
                TODO("Not yet implemented")
            }

            override fun uninstallPackage(packageName: String?): String {
                TODO("Not yet implemented")
            }

            override fun uninstallApp(applicationID: String?, vararg extraArgs: String?): String {
                TODO("Not yet implemented")
            }

            override fun reboot(into: String?) {
                TODO("Not yet implemented")
            }

            override fun root(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isRoot(): Boolean {
                TODO("Not yet implemented")
            }

            @Deprecated("Deprecated in Java")
            override fun getBatteryLevel(): Int {
                TODO("Not yet implemented")
            }

            @Deprecated("Deprecated in Java")
            override fun getBatteryLevel(freshnessMs: Long): Int {
                TODO("Not yet implemented")
            }

            override fun getBattery(): Future<Int> {
                TODO("Not yet implemented")
            }

            override fun getBattery(freshnessTime: Long, timeUnit: TimeUnit?): Future<Int> {
                TODO("Not yet implemented")
            }

            override fun getAbis(): MutableList<String> {
                TODO("Not yet implemented")
            }

            override fun getDensity(): Int {
                TODO("Not yet implemented")
            }

            override fun getLanguage(): String {
                TODO("Not yet implemented")
            }

            override fun getRegion(): String {
                TODO("Not yet implemented")
            }

            override fun getVersion(): AndroidVersion {
                TODO("Not yet implemented")
            }


        })
}