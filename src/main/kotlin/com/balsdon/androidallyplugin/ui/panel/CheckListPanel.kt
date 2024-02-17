package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import java.awt.GridBagLayout
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

/**
 * [Future idea]
 * Creates the Checklist panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class CheckListPanel(private val controller: Controller) {
    private val checkListOptions = listOf(
        // 1.1.1 Non-text content [A]
        "panel.checklist.label.image_buttons",
        "panel.checklist.label.alt_text"
        // 1.2.1 Audio-only and Video-only (Prerecorded) [A]
        // 1.2.2 Captions (Prerecorded) [A]
        // 1.2.3 Audio Description or Media Alternative (Prerecorded) [A]
        // 1.2.4 Captions (Live) [AA]
        // 1.2.5 Audio Description (Prerecorded) [AA]
        // 1.3.1 Info and Relationships [A]
        // 1.3.2 Meaningful Sequence [A]
        // 1.3.3 Sensory Characteristics [A]
        // 1.4.1 Use of Color [A]
        // 1.4.2 Audio Control [A]
        // 1.4.3 Contrast (Minimum) [AA]
        // 1.4.4 Resize Text [AA]
        // 1.4.5 Images of Text [AA]
        // 2.1.1 Keyboard [A]
        // 2.1.2 No Keyboard Trap [A]
        // 2.2.1 Timing Adjustable [A]
        // 2.2.2 Pause, Stop, Hide [A]
        // 2.3.1 Three Flashes or Below Threshold [A]
        // 2.4.1 Bypass Blocks [A]
        // 2.4.2 Page Titled [A]
        // 2.4.3 Focus Order [A]
        // 2.4.4 Link Purpose (In Context) [A]
        // 2.4.5 Multiple Ways [AA]
        // 2.4.6 Headings and Labels [AA]
        // 2.4.7 Focus Visible [AA]
        // 2.5 Input Modalities
        // 3.2.1 On Focus [A]
        // 3.2.2 On Input [A]
        // 3.2.3 Consistent Navigation [AA]
        // 3.2.4 Consistent Identification [AA]
        // 3.3.1 Error Identification [A]
        // 3.3.2 Labels or Instructions [A]
        // 3.3.3 Error Suggestion [AA]
        // 3.3.4 Error Prevention (Legal, Financial, Data) [AA]
        // 4.1.2 Name, Role, Value [A]
    )

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                checkListOptions.forEachIndexed { index, checkOption -> createCheckOption(index, localize(checkOption)) }
            }).apply {
            autoscrolls = true
        })
    }

    private fun JPanel.createCheckOption(whichRow: Int, label: String) {

    }
}