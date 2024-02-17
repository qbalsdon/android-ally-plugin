package com.balsdon.androidallyplugin.ui.component

import com.balsdon.androidallyplugin.ui.CustomIcon
import com.intellij.util.ui.JBUI
import java.awt.Dimension
import java.awt.Font
import javax.swing.JButton
import javax.swing.SwingConstants

class IconButton(
    private val imageIcon: CustomIcon,
    private val buttonText: String,
    private val tooltip: String = "",
    private val onClick: () -> Unit
) {
    private val topMargin = 10
    private val imageButtonHeight = 60
    private val imageButtonWidth = 20
    fun create() =
        JButton().apply {
            icon = imageIcon.create()
            font = Font(font.name, font.style, font.size)
            horizontalTextPosition = SwingConstants.CENTER
            verticalTextPosition = SwingConstants.BOTTOM
            isBorderPainted = true
            isOpaque = true
            isContentAreaFilled = true
            preferredSize = Dimension(imageButtonWidth, imageButtonHeight)
            maximumSize = Dimension(imageButtonWidth, imageButtonHeight)
            margin = JBUI.insets(topMargin)
            toolTipText = tooltip
            name = buttonText
            text = buttonText
            addActionListener {
                onClick()
            }
        }
}