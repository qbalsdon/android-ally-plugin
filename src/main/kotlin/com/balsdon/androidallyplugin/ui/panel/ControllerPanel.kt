package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.adb.AdbScript
import com.balsdon.androidallyplugin.controller.Controller
import com.intellij.ui.components.JBScrollPane
import javax.swing.JPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.awt.Component

abstract class ControllerPanel(private val controller: Controller) {
    protected fun AdbScript.run() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            controller.runOnAllValidSelectedDevices { device -> device.executeScript(this@run.asScript()) }
        }
    }

    abstract fun buildComponent(): Component

    fun create(): Component = JBScrollPane(
        JPanel().apply {
            layout = BorderLayout()
            add(buildComponent(), BorderLayout.NORTH)
        }
    ).apply {
        autoscrolls = true
    }
}