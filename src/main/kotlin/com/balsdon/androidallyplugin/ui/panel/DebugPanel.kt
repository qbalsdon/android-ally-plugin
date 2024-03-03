package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.TB4DWebPage
import com.balsdon.androidallyplugin.TB4DWebPageNeedHelp
import com.balsdon.androidallyplugin.adb.accessibilityScannerService
import com.balsdon.androidallyplugin.adb.layoutBounds
import com.balsdon.androidallyplugin.adb.openScreen
import com.balsdon.androidallyplugin.adb.parameters.SettingsScreen
import com.balsdon.androidallyplugin.adb.showTouches
import com.balsdon.androidallyplugin.adb.switchAccessService
import com.balsdon.androidallyplugin.adb.talkBackService
import com.balsdon.androidallyplugin.adb.voiceAccessService
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.log
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.GridBagLayout
import java.awt.GridLayout

/**
 * Creates the Debug panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 *
 * The reason why Switch Access is on here:
 *   - You can't use the ADB to send keyboard command to an accessibility service
 *   - There are no ADB commands for Switch Access, and it's not open source
 *   - [Ally Keys](https://www.ally-keys.com/) is the tool that would be used, or you can use the volume keys or a keyboard
 *
 * The reason why Accessibility Scanner is on here:
 *   - Developers need to take responsibility for installation and use of this tool, AAP can't provide anything more than a toggle
 */
class DebugPanel(private val controller: Controller) : ControllerPanel(controller) {
    private val layoutBoundsLabelString = localize("panel.debug.label.bounds")
    private val layoutBoundsOnButtonText = localize("panel.debug.button.bounds.on")
    private val layoutBoundsOffButtonText = localize("panel.debug.button.bounds.off")

    private val showTapsLabelString = localize("panel.debug.label.taps")
    private val showTapsOnButtonText = localize("panel.debug.button.taps.on")
    private val showTapsOffButtonText = localize("panel.debug.button.taps.off")

    private val accessibilityScannerLabelString = localize("panel.debug.label.scanner")
    private val accessibilityScannerOnButtonText = localize("panel.debug.button.scanner.on")
    private val accessibilityScannerOffButtonText = localize("panel.debug.button.scanner.off")

    private val voiceAccessLabelString = localize("panel.debug.label.voice_access")
    private val voiceAccessOnButtonText = localize("panel.debug.button.voice_access.on")
    private val voiceAccessOffButtonText = localize("panel.debug.button.voice_access.off")

    private val switchAccessLabelString = localize("panel.debug.label.switch_access")
    private val switchAccessOnButtonText = localize("panel.debug.button.switch_access.on")
    private val switchAccessOffButtonText = localize("panel.debug.button.switch_access.off")

    private val openScreenLabelString = localize("panel.debug.label.screen")
    private val openScreenOptions = SettingsScreen.entries.map { it.reference }

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                // open screen
                addOpenScreenComponent(0) { option ->
                    val screen = SettingsScreen.entries.first { it.reference == option }
                    openScreen(screen).run()
                }
                // layout bounds
                addLayoutBoundsToggleComponent(1)
                // show taps
                addShowTapsToggleComponent(2)
                // accessibility scanner
                addAccessibilityScannerToggleComponent(3)
                // voice access
                addVoiceAccessToggleComponent(4) {
                    controller.showNotification(
                        localize("notification.voice.access.help.title"),
                        localize("notification.voice.access.help.message"),
                        NotificationType.INFORMATION,
                        listOf(
                            object : NotificationAction(localize("notification.voice.access.help.action")) {
                                override fun actionPerformed(event: AnActionEvent, notification: Notification) {
                                    BrowserUtil.browse(TB4DWebPageNeedHelp)
                                }
                            }
                        )
                    )
                }
                // switch access
                addSwitchAccessToggleComponent(5) {
                    controller.showNotification(
                        localize("notification.switch.access.help.title"),
                        localize("notification.switch.access.help.message"),
                        NotificationType.INFORMATION,
                        listOf(
                            object : NotificationAction(localize("notification.switch.access.help.action")) {
                                override fun actionPerformed(event: AnActionEvent, notification: Notification) {
                                    BrowserUtil.browse(TB4DWebPageNeedHelp)
                                }
                            }
                        )
                    )
                }
            }).apply {
            autoscrolls = true
        })
    }

    private fun JPanel.addOpenScreenComponent(whichRow: Int, onOption: (String) -> Unit) {
        createDropDownMenu(openScreenLabelString, whichRow, openScreenOptions, onOption)
    }

    private fun JPanel.addLayoutBoundsToggleComponent(whichRow: Int) {
        createToggleRow(
            layoutBoundsLabelString,
            whichRow,
            layoutBoundsOnButtonText,
            layoutBoundsOffButtonText,
            positiveAction = { layoutBounds(true).run() },
            negativeAction = { layoutBounds(false).run() }
        )
    }

    private fun JPanel.addShowTapsToggleComponent(whichRow: Int) {
        createToggleRow(
            showTapsLabelString,
            whichRow,
            showTapsOnButtonText,
            showTapsOffButtonText,
            positiveAction = { showTouches(true).run() },
            negativeAction = { showTouches(false).run() }
        )
    }

    private fun JPanel.addAccessibilityScannerToggleComponent(whichRow: Int) {
        createToggleRow(
            accessibilityScannerLabelString,
            whichRow,
            accessibilityScannerOnButtonText,
            accessibilityScannerOffButtonText,
            positiveAction = { accessibilityScannerService(true).run() },
            negativeAction = { accessibilityScannerService(false).run() }
        )
    }

    private fun JPanel.addVoiceAccessToggleComponent(whichRow: Int, showNotification: () -> Unit) {
        createToggleRow(
            voiceAccessLabelString,
            whichRow,
            voiceAccessOnButtonText,
            voiceAccessOffButtonText,
            positiveAction = {
                showNotification()
                voiceAccessService(true).run()
            },
            negativeAction = { voiceAccessService(false).run() }
        )
    }

    private fun JPanel.addSwitchAccessToggleComponent(whichRow: Int, showNotification: () -> Unit) {
        createToggleRow(
            switchAccessLabelString,
            whichRow,
            switchAccessOnButtonText,
            switchAccessOffButtonText,
            positiveAction = {
                showNotification()
                switchAccessService(true).run()
            },
            negativeAction = { switchAccessService(false).run() }
        )
    }
}