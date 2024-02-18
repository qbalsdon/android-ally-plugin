package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller

open class ControllerPanel(private val controller: Controller) {
    protected fun execute(scriptString: String) {
        controller.runOnAllValidSelectedDevices { device -> device.executeScript(scriptString) }
    }
}