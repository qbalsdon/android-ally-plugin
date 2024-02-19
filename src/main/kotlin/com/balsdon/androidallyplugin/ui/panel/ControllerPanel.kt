package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.adb.AdbScript
import com.balsdon.androidallyplugin.controller.Controller

open class ControllerPanel(private val controller: Controller) {
    protected fun AdbScript.run() {
        controller.runOnAllValidSelectedDevices { device -> device.executeScript(this.asScript()) }
    }
}