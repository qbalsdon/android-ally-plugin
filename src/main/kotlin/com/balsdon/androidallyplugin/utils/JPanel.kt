package com.balsdon.androidallyplugin.utils

import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.util.maximumHeight
import javax.swing.DefaultComboBoxModel
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Component
import java.awt.GridBagConstraints

fun JPanel.placeComponent(
    component: Component,
    x: Int,
    y: Int,
    w: Int = 1,
    h: Int = 1,
    top: Boolean = false,
    fillType: Int = GridBagConstraints.HORIZONTAL
) {
    add(component, GridBagConstraints().apply {
        gridx = x
        gridy = y
        gridwidth = w
        gridheight = h
        weightx = 1.0
        weighty = 1.0
        if (top) {
            anchor = GridBagConstraints.NORTHWEST
        }
        fill = fillType
    })
}

fun JPanel.createToggleRow(
    label: String,
    whichRow: Int,
    positiveLabel: String,
    negativeLabel: String,
    colStart: Int = 3,
    colSpan: Int = 2,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit
) {
    placeComponent(JLabel(label).apply {
        maximumHeight = elementMaxHeight
    }, 0, y = whichRow, 2)
    placeComponent(JButton(positiveLabel).apply {
        maximumHeight = elementMaxHeight
        addActionListener {
            positiveAction()
        }
    }, colStart, w = colSpan, y = whichRow)
    placeComponent(JButton(negativeLabel).apply {
        maximumHeight = elementMaxHeight
        addActionListener {
            negativeAction()
        }
    }, colStart + colSpan, w = colSpan, y = whichRow)
}

fun JPanel.createDropDownMenu(label: String, whichRow: Int, options: List<String>, onSelectionChanged: (String) -> Unit) {
    placeComponent(JLabel(label).apply { maximumHeight = elementMaxHeight }, 0, y = whichRow, 2)

    placeComponent(ComboBox(DefaultComboBoxModel<String>().apply {
        options.forEach { addElement(localize(it)) }
    }).apply {
        maximumHeight = elementMaxHeight

        addActionListener {
            onSelectionChanged(options[this.selectedIndex])
        }
    }, 3, y = whichRow, 4)
}