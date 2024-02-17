package com.balsdon.androidallyplugin.utils

import java.awt.GraphicsEnvironment
import java.awt.Image
import javax.swing.Icon
import javax.swing.ImageIcon

//TODO: Determine if this is even half way to the right approach
fun Icon.toImage(): Image =
    if (this is ImageIcon) {
        this.image
    } else {
        val image = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .defaultScreenDevice
            .defaultConfiguration
            .createCompatibleImage(iconWidth, iconHeight)
        image.createGraphics().let { graphics ->
            paintIcon(null, graphics, 0, 0)
            graphics.dispose()
        }
        image
    }