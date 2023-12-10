package com.balsdon.androidallyplugin

import com.balsdon.androidallyplugin.controller.AndroidStudioPluginController
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.ui.contract.MainPlugin
import com.balsdon.androidallyplugin.ui.panel.AndroidAllySwingPanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JComponent

/**
 * https://github.com/JetBrains/intellij-sdk-code-samples/blob/main/tool_window/src/main/resources/META-INF/plugin.xml
 * https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html#panelgroup
 * https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
 */

class AndroidAllyPluginToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val controller: Controller = AndroidStudioPluginController(project)
        val mainPanel: MainPlugin = AndroidAllySwingPanel(controller)

        mainPanel.build { content ->
            toolWindow.contentManager.addContent(ContentFactory.getInstance().createContent(content as JComponent, "", false))
        }
    }
}