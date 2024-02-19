package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.adb.audioDescription
import com.balsdon.androidallyplugin.adb.captions
import com.balsdon.androidallyplugin.adb.timeToReact
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.GridBagLayout
import java.awt.GridLayout

/**
 * Creates the Display panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class SettingsPanel(controller: Controller): ControllerPanel(controller) {
    private val captionsLabelString = localize("panel.settings.label.captions")
    private val captionsOnButtonText = localize("panel.settings.button.captions.on")
    private val captionsOffButtonText = localize("panel.settings.button.captions.off")

    private val audioDescriptionLabelString = localize("panel.settings.description.label")
    private val audioDescriptionOnButtonText = localize("panel.settings.description.on")
    private val audioDescriptionOffButtonText = localize("panel.settings.description.off")

    private val orientationLabelString = localize("panel.settings.label.orientation")
    private val orientationOptions = listOf(
        "panel.settings.label.orientation.default",
        "panel.settings.label.orientation.portrait",
        "panel.settings.label.orientation.landscape"
    )

    private val reactionLabelString = localize("panel.settings.label.reaction")
    private val reactionOptions = listOf(
        "panel.settings.label.reaction.default",
        "panel.settings.label.reaction.ten",
        "panel.settings.label.reaction.thirty",
        "panel.settings.label.reaction.minute",
        "panel.settings.label.reaction.minutes"
    )

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                // orientation
                addOrientationComponent(0) { option ->

                }
                // time to react
                addTimeToReactComponent(1) { option ->
                    timeToReact(when (option) {
                        "panel.settings.label.reaction.ten" -> 10
                        "panel.settings.label.reaction.thirty" -> 30
                        "panel.settings.label.reaction.minute" -> 60
                        "panel.settings.label.reaction.minutes" -> 120
                        else -> 0
                    } * 1000).run()
                }
                // captions
                addCaptionsToggleComponent(2)
                // audio description
                addAudioDescriptionToggleComponent(3)
            }).apply {
            autoscrolls = true
        })
    }

    private fun JPanel.addOrientationComponent(whichRow: Int, onOption: (String) -> Unit) {
        createDropDownMenu(orientationLabelString, whichRow, orientationOptions, onOption)
    }

    private fun JPanel.addTimeToReactComponent(whichRow: Int, onOption: (String) -> Unit) {
        createDropDownMenu(reactionLabelString, whichRow, reactionOptions, onOption)
    }

    private fun JPanel.addCaptionsToggleComponent(whichRow: Int) {
        createToggleRow(
            captionsLabelString,
            whichRow,
            captionsOnButtonText,
            captionsOffButtonText,
            positiveAction = { captions(true).run() },
            negativeAction = { captions(false).run() }
        )
    }

    private fun JPanel.addAudioDescriptionToggleComponent(whichRow: Int) {
        createToggleRow(
            audioDescriptionLabelString,
            whichRow,
            audioDescriptionOnButtonText,
            audioDescriptionOffButtonText,
            positiveAction = { audioDescription(true).run() },
            negativeAction = { audioDescription(false).run() }
        )
    }
}