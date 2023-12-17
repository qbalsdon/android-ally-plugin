package com.balsdon.androidallyplugin.extension

import android.databinding.tool.ext.capitalizeUS
import com.android.ddmlib.IDevice
import com.android.ddmlib.IDevice.PROP_DEVICE_MODEL
import com.android.ddmlib.IShellOutputReceiver
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AccessibilityService
import com.balsdon.androidallyplugin.model.filterFromPackages
import com.balsdon.androidallyplugin.utils.log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject


/**
 * Extension methods for [com.android.ddmlib.IDevice]
 *
 * Everything here must be tested!
 */

val IDevice.friendlyName: String
    get() {
        val unknownDeviceLabel = localize("panel.device.label.device_unknown")
        return try {
            if (isEmulator) {
                val avdName = avdData.get().name
                if (avdName.isNullOrEmpty()) {
                    unknownDeviceLabel
                } else {
                    avdName.replace("_", " ")
                }
            } else {
                val brand = getProperty("ro.product.brand") ?: ""
                val model = getProperty(PROP_DEVICE_MODEL) ?: ""
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

/**
 * [filterFromPackages] is tested, rather than testing an RX Wrapper
 *
 * I am not great at RXJava, I need help cleaning this up
 */
fun IDevice.fetchAccessibilityServices(): Observable<List<AccessibilityService>> {
    val subject = BehaviorSubject.create<List<AccessibilityService>>()

    executeShellCommand("pm list packages", object : IShellOutputReceiver {
        override fun addOutput(data: ByteArray, offset: Int, length: Int) {
            val list = String(data, Charsets.UTF_8).split("\n")
            subject.onNext(filterFromPackages(list)) // Emits the value
        }

        override fun flush() = Unit
        override fun isCancelled(): Boolean = false
    })

    subject.doOnSubscribe { disposable ->
        disposable.dispose()
    }
    return subject
}