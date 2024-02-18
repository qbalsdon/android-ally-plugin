package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.TB4DWebPage
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.ui.CustomIcon
import com.balsdon.androidallyplugin.utils.log
import com.intellij.ide.BrowserUtil
import com.intellij.ui.util.maximumHeight
import com.intellij.util.ui.JBUI
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.CompoundBorder
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class AndroidDevicePanel(private val controller: Controller) {
    private val deviceListener = controller.connectedDevicesNotifier
    private val deviceListPanel: JPanel by lazy { JPanel().apply { layout = BoxLayout(this, BoxLayout.Y_AXIS) } }
    private val tb4dPanel: JPanel by lazy {
        JPanel().apply { layout = FlowLayout(FlowLayout.TRAILING) }
    }

    private fun createDeviceSelectionCheckBox(device: AndroidDevice) = JPanel().apply {
        layout = BorderLayout()
        maximumHeight = elementMaxHeight
        add(JPanel().apply {
            layout = FlowLayout(FlowLayout.LEADING)
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
                    font = Font(oldFont.fontName, Font.ITALIC, oldFont.size - 3)
                })
            })
        }, BorderLayout.WEST)

        updateDeviceBasedOnInstalledServices(device, this)
        name = device.friendlyName
    }

    private fun updateDeviceBasedOnInstalledServices(device: AndroidDevice, parentPanel: JPanel, onRecur: Boolean = false) {
        device.fetchAccessibilityServices().take(1).subscribe { services ->
            val index = services.indexOfFirst { it.packageName == "com.android.talkback4d" }
            tb4dPanel.removeAll()
            parentPanel.remove(tb4dPanel)
            if (index == -1) {
                parentPanel.add(tb4dPanel.apply {
                    add(JButton(localize("panel.device.label.install.tb4d")).apply {
                        addActionListener {
                            device.installTalkBackForDevelopers().take(1).subscribe {
                                updateDeviceBasedOnInstalledServices(device, parentPanel,true)
                                if (!it) {
                                    controller.showInstallTB4DErrorNotification()
                                }
                            }
                        }
                    })
                    add(JButton(CustomIcon.INFO.create()).apply {
                        addActionListener {
                            BrowserUtil.browse(TB4DWebPage)
                        }
                    })
                }, BorderLayout.EAST)
            } else {
                if (onRecur) {
                    controller.showInstallTB4DSuccessNotification()
                }
            }
        }
    }

    fun create() = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JLabel(localize("panel.device.title")))
        add(deviceListPanel)
        deviceListener
            .distinctUntilChanged()
            .subscribe {
                deviceListPanel.removeAll()
                if (it.isEmpty()) {
                    deviceListPanel.add(JLabel(localize("panel.device.no_devices")))
                } else {
                    it.forEach { device ->
                        deviceListPanel.add(createDeviceSelectionCheckBox(device))
                    }
                }
            }
    }
}