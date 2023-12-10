package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import java.awt.event.ActionListener
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel

class AndroidDevicePanel(controller: Controller) {
    private val deviceListener = controller.subscribeToDeviceChange()
    private val comboBox: JComboBox<String> by lazy { JComboBox(arrayOf("Nothing")) }
    private val comboBoxActionListener= ActionListener { println("Chosen: ${comboBox.selectedIndex}") }

    fun create() = JPanel().apply {
        add(JLabel(localize("panel.device.title")))
        add(comboBox.apply {
          addActionListener(comboBoxActionListener)
        })
        deviceListener
            .distinctUntilChanged()
            .subscribe {
            comboBox.removeAllItems()
            it.map { device -> comboBox.addItem(device.serial) }
        }
    }
}