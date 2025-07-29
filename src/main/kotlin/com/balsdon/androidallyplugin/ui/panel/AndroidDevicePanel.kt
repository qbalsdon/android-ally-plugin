package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.TB4DPackageName
import com.balsdon.androidallyplugin.TB4DWebPage
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AndroidDevice
import com.balsdon.androidallyplugin.ui.CustomIcon
import com.balsdon.androidallyplugin.utils.addFiller
import com.balsdon.androidallyplugin.utils.addKeyAndActionListener
import com.balsdon.androidallyplugin.utils.log
import com.balsdon.androidallyplugin.utils.setMaxComponentSize
import com.intellij.ide.BrowserUtil
import com.intellij.util.ui.JBUI
import javax.swing.Box
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

    @Suppress("LongMethod")
    private fun createDeviceSelectionCheckBox(device: AndroidDevice) = JPanel().apply {
        layout = BorderLayout()
        setMaxComponentSize()
        val nameLabel = JLabel(device.serial)
        val dataLabel = JLabel("...").apply {
            val oldFont = font
            @Suppress("MagicNumber")
            font = Font(oldFont.fontName, Font.ITALIC, oldFont.size - 3)
        }
        val deviceIconLabel = JLabel(
            if (device.isEmulator) {
                CustomIcon.EMULATOR.create()
            } else {
                CustomIcon.PHONE.create()
            }
        )
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
            add(deviceIconLabel.apply {
                @Suppress("MagicNumber")
                border = CompoundBorder(border, JBUI.Borders.empty(0, 10))
            })
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(nameLabel)
                add(dataLabel)
            })
        }, BorderLayout.WEST)

        name = device.serial
        device.requestData()
            .take(1)
            .subscribe { deviceInfo ->
                log("Device data: [${deviceInfo}]")
                this.name = deviceInfo.name
                nameLabel.text = deviceInfo.name
                dataLabel.text = "${device.serial} - [${deviceInfo.api} / ${deviceInfo.sdk}]"
                deviceIconLabel.icon = if (device.isEmulator) {
                    if (device.isWatch) {
                        CustomIcon.EMULATOR_WATCH.create()
                    } else {
                        CustomIcon.EMULATOR.create()
                    }
                } else {
                    if (device.isWatch) {
                        CustomIcon.WATCH.create()
                    } else {
                        CustomIcon.PHONE.create()
                    }
                }
                tb4dInstallPanel(this, device, deviceInfo.packageList)
            }
    }

    private fun tb4dInstallPanel(parentPanel: JPanel, device: AndroidDevice, services: List<String>) {
        val index = services.indexOfFirst { it.contains(TB4DPackageName) }

        if (index == -1) {
            val tb4dPanel = JPanel().apply { layout = FlowLayout(FlowLayout.TRAILING) }
            parentPanel.add(tb4dPanel.apply {
                add(JButton(localize("panel.device.label.install.tb4d")).apply {
                    addKeyAndActionListener {
                        this.isEnabled = false
                        device
                            .installTalkBackForDevelopers()
                            .take(1)
                            .subscribe {
                                this.isEnabled = true
                                if (it) {
                                    controller.showInstallTB4DSuccessNotification(device)
                                    parentPanel.remove(tb4dPanel)
                                } else {
                                    controller.showInstallTB4DErrorNotification(device)
                                }
                            }
                    }
                })
                add(JButton(CustomIcon.INFO.create()).apply {
                    addKeyAndActionListener {
                        BrowserUtil.browse(TB4DWebPage)
                    }
                })
            }, BorderLayout.EAST)
        }
    }

    fun create() = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            setMaxComponentSize()
            add(JLabel(localize("panel.device.title")).apply {
                setMaxComponentSize()
                val oldFont = font
                font = Font(oldFont.fontName, Font.BOLD, oldFont.size + 2)
            })
            add(Box.createHorizontalGlue())
            add(JButton(localize("panel.device.refresh")).apply {
                setMaxComponentSize()
                icon = CustomIcon.REFRESH.create()
                addKeyAndActionListener {
                    deviceListPanel.let {
                        it.removeAll()
                        it.add(JLabel(localize("panel.device.wait")).apply {
                            val oldFont = font
                            @Suppress("MagicNumber")
                            font = Font(oldFont.fontName, Font.BOLD, oldFont.size + 5)
                        }, BorderLayout.CENTER)
                    }
                    controller.refreshAdb()
                }
            })
        })

        add(deviceListPanel)
        deviceListener
            .distinctUntilChanged()
            .subscribe {
                deviceListPanel.removeAll()
                if (it.isEmpty()) {
                    deviceListPanel.add(JLabel(localize("panel.device.no_devices")))
                } else {
                    it.sortedWith(compareBy({ device -> device.isEmulator }, { device -> device.friendlyName }))
                        .forEach { device ->
                            deviceListPanel.add(createDeviceSelectionCheckBox(device))
                        }
                    deviceListPanel.addFiller()
                }
            }
    }
}