package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.AndroidStudioPluginNotificationPayload
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
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
        add(JLabel(talkBackLabelString))
        add(JButton(talkBackOnButtonText).apply {
            addActionListener {
                controller.showNotification(
                    AndroidStudioPluginNotificationPayload(
                        "Test Title: On", "Test Message: On"
                    )
                )
            }
        })
        add(JButton(talkBackOffButtonText).apply {
            addActionListener {
                controller.showNotification(
                    AndroidStudioPluginNotificationPayload(
                        "Test Title: Off", "Test Message: Off"
                    )
                )
            }
        })
    }
}