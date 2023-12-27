package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.ui.CustomIcon
import com.balsdon.androidallyplugin.utils.log
import com.intellij.ui.util.maximumHeight
import com.intellij.util.ui.JBUI
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.CompoundBorder

class AndroidDevicePanel(private val controller: Controller) {
    private val deviceListener = controller.connectedDevicesNotifier
    private val devicePanel: JPanel by lazy { JPanel().apply { layout = BoxLayout(this, BoxLayout.Y_AXIS) } }

    private fun createDeviceSelectionCheckBox(device: AndroidDevice.Device) = JPanel().apply {
        layout = FlowLayout(FlowLayout.LEADING)
        maximumHeight = 40 // TODO: Fix this
        val updateState = { isSelected: Boolean ->
            if (isSelected) {
                controller.selectedDeviceSerialList.add(device.serial)
            } else {
                controller.selectedDeviceSerialList.remove(device.serial)
            }
            log("Selected devices: ${controller.selectedDeviceSerialList}")
        }
        val checkBox = JCheckBox().apply {
            isSelected = device.serial in controller.selectedDeviceSerialList
            addChangeListener {
                updateState(this.isSelected)
            }
        }
        val toggleSelection = {
            checkBox.isSelected = !checkBox.isSelected
        }

        addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit
            override fun keyPressed(e: KeyEvent?) = Unit
            override fun keyReleased(e: KeyEvent?) {
                e?.also { event ->
                    if (event.keyCode == KeyEvent.VK_SPACE) {
                        toggleSelection()
                    }
                }
            }
        })
        addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                e?.also {
                    toggleSelection()
                }
            }

            override fun mousePressed(e: MouseEvent?) = Unit
            override fun mouseReleased(e: MouseEvent?) = Unit
            override fun mouseEntered(e: MouseEvent?) = Unit
            override fun mouseExited(e: MouseEvent?) = Unit
        })
        add(checkBox)
        add(JLabel(
            if (device.isEmulator) {
                CustomIcon.EMULATOR.create()
            } else {
                CustomIcon.PHONE.create()
            }
        ).apply {
            border = CompoundBorder(border, JBUI.Borders.empty(0, 10))
        })
        add(JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(
                JLabel(device.friendlyName)
            )
            add(JLabel("${device.serial} - [${device.apiLevel} / ${device.sdkLevel}]").apply {
                val oldFont = font
                font = Font(oldFont.fontName, Font.ITALIC, oldFont.size - 5)
//                foreground = ColourReference.FONT.createColour()
            })
        })
//        device.rawDevice.fetchAccessibilityServices().subscribe { services ->
//            val index = services.indexOfFirst { it.friendlyNameReference == "android.accessibility.service.name.talkback-for-developers" }
//            if (index > -1) {
//                add(JButton("Install TalkBack4Devs").apply {
//                    addActionListener {
//                        log("Install!")
//                    }
//                })
//            }
//        }
        name = device.friendlyName
    }

    fun create() = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JLabel(localize("panel.device.title")))
        add(devicePanel)
        deviceListener
            .distinctUntilChanged()
            .subscribe {
                devicePanel.removeAll()
                if (it.isEmpty()) {
                    devicePanel.add(JLabel(localize("panel.device.no_devices")))
                } else {
                    it.forEach { device ->
                        devicePanel.add(createDeviceSelectionCheckBox(device as AndroidDevice.Device))
                    }
                }
            }
    }
}