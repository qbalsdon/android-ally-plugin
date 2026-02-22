package com.balsdon.androidallyplugin.utils

import com.balsdon.androidallyplugin.localize
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.JBUI
import javax.swing.Box
import javax.swing.DefaultComboBoxModel
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Component
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

@Suppress("LongParameterList")
fun JPanel.placeComponent(
    component: Component,
    x: Int,
    y: Int,
    width: Int = 1,
    height: Int = 1,
    fillType: Int = GridBagConstraints.HORIZONTAL,
    anchorType: Int = GridBagConstraints.NORTHWEST
) {
    add(component, GridBagConstraints().apply {
        gridx = x
        gridy = y
        gridwidth = width
        gridheight = height
        anchor = anchorType
        weightx = 1.0
        fill = fillType
        insets = JBUI.emptyInsets()
    })
}

@Suppress("LongParameterList")
fun JPanel.createToggleRow(
    label: String,
    whichRow: Int,
    positiveLabel: String,
    negativeLabel: String,
    colStart: Int = 3,
    colSpan: Int = 2,
    secondColSpan: Int? = null,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit
) {
    placeComponent(
        JLabel(label).apply {
            setMaxComponentSize()
        },
        x = 0,
        y = whichRow,
        width = 2,
        anchorType = GridBagConstraints.CENTER
    )
    placeComponent(JButton(positiveLabel).apply {
        setMaxComponentSize()
        addKeyAndActionListener(positiveAction)
    }, colStart, width = colSpan, y = whichRow)
    placeComponent(JButton(negativeLabel).apply {
        setMaxComponentSize()
        addKeyAndActionListener(negativeAction)
    }, colStart + colSpan, width = secondColSpan ?: colSpan, y = whichRow)
}

fun JPanel.createDropDownMenu(
    label: String,
    whichRow: Int,
    options: List<String>,
    width: Int = 4,
    onSelectionChanged: (String) -> Unit
) {
    placeComponent(
        JLabel(label).apply { setMaxComponentSize() },
        x = 0,
        y = whichRow,
        width = 2,
        anchorType = GridBagConstraints.CENTER
    )

    placeComponent(ComboBox(DefaultComboBoxModel<String>().apply {
        options.forEach { addElement(localize(it)) }
    }).apply {
        setMaxComponentSize()
        addActionListener {
            onSelectionChanged(options[this.selectedIndex])
        }
    }, x = 3, y = whichRow, width = width)
}

fun JPanel.createDropDownMenu(
    label: String,
    whichRow: Int,
    options: List<String>,
    onSelectionChanged: (String) -> Unit
) {
    createDropDownMenu(label, whichRow, options, width = 4, onSelectionChanged)
}

fun JPanel.addFiller(index: Int = -1) {
    if (index == -1) {
        add(
            Box.Filler(
                Dimension(0, 0),
                Dimension(0, Int.MAX_VALUE),
                Dimension(0, Int.MAX_VALUE)
            )
        )
    } else {
        add(
            Box.Filler(
                Dimension(0, 0),
                Dimension(0, Int.MAX_VALUE),
                Dimension(0, Int.MAX_VALUE)
            ),
            index
        )
    }
}

fun JButton.addKeyAndActionListener(onClick: () -> Unit) {
    addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER || e.keyCode == KeyEvent.VK_SPACE) {
                onClick()
            }
        }
    })

    addActionListener {
        onClick()
    }
}