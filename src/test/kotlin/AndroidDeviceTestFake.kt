import com.android.ddmlib.Client
import com.android.ddmlib.FileListingService
import com.android.ddmlib.IDevice
import com.android.ddmlib.IShellOutputReceiver
import com.android.ddmlib.InstallReceiver
import com.android.ddmlib.RawImage
import com.android.ddmlib.ScreenRecorderOptions
import com.android.ddmlib.ServiceInfo
import com.android.ddmlib.SyncService
import com.android.ddmlib.log.LogReceiver
import com.android.sdklib.AndroidVersion
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.google.common.util.concurrent.ListenableFuture
import org.jetbrains.kotlin.backend.common.push
import java.io.File
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Creates a wrapper for an [AndroidDevice] with a faked [IDevice]
 *
 * To test functions on an [AndroidDevice], use
 * [AndroidDeviceTestFake].device
 */
class AndroidDeviceTestFake(
    serial: String = "",
    isEmulator: Boolean = false,
    isOnline: Boolean = true,
    avdName: String = "",
    deviceProps: Map<String, String> = emptyMap()
) {
    val executedCommands = mutableListOf<String>()
    val rawIDevice = object : IDevice {
        override fun getName(): String {
            TODO("Not yet implemented")
        }

        override fun executeShellCommand(command: String?, receiver: IShellOutputReceiver?) {
            if (command != null) {
                executedCommands.push(command)
            }
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

        override fun getSerialNumber(): String = serial


        override fun getState(): IDevice.DeviceState {
            TODO("Not yet implemented")
        }

        override fun getProperty(name: String?): String =
            if (name in deviceProps.keys) {
                deviceProps[name]!!
            } else {
                throw Exception("createDeviceFake(...).getProperty exception: [$name] not found")
            }

        override fun arePropertiesSet(): Boolean {
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

        // ----- Deprecated methods -----

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))
        override fun executeShellCommand(
            command: String?,
            receiver: IShellOutputReceiver?,
            maxTimeToOutputResponse: Int
        ) = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to executeShellCommand(command, receiver, maxTimeToOutputResponse)")

        @Deprecated("Deprecated in Java", ReplaceWith("avdName"))
        override fun getAvdName(): String = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getAvdName()")

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))
        override fun getAvdPath(): String = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getAvdPath()")

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))
        override fun getProperties(): MutableMap<String, String> = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getProperties()")

        @Deprecated("Deprecated in Java", ReplaceWith("deviceProps.size"))
        override fun getPropertyCount(): Int = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getPropertyCount()")

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))
        override fun getPropertySync(name: String?): String = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getPropertySync(name)")

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))
        override fun getPropertyCacheOrSync(name: String?): String = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getPropertyCacheOrSync(name)")

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))
        override fun getBatteryLevel(): Int = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getBatteryLevel()")

        @Deprecated("Deprecated in Java", ReplaceWith("Unknown"))

        override fun getBatteryLevel(freshnessMs: Long): Int = throw RuntimeException("AndroidDeviceTestFake: Deprecated call to getBatteryLevel(freshnessMs)")
    }
    val device = AndroidDevice(rawIDevice)
}