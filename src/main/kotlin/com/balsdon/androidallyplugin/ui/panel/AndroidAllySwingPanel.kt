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
                localize("panel.display.title") to DisplayPanel(controller).create(),
                localize("panel.font.title") to FontPanel(controller).create(),
                localize("panel.settings.title") to SettingsPanel(controller).create(),
                localize("panel.debug.title") to DebugPanel(controller).create()
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
// TODO: adb keyboard?
//          type on device
//          basic adb keys: tab, space, enter, menu, arrows
