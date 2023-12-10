package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.ui.component.TabbedPanel
import com.balsdon.androidallyplugin.ui.contract.MainPlugin
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSplitPane

class AndroidAllySwingPanel(private val controller: Controller) : MainPlugin {

    private val devicePanel by lazy { AndroidDevicePanel(controller).create() }
    private val tabPanel by lazy {
        TabbedPanel(
            mapOf(
                localize("panel.talkback.title") to TalkBackPanel(controller).create(),
                "Display" to JPanel().apply { add(JLabel("Panel: Display")) }
            )
        ).create()
    }

    private val content: JSplitPane
        get() = JSplitPane(JSplitPane.HORIZONTAL_SPLIT).apply {
            resizeWeight = 0.3
            leftComponent = devicePanel
            rightComponent = tabPanel
        }

    override fun build(postConstruction: (content: Any) -> Unit) {
        postConstruction(content)
    }
}