package com.balsdon.androidallyplugin.model

import com.balsdon.androidallyplugin.localize

data class ConnectedDeviceComboBoxPayload(
    val labels: List<String> = listOf(localize("panel.device.no_devices")),
    val selectedDeviceIndex: Int = 0
)