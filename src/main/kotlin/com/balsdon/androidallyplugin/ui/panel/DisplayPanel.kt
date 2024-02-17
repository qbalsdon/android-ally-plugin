package com.balsdon.androidallyplugin.ui.panel

import com.android.tools.lint.detector.api.isNumberString
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.log
import com.balsdon.androidallyplugin.utils.placeComponent
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.util.maximumHeight
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.*

/**
 * Creates the Display panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class DisplayPanel(private val controller: Controller) {
    private val darkModeLabelString = localize("panel.display.dark.label")
    private val darkModeOnButtonText = localize("panel.display.dark.on")
    private val darkModeOffButtonText = localize("panel.display.dark.off")

    private val animationLabelString = localize("panel.display.animation.label")
    private val animationOnButtonText = localize("panel.display.animation.on")
    private val animationOffButtonText = localize("panel.display.animation.off")

    private val colorInversionLabelString = localize("panel.display.inversion.label")
    private val colorInversionOnButtonText = localize("panel.display.inversion.on")
    private val colorInversionOffButtonText = localize("panel.display.inversion.off")

    private val displayDensityLabelString = localize("panel.display.density.label")
    private val displayDensityDefaultString = localize("panel.display.density.default")

    private val colorCorrectionLabelString = localize("panel.display.label.correction")
    private val colorCorrectionOptions = listOf(
        "panel.display.label.correction.off",
        "panel.display.label.correction.grey",
        "panel.display.label.correction.green",
        "panel.display.label.correction.red",
        "panel.display.label.correction.blue"
    )

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                // display size secure display_density_forced [, 356, 540, 500, 460]
                addDisplayDensityComponent(0) { density -> log("TODO: Set density: [$density]") }
                // dark mode
                addDarkModeToggleComponent(1)
                // animations
                addAnimationsToggleComponent(2)
                // colour inversion
                addColorInversionToggleComponent(3)
                // color correction
                addColorCorrectionComponent(4) { option -> log("TODO: Set correction: [$option]") }
            }).apply {
            autoscrolls = true
        })
    }

    private fun JPanel.addDisplayDensityComponent(whichRow: Int, onDensitySelected: (String) -> Unit) {
        placeComponent(
            JLabel(displayDensityLabelString).apply { maximumHeight = elementMaxHeight },
            x = 0, y = whichRow, 1
        )
        val startIndex = 2
        listOf("356", displayDensityDefaultString, "460", "540", "500").forEachIndexed { index, label ->
            placeComponent(
                JButton(label).apply {
                    addActionListener {
                        onDensitySelected(
                            if (isNumberString(label)) {
                                label
                            } else {
                                ""
                            }
                        )
                    }
                },
                x = startIndex + index, y = whichRow, 1
            )
        }
    }

    private fun JPanel.addDarkModeToggleComponent(whichRow: Int) {
        createToggleRow(
            darkModeLabelString,
            whichRow,
            darkModeOnButtonText,
            darkModeOffButtonText,
            positiveAction = { log("TODO: Dark mode: On") },
            negativeAction = { log("TODO: Dark mode: Off") }
        )
    }

    private fun JPanel.addAnimationsToggleComponent(whichRow: Int) {
        createToggleRow(
            animationLabelString,
            whichRow,
            animationOnButtonText,
            animationOffButtonText,
            positiveAction = { log("TODO: Animations: On") },
            negativeAction = { log("TODO: Animations: Off") }
        )
    }

    private fun JPanel.addColorInversionToggleComponent(whichRow: Int) {
        createToggleRow(
            colorInversionLabelString,
            whichRow,
            colorInversionOnButtonText,
            colorInversionOffButtonText,
            positiveAction = { log("TODO: Color Inversion: On") },
            negativeAction = { log("TODO: Color Inversion: Off") }
        )
    }

    private fun JPanel.addColorCorrectionComponent(whichRow: Int, onOption: (String) -> Unit) {
        placeComponent(
            JLabel(colorCorrectionLabelString).apply { maximumHeight = elementMaxHeight },
            0,
            y = whichRow,
            2
        )

        placeComponent(ComboBox(DefaultComboBoxModel<String>().apply {
            colorCorrectionOptions.forEach { addElement(localize(it)) }
        }).apply {
            maximumHeight = elementMaxHeight

            addActionListener {
                onOption(colorCorrectionOptions[this.selectedIndex])
            }
        }, 3, y = whichRow, 4)
    }
}