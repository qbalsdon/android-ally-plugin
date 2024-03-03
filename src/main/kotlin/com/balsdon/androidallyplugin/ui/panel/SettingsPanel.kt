package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.adb.audioDescription
import com.balsdon.androidallyplugin.adb.boldFont
import com.balsdon.androidallyplugin.adb.captions
import com.balsdon.androidallyplugin.adb.fontScale
import com.balsdon.androidallyplugin.adb.highTextContrast
import com.balsdon.androidallyplugin.adb.timeToReact
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.placeComponent
import com.intellij.ui.util.maximumHeight
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSlider
import java.awt.GridBagLayout
import java.awt.GridLayout

/**
 * Creates the Display panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class SettingsPanel(controller: Controller) : ControllerPanel(controller) {
    private val captionsLabelString = localize("panel.settings.label.captions")
    private val captionsOnButtonText = localize("panel.settings.button.captions.on")
    private val captionsOffButtonText = localize("panel.settings.button.captions.off")

    private val audioDescriptionLabelString = localize("panel.settings.description.label")
    private val audioDescriptionOnButtonText = localize("panel.settings.description.on")
    private val audioDescriptionOffButtonText = localize("panel.settings.description.off")

    private val reactionLabelString = localize("panel.settings.label.reaction")
    private val reactionOptions = listOf(
        "panel.settings.label.reaction.default",
        "panel.settings.label.reaction.ten",
        "panel.settings.label.reaction.thirty",
        "panel.settings.label.reaction.minute",
        "panel.settings.label.reaction.minutes"
    )

    private val layoutFontScaleLabelString = localize("panel.font.label.scale")

    private val boldFontLabelString = localize("panel.font.bold.label")
    private val boldFontOnButtonText = localize("panel.font.bold.on")
    private val boldFontOffButtonText = localize("panel.font.bold.off")

    private val highContrastLabelString = localize("panel.font.contrast.label")
    private val highContrastOnButtonText = localize("panel.font.contrast.on")
    private val highContrastOffButtonText = localize("panel.font.contrast.off")

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                addFontSizeComponent(0) { scale -> fontScale(scale).run() }
                // bold font
                addBoldFontToggleComponent(1)
                // high contrast text
                addHighContrastToggleComponent(2)
                // time to react
                addTimeToReactComponent(3) { option ->
                    timeToReact(
                        when (option) {
                            "panel.settings.label.reaction.ten" -> 10
                            "panel.settings.label.reaction.thirty" -> 30
                            "panel.settings.label.reaction.minute" -> 60
                            "panel.settings.label.reaction.minutes" -> 120
                            else -> 0
                        } * 1000
                    ).run()
                }
                // captions
                addCaptionsToggleComponent(4)
                // audio description
                addAudioDescriptionToggleComponent(5)
            }).apply {
            autoscrolls = true
        })
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

    private fun JPanel.addFontSizeComponent(whichRow: Int, onSliderChanged: (Float) -> Unit) {
        val label = JLabel(layoutFontScaleLabelString).apply { maximumHeight = elementMaxHeight }
        val slider = JSlider(50, 300, 100).apply {
            maximumHeight = elementMaxHeight
            paintTrack = true
            paintTicks = true
            paintLabels = true
            snapToTicks = true
            majorTickSpacing = 50
            minorTickSpacing = 10
            addChangeListener {
                val floatValue = value / 100f
                if (!this.valueIsAdjusting) {
                    onSliderChanged(floatValue)
                }
            }
        }

        placeComponent(
            label,
            x = 0, y = whichRow, 1
        )
        placeComponent(
            slider,
            x = 3, y = whichRow, 4
        )
    }

    private fun JPanel.addBoldFontToggleComponent(whichRow: Int) {
        createToggleRow(
            boldFontLabelString,
            whichRow,
            boldFontOnButtonText,
            boldFontOffButtonText,
            positiveAction = { boldFont(true).run() },
            negativeAction = { boldFont(false).run() }
        )
    }

    private fun JPanel.addHighContrastToggleComponent(whichRow: Int) {
        createToggleRow(
            highContrastLabelString,
            whichRow,
            highContrastOnButtonText,
            highContrastOffButtonText,
            positiveAction = { highTextContrast(true).run() },
            negativeAction = { highTextContrast(false).run() }
        )
    }
}