package com.balsdon.androidallyplugin.ui.component

import com.intellij.ui.components.JBTabbedPane
import java.awt.Component

class TabbedPanel(private val tabs: Map<String, Component>) {
    fun create() = JBTabbedPane().also {
            tabs.forEach { (title, tabContent) ->
                it.addTab(title, tabContent)
            }
    }
}