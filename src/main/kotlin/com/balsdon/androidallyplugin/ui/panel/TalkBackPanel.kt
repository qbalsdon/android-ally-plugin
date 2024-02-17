package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.ui.CustomIcon
import com.balsdon.androidallyplugin.ui.component.IconButton
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.log
import com.balsdon.androidallyplugin.utils.placeComponent
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.util.maximumHeight
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane


/**
 * Creates the TalkBack panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class TalkBackPanel(private val controller: Controller) {
    private val talkBackLabelString = localize("panel.talkback.label.talkback")
    private val talkBackOnButtonText = localize("panel.talkback.button.talkback.on")
    private val talkBackOffButtonText = localize("panel.talkback.button.talkback.off")

    private val talkBackSpeechOutputLabelString = localize("panel.talkback.label.talkback.speech")
    private val talkBackSpeechOutputOnButtonText = localize("panel.talkback.button.talkback.speech.on")
    private val talkBackSpeechOutputOffButtonText = localize("panel.talkback.button.talkback.speech.off")

    private val talkBackVolumeLabelString = localize("panel.talkback.label.talkback.volume")
    private val talkBackVolumeMediumButtonText = localize("panel.talkback.button.talkback.volume.medium")
    private val talkBackVolumeLowButtonText = localize("panel.talkback.button.talkback.volume.low")

    private val navigationLabelString = localize("panel.talkback.label.talkback.navigation")
    private val previousButtonText = localize("panel.talkback.button.previous")
    private val nextButtonText = localize("panel.talkback.button.next")
    private val tapButtonText = localize("panel.talkback.button.tap")
    private val longTapButtonText = localize("panel.talkback.button.longTap")
    private val backButtonText = localize("panel.talkback.button.back")
    private val menuButtonText = localize("panel.talkback.button.menu")
    private val actionsButtonText = localize("panel.talkback.button.actions")

    private val granularityLabelString = localize("panel.talkback.label.granularity")
    private val granularityOptions = listOf(
        "panel.talkback.label.granularity.default",
        "panel.talkback.label.granularity.headings",
        "panel.talkback.label.granularity.controls",
        "panel.talkback.label.granularity.links",
        "panel.talkback.label.granularity.words",
        "panel.talkback.label.granularity.paragraphs",
        "panel.talkback.label.granularity.characters",
        "panel.talkback.label.granularity.lines",
        "panel.talkback.label.granularity.window"
    )

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                addTalkBackToggleComponent(0)
                addGranularityCombo(1) { option -> log("Selected: [$option]") }
                addBasicControls(2)
                addAdvancedControls(3)
                addVolumeToggleComponent(4)
                addSpeechOutputToggleComponent(5)
            }).apply {
            autoscrolls = true
        })
    }

    private fun JPanel.addTalkBackToggleComponent(whichRow: Int) {
        createToggleRow(
            talkBackLabelString,
            whichRow,
            talkBackOnButtonText,
            talkBackOffButtonText,
            positiveAction = { controller.runOnAllValidSelectedDevices { device -> device.turnOnTalkBack() } },
            negativeAction = { controller.runOnAllValidSelectedDevices { device -> device.turnOffTalkBack() } }
        )
    }

    private fun JPanel.addSpeechOutputToggleComponent(whichRow: Int) {
        createToggleRow(
            talkBackSpeechOutputLabelString,
            whichRow,
            talkBackSpeechOutputOnButtonText,
            talkBackSpeechOutputOffButtonText,
            positiveAction = { log("TODO: Speech output: On") },
            negativeAction = { log("TODO: Speech output: Off") }
        )
    }

    private fun JPanel.addVolumeToggleComponent(whichRow: Int) {
        createToggleRow(
            talkBackVolumeLabelString,
            whichRow,
            talkBackVolumeMediumButtonText,
            talkBackVolumeLowButtonText,
            positiveAction = { log("TODO: Volume: Medium") },
            negativeAction = { log("TODO: Volume: Low") }
        )
    }

    private fun JPanel.addGranularityCombo(whichRow: Int, onOption: (String) -> Unit) {
        createDropDownMenu(granularityLabelString, whichRow, granularityOptions, onOption)
    }

    private fun JPanel.addBasicControls(whichRow: Int) {
        placeComponent(JLabel(navigationLabelString).apply { maximumHeight = elementMaxHeight },
            x = 0, y = whichRow, 2, top = true
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_SWIPE_LEFT,
                previousButtonText,
                ""
            ) { log("Previous") }.create(),
            3, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_SWIPE_RIGHT,
                nextButtonText,
                ""
            ) { log("Next") }.create(),
            4, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_TAP,
                tapButtonText,
                ""
            ) { log("Tap") }.create(),
            5, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_TAP_LONG,
                longTapButtonText,
                ""
            ) { log("Long Tap") }.create(),
            6, y = whichRow, fillType = GridBagConstraints.BOTH
        )
    }

    private fun JPanel.addAdvancedControls(whichRow: Int) {
        placeComponent(
            IconButton(
                CustomIcon.DEVICE_BACK,
                backButtonText,
                ""
            ) { log("Back") }.create(),
            3, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_OPEN_MENU,
                menuButtonText,
                ""
            ) { log("Menu") }.create(),
            5, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_ACTIONS,
                actionsButtonText,
                ""
            ) { log("Actions") }.create(),
            6, y = whichRow, fillType = GridBagConstraints.BOTH
        )
    }
}