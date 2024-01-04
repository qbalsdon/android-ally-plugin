package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.intellij.ui.util.maximumHeight
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * Creates the TalkBack panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class TalkBackPanel(private val controller: Controller) {

    private val talkBackLabelString = localize("panel.talkback.label.talkback")
    private val talkBackOnButtonText = localize("panel.talkback.button.talkback.on")
    private val talkBackOffButtonText = localize("panel.talkback.button.talkback.off")

    fun create() = JPanel().apply {
        layout = GridLayout()
        add(JPanel().apply {
            maximumHeight = elementMaxHeight
            add(JLabel(talkBackLabelString))
            add(JButton(talkBackOnButtonText).apply {
                addActionListener {
                    controller.runOnAllValidSelectedDevices { device -> device.turnOnTalkBack() }
                }
            })
            add(JButton(talkBackOffButtonText).apply {
                addActionListener {
                    controller.runOnAllValidSelectedDevices { device -> device.turnOffTalkBack() }
                }
            })
        })
    }
}