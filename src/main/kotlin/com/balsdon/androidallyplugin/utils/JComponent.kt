package com.balsdon.androidallyplugin.utils

import com.balsdon.androidallyplugin.elementMaxHeight
import javax.swing.JComponent

fun JComponent.setMaxComponentSize() {
    preferredSize.height = elementMaxHeight
}