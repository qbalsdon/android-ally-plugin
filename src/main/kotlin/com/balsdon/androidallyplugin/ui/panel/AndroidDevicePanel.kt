package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.model.ConnectedDeviceComboBoxPayload
import java.awt.event.ActionListener
import javax.swing.BoxLayout
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel

class AndroidDevicePanel(private val controller: Controller) {
    private val deviceListener = controller.deviceChangeNotifier
    private val comboBox: JComboBox<String> by lazy { JComboBox(arrayOf(localize("panel.device.no_devices"))) }
    private val comboBoxActionListener = ActionListener {
        if (comboBox.selectedItem != null) {
            val selection = if (comboBox.selectedIndex == 0) "" else comboBox.selectedItem as String
            controller.selectedDeviceSerial = selection
        }
    }

    fun create() = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JLabel(localize("panel.device.title")))
        add(comboBox.apply {
            addActionListener(comboBoxActionListener)
        })
        deviceListener
            .distinctUntilChanged()
            .subscribe {
                val payload = updateDeviceList(it)
                comboBox.removeActionListener(comboBoxActionListener)
                comboBox.removeAllItems()
                payload.labels.map { label -> comboBox.addItem(label) }
                comboBox.selectedIndex = payload.selectedDeviceIndex
                comboBox.addActionListener(comboBoxActionListener)
            }
    }

    fun updateDeviceList(deviceList: List<AndroidDevice>): ConnectedDeviceComboBoxPayload =
        if (deviceList.isEmpty()) {
            ConnectedDeviceComboBoxPayload()
        } else {
            val comboBoxLabels = deviceList.map { it.serial }
                .toMutableList()
                .apply { add(0, localize("panel.device.has_devices")) }
            val currentSelectionIndex = comboBoxLabels.indexOf(controller.selectedDeviceSerial)
            val selectionIndex = if (controller.selectedDeviceSerial.isBlank() || currentSelectionIndex == -1) {
                0
            } else {
                currentSelectionIndex
            }
            ConnectedDeviceComboBoxPayload(
                comboBoxLabels,
                selectionIndex
            )
        }
}