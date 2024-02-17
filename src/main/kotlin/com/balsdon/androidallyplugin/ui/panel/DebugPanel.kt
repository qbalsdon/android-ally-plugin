package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.log
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

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
class DebugPanel(private val controller: Controller) {
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
    private val openScreenOptions = listOf(
        "panel.debug.label.screen.settings",
        "panel.debug.label.screen.developer",
        "panel.debug.label.screen.accessibility",
        "panel.debug.label.screen.locale",
        "panel.debug.label.screen.colors",
        "panel.debug.label.screen.display",
        "panel.debug.label.screen.dark",
        "panel.debug.label.screen.bluetooth",
        "panel.debug.label.screen.wifi",
        "panel.debug.label.screen.network",
    )

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                // open screen
                addOpenScreenComponent(0) { option -> log("TODO: Open screen: [$option]") }
                // layout bounds
                addLayoutBoundsToggleComponent(1)
                // show taps
                addShowTapsToggleComponent(2)
                // accessibility scanner
                addAccessibilityScannerToggleComponent(3)
                // voice access
                addVoiceAccessToggleComponent(4)
                // switch access
                addSwitchAccessToggleComponent(5)
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
            positiveAction = { log("TODO: Layout Bounds: On") },
            negativeAction = { log("TODO: Layout Bounds: Off") }
        )
    }

    private fun JPanel.addShowTapsToggleComponent(whichRow: Int) {
        createToggleRow(
            showTapsLabelString,
            whichRow,
            showTapsOnButtonText,
            showTapsOffButtonText,
            positiveAction = { log("TODO: Show taps: On") },
            negativeAction = { log("TODO: Show taps: Off") }
        )
    }

    private fun JPanel.addAccessibilityScannerToggleComponent(whichRow: Int) {
        createToggleRow(
            accessibilityScannerLabelString,
            whichRow,
            accessibilityScannerOnButtonText,
            accessibilityScannerOffButtonText,
            positiveAction = { log("TODO: Accessibility Scanner: On") },
            negativeAction = { log("TODO: Accessibility Scanner: Off") }
        )
    }

    private fun JPanel.addVoiceAccessToggleComponent(whichRow: Int) {
        createToggleRow(
            voiceAccessLabelString,
            whichRow,
            voiceAccessOnButtonText,
            voiceAccessOffButtonText,
            positiveAction = { log("TODO: Voice Access: On") },
            negativeAction = { log("TODO: Voice Access: Off") }
        )
    }

    private fun JPanel.addSwitchAccessToggleComponent(whichRow: Int) {
        createToggleRow(
            switchAccessLabelString,
            whichRow,
            switchAccessOnButtonText,
            switchAccessOffButtonText,
            positiveAction = { log("TODO: Switch Access: On") },
            negativeAction = { log("TODO: Switch Access: Off") }
        )
    }
}