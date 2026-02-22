package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.adb.audioDescription
import com.balsdon.androidallyplugin.adb.boldFont
import com.balsdon.androidallyplugin.adb.captions
import com.balsdon.androidallyplugin.adb.fontScale
import com.balsdon.androidallyplugin.adb.highTextContrast
import com.balsdon.androidallyplugin.adb.timeToReact
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.addKeyAndActionListener
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.placeComponent
import com.balsdon.androidallyplugin.utils.setMaxComponentSize
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

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
    private val layoutFontScaleResetString = localize("panel.font.label.reset")

    private val boldFontLabelString = localize("panel.font.bold.label")
    private val boldFontOnButtonText = localize("panel.font.bold.on")
    private val boldFontOffButtonText = localize("panel.font.bold.off")

    private val highContrastLabelString = localize("panel.font.contrast.label")
    private val highContrastOnButtonText = localize("panel.font.contrast.on")
    private val highContrastOffButtonText = localize("panel.font.contrast.off")

    @Suppress("MagicNumber")
    private val defaultFontScale = 100

    override fun buildComponent() = JPanel().apply {
        layout = GridBagLayout()
        addFontSizeComponent(whichRow = 0) { scale -> fontScale(scale).run() }
        // bold font
        addBoldFontToggleComponent(whichRow = 1)
        // high contrast text
        addHighContrastToggleComponent(whichRow = 2)
        // time to react
        addTimeToReactComponent(whichRow = 3) { option ->
            @Suppress("MagicNumber")
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
        addCaptionsToggleComponent(whichRow = 4)
        // audio description
        addAudioDescriptionToggleComponent(whichRow = 5)
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
        val label = JLabel(layoutFontScaleLabelString).apply { setMaxComponentSize() }
        @Suppress("MagicNumber")
        val slider = JSlider(50, 300, 100).apply {
            setMaxComponentSize()
            paintTrack = true
            paintTicks = true
            paintLabels = true
            snapToTicks = true
            @Suppress("MagicNumber")
            majorTickSpacing = 50
            @Suppress("MagicNumber")
            minorTickSpacing = 10
            addChangeListener {
                val floatValue = value / defaultFontScale.toFloat() // percent to float
                if (!this.valueIsAdjusting) {
                    onSliderChanged(floatValue)
                }
            }
        }
        val resetButton = JButton(layoutFontScaleResetString).apply {
            setMaxComponentSize()
            addKeyAndActionListener { slider.value = defaultFontScale }
        }

        placeComponent(
            label,
            x = 0, y = whichRow, width = 1, anchorType = GridBagConstraints.CENTER
        )
        placeComponent(
            resetButton,
            x = 1, y = whichRow, width = 1, anchorType = GridBagConstraints.CENTER
        )
        placeComponent(
            slider,
            x = 3, y = whichRow, width = 4
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