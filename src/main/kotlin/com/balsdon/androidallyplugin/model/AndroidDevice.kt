package com.balsdon.androidallyplugin.model

import android.databinding.tool.ext.capitalizeUS
import com.android.ddmlib.IDevice
import com.android.ddmlib.IShellOutputReceiver
import com.android.ddmlib.InstallException
import com.android.ddmlib.InstallReceiver
import com.android.ddmlib.NullOutputReceiver
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class AndroidDevice(private val rawDevice: IDevice) {

    val isEmulator: Boolean by lazy { rawDevice.isEmulator }
    val serial: String by lazy { rawDevice.serialNumber }
    private val apiLevel: String get() = rawDevice.getProperty(IDevice.PROP_BUILD_API_LEVEL) ?: ""
    private val sdkLevel: String get() = rawDevice.getProperty(IDevice.PROP_BUILD_VERSION) ?: ""
    val friendlyName: String
        get() {
            val unknownDeviceLabel = localize("panel.device.label.device_unknown")
            return try {
                if (isEmulator) {
                    val avdName = rawDevice.avdData.get().name
                    if (avdName.isNullOrEmpty()) {
                        unknownDeviceLabel
                    } else {
                        avdName.replace("_", " ")
                    }
                } else {
                    val brand = rawDevice.getProperty("ro.product.brand") ?: ""
                    val model = rawDevice.getProperty(IDevice.PROP_DEVICE_MODEL) ?: ""
                    val name = "${brand.lowercase()} $model".trim().capitalizeUS()
                    name.ifBlank {
                        unknownDeviceLabel
                    }
                }
            } catch (e: Exception) {
                log(e)
                unknownDeviceLabel
            }
        }

    private fun getPackageList(fn: (List<String>) -> Unit) {
        val builder = StringBuilder()
        try {
            rawDevice.executeShellCommand("pm list packages", object : IShellOutputReceiver {
                override fun addOutput(data: ByteArray, offset: Int, length: Int) {
                    builder.append(String(data, offset, length, StandardCharsets.UTF_8))
                }

                override fun flush() {
                    val list = builder.split("\n")
                    fn(list)
                }

                override fun isCancelled(): Boolean = false
            })
        } catch (e: com.android.ddmlib.AdbCommandRejectedException) {
            log(e)
            fn(emptyList())
        }
    }

    /**
     * Opportunity for improvement here!!
     *
     * I need help making this efficient and less overwhelming for the system.
     *
     * There is no isReady function I could call or wait for.
     */
    fun requestData(): Observable<BasicDeviceInfo> {
        val subject = BehaviorSubject.create<BasicDeviceInfo>()
        val unknownDeviceLabel = localize("panel.device.label.device_unknown")
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var name = friendlyName
            var api = apiLevel
            var sdk = sdkLevel
            while (name == unknownDeviceLabel) {
                delay(100)
                name = friendlyName
            }
            while (api.isBlank()) {
                delay(100)
                api = apiLevel
            }
            while (sdk.isBlank()) {
                delay(100)
                sdk = sdkLevel
            }

            getPackageList { list -> subject.onNext(BasicDeviceInfo(name, api, sdk, list)) }
        }
        return subject
    }

    fun installTalkBackForDevelopers() =
        installAPK()

    fun executeScript(scriptString: String) {
        rawDevice.executeShellCommand(
            scriptString,
            NullOutputReceiver()
        )
    }

    /**
     * Installs an APK by copying it out of resources and installing a temporary file
     */
    private fun installAPK(fileName: String = "talkback-phone-release-signed-66.apk"): Observable<Boolean> {
        val subject = BehaviorSubject.create<Boolean>()
        val path = "/files/$fileName"

        val inputStream: InputStream = AndroidDevice::class.java.getResourceAsStream(path)
            ?: throw FileNotFoundException("installAPK: [$path] not found")
        val temporaryFile: File = File.createTempFile("base", ".apk")
        Files.copy(inputStream, temporaryFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        try {
            rawDevice.installPackage(temporaryFile.absolutePath, true, object : InstallReceiver() {
                override fun done() {
                    log("Install was successful: $isSuccessfullyCompleted")
                    temporaryFile.delete()
                    subject.onNext(isSuccessfullyCompleted)
                    super.done()
                }
            })
        } catch (installException: InstallException) {
            installException.printStackTrace()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return subject
    }
}