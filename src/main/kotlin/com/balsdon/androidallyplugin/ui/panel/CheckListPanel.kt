package com.balsdon.androidallyplugin.ui.panel

import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.data.checkListItemList
import com.balsdon.androidallyplugin.elementMaxHeight
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.model.AccessibilityPrinciple
import com.balsdon.androidallyplugin.model.AssistiveTechnologyType
import com.balsdon.androidallyplugin.model.CheckListItem
import com.balsdon.androidallyplugin.model.MarkDownConverter
import com.balsdon.androidallyplugin.ui.CustomIcon
import com.balsdon.androidallyplugin.utils.placeComponent
import com.balsdon.androidallyplugin.utils.setMaxComponentSize
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.Border
import javax.swing.border.CompoundBorder
import org.apache.commons.lang3.tuple.MutablePair
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class CheckListPanel(private val controller: Controller) {
    private val toClipboardNotificationTitle = localize("panel.checklist.notification.clip.title")
    private val toClipboardNotificationMessage = localize("panel.checklist.notification.clip.message")
    private val serviceGroupHeading = localize("panel.checklist.label.service")
    private val settingGroupHeading = localize("panel.checklist.label.setting")
    private val ruleGroupHeading = localize("panel.checklist.label.rule")
    private val markdownButtonText = localize("panel.checklist.button.markdown")
    private val resetButtonText = localize("panel.checklist.button.reset")

    private val selectionState: MutableMap<String, MutablePair<Boolean, Boolean>> =
        checkListItemList.associate { item -> item.id to MutablePair(false, false) }.toMutableMap()

    private val serviceCheckList: List<CheckListItem> by lazy {
        checkListItemList.filter { it.assistiveType == AssistiveTechnologyType.AccessibilityService }
    }

    private val settingCheckList: List<CheckListItem> by lazy {
        checkListItemList.filter { it.assistiveType == AssistiveTechnologyType.AccessibilitySetting }
    }

    private val ruleCheckList: List<CheckListItem> by lazy {
        checkListItemList.filter { it.assistiveType == AssistiveTechnologyType.AccessibilityRule }
    }

    private var resetPanel: () -> Unit = {}
    private val markDownConverter = MarkDownConverter()

    fun create() = JPanel().apply {
        layout = GridLayout(0, 1)
        add(JScrollPane(
            JPanel().apply {
                layout = GridBagLayout()
                resetPanel = {
                    this.removeAll()
                    selectionState.forEach { (_, pair) ->
                        pair.left = false
                        pair.right = false
                    }
                    this.addHeadingControls(whichRow = 0)
                    this.addBorderedGroup(whichRow = 1, serviceGroupHeading, serviceCheckList)
                    this.addBorderedGroup(whichRow = 2, settingGroupHeading, settingCheckList)
                    this.addBorderedGroup(whichRow = 3, ruleGroupHeading, ruleCheckList)
                }
                resetPanel()
            }).apply {
            autoscrolls = true
        })
    }

    private fun JPanel.addHeadingControls(whichRow: Int) {
        placeComponent(
            component = JPanel().apply {
                layout = FlowLayout(FlowLayout.LEADING)
                add(JButton(markdownButtonText).apply {
                    addActionListener {
                        Toolkit
                            .getDefaultToolkit()
                            .systemClipboard
                            .setContents(StringSelection(markDownConverter(selectionState)), null)
                        controller.showNotification(
                            title = toClipboardNotificationTitle,
                            message = toClipboardNotificationMessage,
                            type = NotificationType.INFORMATION,
                            actions = listOf(
                                object : NotificationAction(resetButtonText) {
                                    override fun actionPerformed(event: AnActionEvent, notification: Notification) {
                                        resetPanel()
                                    }
                                }
                            )
                        )
                    }
                })
                add(JButton(resetButtonText).apply {
                    addActionListener { resetPanel() }
                })
            },
            x = 0,
            y = whichRow,
            w = 2,
            anchorType = GridBagConstraints.CENTER
        )
    }

    private fun JPanel.addBorderedGroup(whichRow: Int, heading: String, list: List<CheckListItem>) {
        placeComponent(
            component = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                val borderStyle: Border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(JBColor.BLACK),
                    BorderFactory.createLineBorder(JBColor.BLACK)
                )
                border = BorderFactory.createTitledBorder(
                    borderStyle, heading
                )
                list.forEach { option -> add(createCheckOption(option)) }
            },
            x = 0,
            y = whichRow,
            w = 2,
            anchorType = GridBagConstraints.CENTER
        )
    }

    @Suppress("LongMethod")
    private fun createCheckOption(option: CheckListItem) = JPanel().apply {
        layout = BorderLayout()
        setMaxComponentSize()
        val nameLabel = JLabel(localize(option.nameId)).apply {
            val oldFont = font
            @Suppress("MagicNumber")
            font = Font(oldFont.fontName, Font.ITALIC, oldFont.size + 2)
        }
        val dataLabel = JLabel(localize(option.descriptionId)).apply {
            val oldFont = font
            @Suppress("MagicNumber")
            font = Font(oldFont.fontName, Font.ITALIC, oldFont.size - 3)
        }
        val deviceIconLabel = JButton(
            when (option.accessibilityPrinciple) {
                AccessibilityPrinciple.Perceivable -> CustomIcon.PERCEIVABLE
                AccessibilityPrinciple.Operable -> CustomIcon.OPERABLE
                AccessibilityPrinciple.Understandable -> CustomIcon.UNDERSTANDABLE
                AccessibilityPrinciple.Robust -> CustomIcon.ROBUST
            }.create()
        ).apply {
            border = BorderFactory.createEmptyBorder()
            preferredSize = Dimension(elementMaxHeight, elementMaxHeight)
            addActionListener {
                BrowserUtil.browse(localize(option.accessibilityPrinciple.webReferenceId))
            }
        }

        val checkBox = JCheckBox().apply {
            isSelected = selectionState[option.id]!!.left == true
            addActionListener {
                isSelected = !isSelected
                selectionState[option.id]!!.left = isSelected
            }
        }
        val toggleSelection = {
            checkBox.isSelected = !checkBox.isSelected
            selectionState[option.id]!!.left = checkBox.isSelected
        }

        add(JPanel().apply {
            add(checkBox.apply {
                addInteractionFunction(toggleSelection)
            })
            add(deviceIconLabel.apply {
                @Suppress("MagicNumber")
                border = CompoundBorder(border, JBUI.Borders.empty(0, 10))
            })
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(nameLabel)
                add(dataLabel)
                addInteractionFunction(toggleSelection)
            })
        }, BorderLayout.WEST)
        add(JPanel().apply {
            layout = FlowLayout(FlowLayout.TRAILING)
            localize(option.linkId).split(",").forEach { linkString ->
                add(
                    JButton(CustomIcon.LINK.create()).apply {
                        addActionListener {
                            BrowserUtil.browse(linkString.split("|")[1])
                        }
                    }
                )
            }
            add(
                JButton(CustomIcon.NOT_APPLICABLE.create()).apply {
                    addActionListener {
                        selectionState[option.id]!!.right = !selectionState[option.id]!!.right
                        nameLabel.apply {
                            text = if (selectionState[option.id]!!.right) {
                                "<html><strike>${localize(option.nameId)}</strike><html>"
                            } else {
                                localize(option.nameId)
                            }
                        }
                        dataLabel.apply {
                            text = if (selectionState[option.id]!!.right) {
                                "<html><strike>${localize(option.descriptionId)}</strike><html>"
                            } else {
                                localize(option.descriptionId)
                            }
                        }
                    }
                }
            )
        }, BorderLayout.EAST)
    }

    private fun JComponent.addInteractionFunction(fn: () -> Unit) = this.apply {
        addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit
            override fun keyPressed(e: KeyEvent?) = Unit
            override fun keyReleased(e: KeyEvent?) {
                e?.also { event ->
                    if (event.keyCode == KeyEvent.VK_SPACE) {
                        fn()
                    }
                }
            }
        })
        addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                e?.also {
                    fn()
                }
            }

            override fun mousePressed(e: MouseEvent?) = Unit
            override fun mouseReleased(e: MouseEvent?) = Unit
            override fun mouseEntered(e: MouseEvent?) = Unit
            override fun mouseExited(e: MouseEvent?) = Unit
        })
    }
}