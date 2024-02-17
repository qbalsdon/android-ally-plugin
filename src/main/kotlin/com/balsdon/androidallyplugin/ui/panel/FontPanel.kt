package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.log
import com.balsdon.androidallyplugin.utils.placeComponent
import com.intellij.ui.util.maximumHeight
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSlider

/**
 * Creates the Font panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class FontPanel(private val controller: Controller) {
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
                // font size
                addFontSizeComponent(0) { scale -> log("TODO: Set scale to [$scale]") }
                // bold font
                addBoldFontToggleComponent(1)
                // high contrast text
                addHighContrastToggleComponent(2)
            }).apply {
            autoscrolls = true
        })
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
            positiveAction = { log("TODO: Bold Font: On") },
            negativeAction = { log("TODO: Bold Font: Off") }
        )
    }

    private fun JPanel.addHighContrastToggleComponent(whichRow: Int) {
        createToggleRow(
            highContrastLabelString,
            whichRow,
            highContrastOnButtonText,
            highContrastOffButtonText,
            positiveAction = { log("TODO: High contrast text: On") },
            negativeAction = { log("TODO: High contrast text: Off") }
        )
    }
}