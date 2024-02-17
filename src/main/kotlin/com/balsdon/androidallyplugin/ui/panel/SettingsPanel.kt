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
 * Creates the Display panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class SettingsPanel(private val controller: Controller) {
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
                addOrientationComponent(0) { option -> log("TODO: Set orientation: [$option]") }
                // time to react
                addTimeToReactComponent(1) { option -> log("TODO: Time to react: [$option]") }
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
            positiveAction = { log("TODO: Captions: On") },
            negativeAction = { log("TODO: Captions: Off") }
        )
    }

    private fun JPanel.addAudioDescriptionToggleComponent(whichRow: Int) {
        createToggleRow(
            audioDescriptionLabelString,
            whichRow,
            audioDescriptionOnButtonText,
            audioDescriptionOffButtonText,
            positiveAction = { log("TODO: Audio description: On") },
            negativeAction = { log("TODO: Audio description: Off") }
        )
    }
}