package com.balsdon.androidallyplugin.model

import android.databinding.tool.ext.capitalizeUS
import com.android.ddmlib.IDevice
import com.android.ddmlib.IShellOutputReceiver
import com.android.ddmlib.InstallException
import com.android.ddmlib.InstallReceiver
import com.android.ddmlib.NullOutputReceiver
import com.balsdon.androidallyplugin.LatestPhoneApkFileName
import com.balsdon.androidallyplugin.LatestWearApkFileName
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.log
import com.balsdon.androidallyplugin.utils.onException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.util.classNameAndMessage
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class AndroidDevice(private val rawDevice: IDevice) {

    val isEmulator: Boolean by lazy { rawDevice.isEmulator }
    val serial: String by lazy { rawDevice.serialNumber }
    val isWatch: Boolean by lazy {
        rawDevice.getProperty(IDevice.PROP_BUILD_CHARACTERISTICS)?.contains("watch") ?: false
    }
    private val apiLevel: String get() = rawDevice.getProperty(IDevice.PROP_BUILD_API_LEVEL) ?: ""
    private val sdkLevel: String get() = rawDevice.getProperty(IDevice.PROP_BUILD_VERSION) ?: ""

    @Suppress("TooGenericExceptionCaught")
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
                    val brand = (rawDevice.getProperty("ro.product.brand") ?: "").lowercase()
                    val model = (rawDevice.getProperty(IDevice.PROP_DEVICE_MODEL) ?: "").lowercase()
                    val name = if (model.startsWith(brand)) {
                        model
                    } else {
                        "$brand $model"
                    }.trim()
                        .split(" ")
                        .joinToString(" ") { t -> t.capitalizeUS() }
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
        runCatching {
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
        }.onException(
            com.android.ddmlib.AdbCommandRejectedException::class,
            java.net.SocketException::class
        ) {
            log(it.classNameAndMessage)
            fn(emptyList())
        }
    }

    /**
     * Opportunity for improvement here!!
     *
     * This method runs async between the device being detected and reported back to the UI
     * It's goal is to run all the commands on the ADB to gather info before reporting back
     * Friendly name, device sdk and api levels all run different commands and the
     * packageList can be a bit heavy.
     *
     * I need help making this efficient and less overwhelming for the system.
     *
     * There is no isReady function I could call or wait for.
     */
    @Suppress("MagicNumber")
    fun requestData(): Observable<BasicDeviceInfo> {
        val subject = BehaviorSubject.create<BasicDeviceInfo>()
        val unknownDeviceLabel = localize("panel.device.label.device_unknown")
        val scope = CoroutineScope(Job() + Dispatchers.IO)
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

            getPackageList { list -> subject.onNext(BasicDeviceInfo(name, api, sdk, isWatch, list)) }
        }
        return subject
    }

    fun installTalkBackForDevelopers() =
        installAPK(if (isWatch) LatestWearApkFileName else LatestPhoneApkFileName)

    fun executeScript(scriptString: String) {
        rawDevice.executeShellCommand(
            scriptString,
            NullOutputReceiver()
        )
    }

    /**
     * Installs an APK by copying it out of resources and installing a temporary file
     */
    @Suppress("TooGenericExceptionCaught")
    private fun installAPK(fileName: String): Observable<Boolean> {
        val subject = BehaviorSubject.create<Boolean>()
        log("Installing [$fileName] on [$friendlyName]")
        CoroutineScope(Job() + Dispatchers.IO).launch {
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
                subject.onNext(false)
                log(installException)
            } catch (exception: Exception) {
                subject.onNext(false)
                log(exception)
            }
        }
        return subject
    }
}